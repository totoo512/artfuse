<template>
	<view class="work-detail-container">
		<!-- 导航栏 -->
		<uni-nav-bar 
			title="作品详情" 
			:border="false" 
			background-color="#000000"
			color="#ffffff"
			left-icon="back"
			@clickLeft="goBack"
		>
			<template v-slot:right>
				<view class="nav-actions">
					<button class="action-btn" @click="shareWork">
						<uni-icons type="redo" size="18" color="#fff"></uni-icons>
					</button>
					<button class="action-btn" @click="downloadWork">
						<uni-icons type="download" size="18" color="#fff"></uni-icons>
					</button>
				</view>
			</template>
		</uni-nav-bar>

		<!-- 作品展示区 -->
		<view class="work-display">
			<swiper 
				v-if="workDetail.type === 'image' && workDetail.variations && workDetail.variations.length > 1"
				class="work-swiper"
				:indicator-dots="true"
				:autoplay="false"
				indicator-color="rgba(255,255,255,0.5)"
				indicator-active-color="#fff"
			>
				<swiper-item v-for="(variation, index) in workDetail.variations" :key="index">
					<image 
						:src="variation.url" 
						class="work-image" 
						mode="aspectFit"
						@click="previewImage(variation.url)"
					></image>
				</swiper-item>
			</swiper>
			
			<image 
				v-else-if="workDetail.type === 'image'"
				:src="workDetail.originalUrl" 
				class="work-image single" 
				mode="aspectFit"
				@click="previewImage(workDetail.originalUrl)"
			></image>
			
			<video 
				v-else-if="workDetail.type === 'video'"
				:src="workDetail.videoUrl"
				:poster="workDetail.thumbnail"
				class="work-video"
				controls
				autoplay="false"
			></video>
		</view>

		<!-- 作品信息 -->
		<view class="work-info">
			<view class="info-header">
				<text class="work-title">{{ workDetail.title }}</text>
				<view class="work-stats">
					<view class="stat-item" @click="toggleLike">
						<uni-icons 
							:type="isLiked ? 'heart-filled' : 'heart'" 
							size="20" 
							:color="isLiked ? '#ff4757' : '#666'"
						></uni-icons>
						<text class="stat-text">{{ workDetail.likes }}</text>
					</view>
					<view class="stat-item">
						<uni-icons type="eye" size="20" color="#666"></uni-icons>
						<text class="stat-text">{{ workDetail.views }}</text>
					</view>
				</view>
			</view>
			
			<view class="work-meta">
				<view class="meta-item">
					<text class="meta-label">创作时间</text>
					<text class="meta-value">{{ formatDate(workDetail.createTime) }}</text>
				</view>
				<view class="meta-item">
					<text class="meta-label">艺术风格</text>
					<text class="meta-value style-tag">{{ workDetail.style }}</text>
				</view>
				<view v-if="workDetail.resolution" class="meta-item">
					<text class="meta-label">分辨率</text>
					<text class="meta-value">{{ workDetail.resolution }}</text>
				</view>
				<view v-if="workDetail.duration" class="meta-item">
					<text class="meta-label">时长</text>
					<text class="meta-value">{{ workDetail.duration }}</text>
				</view>
			</view>
			
			<view v-if="workDetail.description" class="work-description">
				<text class="desc-title">作品描述</text>
				<text class="desc-content">{{ workDetail.description }}</text>
			</view>
		</view>

		<!-- 生成参数 -->
		<view class="generation-params">
			<uni-section title="生成参数" type="line">
				<view class="params-list">
					<view class="param-item">
						<text class="param-label">提示词</text>
						<text class="param-value">{{ workDetail.prompt || '无' }}</text>
					</view>
					<view class="param-item">
						<text class="param-label">负面提示词</text>
						<text class="param-value">{{ workDetail.negativePrompt || '无' }}</text>
					</view>
					<view class="param-item">
						<text class="param-label">生成模型</text>
						<text class="param-value">{{ workDetail.model || 'AIGC-v1.0' }}</text>
					</view>
					<view class="param-item">
						<text class="param-label">生成步数</text>
						<text class="param-value">{{ workDetail.steps || '50' }}</text>
					</view>
					<view class="param-item">
						<text class="param-label">引导强度</text>
						<text class="param-value">{{ workDetail.guidance || '7.5' }}</text>
					</view>
				</view>
			</uni-section>
		</view>

		<!-- 原始素材 -->
		<view v-if="workDetail.sourceImages && workDetail.sourceImages.length > 0" class="source-materials">
			<uni-section title="原始素材" type="line">
				<view class="source-grid">
					<view 
						v-for="(source, index) in workDetail.sourceImages" 
						:key="index"
						class="source-item"
						@click="previewImage(source.url)"
					>
						<image :src="source.url" class="source-image" mode="aspectFill"></image>
						<text class="source-label">{{ source.label }}</text>
					</view>
				</view>
			</uni-section>
		</view>

		<!-- 操作按钮 -->
		<view class="action-buttons">
			<button class="action-button secondary" @click="regenerateWork">
				<uni-icons type="refresh" size="18" color="#007AFF"></uni-icons>
				<text class="button-text">重新生成</text>
			</button>
			<button class="action-button secondary" @click="useAsReference">
				<uni-icons type="color" size="18" color="#007AFF"></uni-icons>
				<text class="button-text">用作参考</text>
			</button>
			<button class="action-button primary" @click="saveToAlbum">
				<uni-icons type="download" size="18" color="#fff"></uni-icons>
				<text class="button-text">保存到相册</text>
			</button>
		</view>
	</view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

