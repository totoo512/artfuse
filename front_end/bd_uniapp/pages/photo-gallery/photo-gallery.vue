<template>
	<view class="photo-gallery-container">
		<!-- 导航栏 -->
		<uni-nav-bar 
			:title="sessionTitle" 
			:border="false" 
			background-color="#ffffff"
			color="#333333"
			left-icon="back"
			@clickLeft="goBack"
		>
			<template v-slot:right>
				<view class="nav-actions">
					<button class="action-btn" @click="selectMode = !selectMode">
						{{ selectMode ? '取消' : '选择' }}
					</button>
				</view>
			</template>
		</uni-nav-bar>

		<!-- 照片统计信息 -->
		<view class="photo-stats">
			<text class="stats-text">共 {{ photos.length }} 张照片</text>
			<text class="stats-date">{{ sessionDate }}</text>
		</view>

		<!-- 照片网格 -->
		<view class="photo-grid">
			<view 
				v-for="(photo, index) in photos" 
				:key="photo.id"
				class="photo-item"
				@click="handlePhotoClick(photo, index)"
			>
				<image 
					:src="photo.thumbnail" 
					class="photo-image" 
					mode="aspectFill"
					@load="onImageLoad"
					@error="onImageError"
				></image>
				
				<!-- 选择模式下的复选框 -->
				<view v-if="selectMode" class="select-overlay">
					<uni-icons 
						:type="selectedPhotos.includes(photo.id) ? 'checkmarkempty' : 'circle'" 
						size="24" 
						:color="selectedPhotos.includes(photo.id) ? '#007AFF' : '#fff'"
						class="select-icon"
					></uni-icons>
				</view>
				
				<!-- 照片信息 -->
				<view class="photo-info">
					<text class="photo-title">{{ photo.title }}</text>
				</view>
			</view>
		</view>

		<!-- 底部操作栏 -->
		<view v-if="selectMode && selectedPhotos.length > 0" class="bottom-actions">
			<view class="action-info">
				<text class="selected-count">已选择 {{ selectedPhotos.length }} 张</text>
			</view>
			<view class="action-buttons">
				<button class="action-button" @click="useAsStyleReference">
					<uni-icons type="color" size="18" color="#007AFF"></uni-icons>
					<text class="button-text">用作风格参考</text>
				</button>
				<button class="action-button" @click="sharePhotos">
					<uni-icons type="redo" size="18" color="#007AFF"></uni-icons>
					<text class="button-text">分享</text>
				</button>
				<button class="action-button" @click="deletePhotos">
					<uni-icons type="trash" size="18" color="#ff4757"></uni-icons>
					<text class="button-text delete">删除</text>
				</button>
			</view>
		</view>

		<!-- 空状态 -->
		<view v-if="photos.length === 0" class="empty-state">
			<uni-icons type="image" size="60" color="#ccc"></uni-icons>
			<text class="empty-text">暂无照片</text>
			<text class="empty-desc">该展览会话还没有拍摄照片</text>
		</view>
	</view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

// 页面参数
const sessionId = ref('')
const sessionTitle = ref('展览照片')
const sessionDate = ref('')

// 选择模式
const selectMode = ref(false)
const selectedPhotos = ref([])

// 照片数据
const photos = reactive([])