// 页面参数
const workId = ref('')
const isLiked = ref(false)

// 作品详情数据
const workDetail = reactive({
	id: 1,
	title: '印象派风格人像',
	type: 'image',
	style: '印象派',
	originalUrl: '/static/c1.png',
	thumbnail: '/static/c1.png',
	resolution: '1024x1024',
	createTime: '2024-01-20T10:30:00',
	likes: 128,
	views: 456,
	shares: 45,
	description: '基于莫奈风格生成的人物肖像，展现了印象派独特的光影效果和色彩运用。',
	prompt: 'portrait of a young woman in impressionist style, soft brushstrokes, natural lighting, vibrant colors',
	negativePrompt: 'blurry, low quality, distorted',
	model: 'AIGC-v2.0',
	steps: '50',
	guidance: '7.5',
	variations: [
		{ url: '/static/c1.png', label: '变体1' },
		{ url: '/static/c2.png', label: '变体2' },
		{ url: '/static/c3.png', label: '变体3' }
	],
	sourceImages: [
		{ url: '/static/avatar.png', label: '人物照片' },
		{ url: '/static/c5.png', label: '展品照片' }
	]
})

// 方法
const goBack = () => {
	uni.navigateBack()
}

const formatDate = (dateStr) => {
	const date = new Date(dateStr)
	return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日 ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

const toggleLike = () => {
	isLiked.value = !isLiked.value
	if (isLiked.value) {
		workDetail.likes++
		uni.showToast({
			title: '已点赞',
			icon: 'success',
			duration: 1000
		})
	} else {
		workDetail.likes--
		uni.showToast({
			title: '已取消点赞',
			icon: 'none',
			duration: 1000
		})
	}
}

const previewImage = (imageUrl) => {
	const urls = workDetail.variations ? workDetail.variations.map(v => v.url) : [imageUrl]
	uni.previewImage({
		current: imageUrl,
		urls: urls
	})
}

const shareWork = () => {
	uni.share({
		provider: 'weixin',
		scene: 'WXSceneSession',
		type: 0,
		href: `pages/work-detail/work-detail?workId=${workDetail.id}`,
		title: workDetail.title,
		summary: workDetail.description,
		imageUrl: workDetail.thumbnail,
		success: () => {
			uni.showToast({
				title: '分享成功',
				icon: 'success'
			})
			workDetail.shares++
		},
		fail: () => {
			uni.showToast({
				title: '分享失败',
				icon: 'none'
			})
		}
	})
}

const downloadWork = () => {
	uni.showLoading({
		title: '下载中...'
	})
	
	// 模拟下载过程
	setTimeout(() => {
		uni.hideLoading()
		uni.showToast({
			title: '下载完成',
			icon: 'success'
		})
	}, 2000)
}

const regenerateWork = () => {
	uni.showModal({
		title: '重新生成',
		content: '是否使用相同参数重新生成作品？',
		success: (res) => {
			if (res.confirm) {
				uni.navigateTo({
					url: `/pages/creation/creation?regenerate=true&workId=${workDetail.id}`
				})
			}
		}
	})
}

const useAsReference = () => {
	uni.navigateTo({
		url: `/pages/creation/creation?reference=${workDetail.id}`
	})
}

const saveToAlbum = () => {
	uni.saveImageToPhotosAlbum({
		filePath: workDetail.originalUrl,
		success: () => {
			uni.showToast({
				title: '保存成功',
				icon: 'success'
			})
		},
		fail: () => {
			uni.showToast({
				title: '保存失败',
				icon: 'none'
			})
		}
	})
}

// 页面加载
onMounted(() => {
	// 获取页面参数
	const pages = getCurrentPages()
	const currentPage = pages[pages.length - 1]
	workId.value = currentPage.options.workId || '1'
	
	// 根据workId加载作品详情
	console.log('加载作品详情:', workId.value)
	
	// 这里应该调用API获取作品详情
	// loadWorkDetail(workId.value)
})
</script>

<style scoped>
.work-detail-container {
	background-color: #000;
	min-height: 100vh;
}

.nav-actions {
	display: flex;
	align-items: center;
	gap: 10px;
}

.action-btn {
	width: 36px;
	height: 36px;
	border-radius: 18px;
	background: rgba(255, 255, 255, 0.2);
	border: none;
	display: flex;
	align-items: center;
	justify-content: center;
}

.work-display {
	position: relative;
	height: 60vh;
	background: #000;
}

.work-swiper {
	width: 100%;
	height: 100%;
}

.work-image {
	width: 100%;
	height: 100%;
}

.work-image.single {
	height: 60vh;
}

.work-video {
	width: 100%;
	height: 100%;
}

.work-info {
	background: white;
	padding: 20px 15px;
	border-radius: 20px 20px 0 0;
	margin-top: -20px;
	position: relative;
	z-index: 10;
}

.info-header {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	margin-bottom: 15px;
}

.work-title {
	font-size: 20px;
	font-weight: bold;
	color: #333;
	flex: 1;
	margin-right: 15px;
}

.work-stats {
	display: flex;
	gap: 15px;
}

.stat-item {
	display: flex;
	align-items: center;
	gap: 5px;
}

.stat-text {
	font-size: 14px;
	color: #666;
}

.work-meta {
	display: flex;
	flex-direction: column;
	gap: 10px;
	margin-bottom: 20px;
}

.meta-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.meta-label {
	font-size: 14px;
	color: #666;
}

.meta-value {
	font-size: 14px;
	color: #333;
	font-weight: 500;
}

.style-tag {
	background: #e3f2fd;
	color: #007AFF;
	padding: 4px 8px;
	border-radius: 12px;
	font-size: 12px;
}

.work-description {
	padding-top: 15px;
	border-top: 1px solid #f0f0f0;
}

.desc-title {
	font-size: 16px;
	font-weight: 500;
	color: #333;
	display: block;
	margin-bottom: 8px;
}

.desc-content {
	font-size: 14px;
	color: #666;
	line-height: 1.6;
	display: block;
}

.generation-params,
.source-materials {
	background: white;
	margin: 0 15px 15px;
	border-radius: 12px;
	overflow: hidden;
}

.params-list {
	padding: 15px;
}

.param-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 10px 0;
	border-bottom: 1px solid #f5f5f5;
}

.param-item:last-child {
	border-bottom: none;
}

.param-label {
	font-size: 14px;
	color: #666;
}

.param-value {
	font-size: 14px;
	color: #333;
	font-family: monospace;
	max-width: 60%;
	text-align: right;
	word-break: break-all;
}

.source-grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 10px;
	padding: 15px;
}

.source-item {
	text-align: center;
}

.source-image {
	width: 100%;
	aspect-ratio: 1;
	border-radius: 8px;
	margin-bottom: 5px;
}

.source-label {
	font-size: 12px;
	color: #666;
	display: block;
}

.action-buttons {
	display: flex;
	gap: 10px;
	padding: 20px 15px;
	background: white;
}

.action-button {
	flex: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 5px;
	padding: 12px;
	border-radius: 8px;
	border: none;
	font-size: 14px;
}

.action-button.primary {
	background: #007AFF;
	color: white;
}

.action-button.secondary {
	background: #f8f9fa;
	color: #007AFF;
	border: 1px solid #007AFF;
}

.button-text {
	color: inherit;
}
</style>