// 模拟照片数据
const mockPhotos = {
	1: [
		{ id: 1, thumbnail: '/static/c1.png', original: '/static/c1.png', title: '莫奈-睡莲' },
		{ id: 2, thumbnail: '/static/c2.png', original: '/static/c2.png', title: '梵高-星空' },
		{ id: 3, thumbnail: '/static/c3.png', original: '/static/c3.png', title: '雷诺阿-舞会' },
		{ id: 4, thumbnail: '/static/c4.png', original: '/static/c4.png', title: '德加-芭蕾舞者' },
		{ id: 5, thumbnail: '/static/c5.png', original: '/static/c5.png', title: '塞尚-苹果' },
		{ id: 6, thumbnail: '/static/c6.png', original: '/static/c6.png', title: '毕沙罗-乡村' },
		{ id: 7, thumbnail: '/static/c7.png', original: '/static/c7.png', title: '马奈-奥林匹亚' },
		{ id: 8, thumbnail: '/static/c8.png', original: '/static/c8.png', title: '西斯莱-风景' },
		{ id: 9, thumbnail: '/static/c9.png', original: '/static/c9.png', title: '莫奈-日出印象' },
		{ id: 10, thumbnail: '/static/c1.png', original: '/static/c1.png', title: '梵高-向日葵' },
		{ id: 11, thumbnail: '/static/c2.png', original: '/static/c2.png', title: '雷诺阿-船上的午餐' },
		{ id: 12, thumbnail: '/static/c3.png', original: '/static/c3.png', title: '德加-舞蹈课' }
	],
	2: [
		{ id: 13, thumbnail: '/static/c4.png', original: '/static/c4.png', title: '毕加索-立体主义' },
		{ id: 14, thumbnail: '/static/c5.png', original: '/static/c5.png', title: '康定斯基-抽象' },
		{ id: 15, thumbnail: '/static/c6.png', original: '/static/c6.png', title: '蒙德里安-几何' },
		{ id: 16, thumbnail: '/static/c7.png', original: '/static/c7.png', title: '达利-超现实' },
		{ id: 17, thumbnail: '/static/c8.png', original: '/static/c8.png', title: '马蒂斯-野兽派' },
		{ id: 18, thumbnail: '/static/c9.png', original: '/static/c9.png', title: '波洛克-滴画' },
		{ id: 19, thumbnail: '/static/c1.png', original: '/static/c1.png', title: '罗斯科-色域' },
		{ id: 20, thumbnail: '/static/c2.png', original: '/static/c2.png', title: '沃霍尔-波普' }
	],
	3: [
		{ id: 21, thumbnail: '/static/c3.png', original: '/static/c3.png', title: '清明上河图' },
		{ id: 22, thumbnail: '/static/c4.png', original: '/static/c4.png', title: '富春山居图' },
		{ id: 23, thumbnail: '/static/c5.png', original: '/static/c5.png', title: '千里江山图' },
		{ id: 24, thumbnail: '/static/c6.png', original: '/static/c6.png', title: '韩熙载夜宴图' },
		{ id: 25, thumbnail: '/static/c7.png', original: '/static/c7.png', title: '步辇图' },
		{ id: 26, thumbnail: '/static/c8.png', original: '/static/c8.png', title: '洛神赋图' }
	]
}

// 页面方法
const goBack = () => {
	uni.navigateBack()
}

const handlePhotoClick = (photo, index) => {
	if (selectMode.value) {
		// 选择模式下切换选中状态
		const photoIndex = selectedPhotos.value.indexOf(photo.id)
		if (photoIndex > -1) {
			selectedPhotos.value.splice(photoIndex, 1)
		} else {
			selectedPhotos.value.push(photo.id)
		}
	} else {
		// 普通模式下预览照片
		previewPhoto(photo, index)
	}
}

const previewPhoto = (photo, index) => {
	const urls = photos.map(p => p.original)
	uni.previewImage({
		current: index,
		urls: urls
	})
}

const useAsStyleReference = () => {
	if (selectedPhotos.value.length === 0) return
	
	// 将选中的照片作为风格参考，跳转到创作页面
	const selectedPhotoData = photos.filter(p => selectedPhotos.value.includes(p.id))
	uni.navigateTo({
		url: `/pages/creation/creation?stylePhotos=${encodeURIComponent(JSON.stringify(selectedPhotoData))}`
	})
}

const sharePhotos = () => {
	if (selectedPhotos.value.length === 0) return
	
	uni.showActionSheet({
		itemList: ['分享到微信', '分享到朋友圈', '复制链接'],
		success: (res) => {
			switch (res.tapIndex) {
				case 0:
					shareToWeChat()
					break
				case 1:
					shareToMoments()
					break
				case 2:
					copyShareLink()
					break
			}
		}
	})
}

const shareToWeChat = () => {
	uni.showToast({
		title: '分享到微信',
		icon: 'success'
	})
}

const shareToMoments = () => {
	uni.showToast({
		title: '分享到朋友圈',
		icon: 'success'
	})
}

const copyShareLink = () => {
	uni.setClipboardData({
		data: `https://example.com/gallery/${sessionId.value}`,
		success: () => {
			uni.showToast({
				title: '链接已复制',
				icon: 'success'
			})
		}
	})
}

const deletePhotos = () => {
	if (selectedPhotos.value.length === 0) return
	
	uni.showModal({
		title: '确认删除',
		content: `确定要删除选中的 ${selectedPhotos.value.length} 张照片吗？`,
		success: (res) => {
			if (res.confirm) {
				// 执行删除操作
				for (let i = photos.length - 1; i >= 0; i--) {
					if (selectedPhotos.value.includes(photos[i].id)) {
						photos.splice(i, 1)
					}
				}
				selectedPhotos.value = []
				selectMode.value = false
				uni.showToast({
					title: '删除成功',
					icon: 'success'
				})
			}
		}
	})
}

const onImageLoad = () => {
	// 图片加载成功
}

const onImageError = () => {
	// 图片加载失败
	console.log('图片加载失败')
}

// 页面加载时获取参数
onLoad((options) => {
	sessionId.value = options.sessionId || '1'
	sessionTitle.value = decodeURIComponent(options.title || '展览照片')
	
	// 根据sessionId加载对应的照片数据
	const sessionPhotos = mockPhotos[sessionId.value] || []
	photos.splice(0, photos.length, ...sessionPhotos)
	
	// 设置会话日期
	const dates = {
		'1': '2024年1月15日',
		'2': '2024年1月20日',
		'3': '2024年1月25日'
	}
	sessionDate.value = dates[sessionId.value] || ''
})

onMounted(() => {
	console.log('照片画廊页面加载完成')
})
</script>

<style scoped>
.photo-gallery-container {
	background-color: #f5f5f5;
	min-height: 100vh;
	padding-bottom: 80px;
}

.nav-actions {
	display: flex;
	align-items: center;
}

.action-btn {
	padding: 6px 12px;
	background: #007AFF;
	color: white;
	border: none;
	border-radius: 15px;
	font-size: 14px;
}

.photo-stats {
	padding: 15px 20px;
	background: white;
	border-bottom: 1px solid #f0f0f0;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.stats-text {
	font-size: 16px;
	color: #333;
	font-weight: 500;
}

.stats-date {
	font-size: 14px;
	color: #666;
}

.photo-grid {
	padding: 15px;
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 10px;
}

.photo-item {
	position: relative;
	aspect-ratio: 1;
	border-radius: 8px;
	overflow: hidden;
	background: white;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.photo-image {
	width: 100%;
	height: 100%;
}

.select-overlay {
	position: absolute;
	top: 8px;
	right: 8px;
	width: 30px;
	height: 30px;
	background: rgba(0, 0, 0, 0.3);
	border-radius: 15px;
	display: flex;
	align-items: center;
	justify-content: center;
}

.select-icon {
	background: rgba(255, 255, 255, 0.9);
	border-radius: 12px;
}

.photo-info {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
	padding: 20px 8px 8px;
}

.photo-title {
	color: white;
	font-size: 12px;
	line-height: 1.2;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

.bottom-actions {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: white;
	border-top: 1px solid #f0f0f0;
	padding: 15px 20px;
	box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.action-info {
	margin-bottom: 10px;
}

.selected-count {
	font-size: 14px;
	color: #666;
}

.action-buttons {
	display: flex;
	gap: 15px;
}

.action-button {
	flex: 1;
	padding: 10px;
	background: #f8f9fa;
	border: 1px solid #e9ecef;
	border-radius: 8px;
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 4px;
}

.button-text {
	font-size: 12px;
	color: #007AFF;
}

.button-text.delete {
	color: #ff4757;
}

.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 60px 20px;
	text-align: center;
}

.empty-text {
	font-size: 18px;
	color: #666;
	margin: 15px 0 5px;
}

.empty-desc {
	font-size: 14px;
	color: #999;
}
</style>