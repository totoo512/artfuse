<template>
	<view class="generated-works-container">
		<!-- 导航栏 -->
		<uni-nav-bar 
			title="生成作品" 
			:border="false" 
			background-color="#ffffff"
			color="#333333"
			left-icon="back"
			@clickLeft="goBack"
		>
			<template v-slot:right>
				<view class="nav-actions">
					<button class="filter-btn" @click="showFilterPopup">
						<uni-icons type="funnel" size="16" color="#007AFF"></uni-icons>
					</button>
				</view>
			</template>
		</uni-nav-bar>

		<!-- 筛选标签 -->
		<view class="filter-tabs">
			<scroll-view scroll-x="true" class="tabs-scroll">
				<view class="tabs-container">
					<view 
						v-for="tab in filterTabs" 
						:key="tab.key"
						class="tab-item"
						:class="{ 'active': currentFilter === tab.key }"
						@click="switchFilter(tab.key)"
					>
						<text class="tab-text">{{ tab.label }}</text>
						<uni-badge v-if="tab.count" :text="tab.count" type="primary" size="small"></uni-badge>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 作品网格 -->
		<view class="works-grid">
			<view 
				v-for="work in filteredWorks" 
				:key="work.id"
				class="work-item"
				@click="viewWork(work)"
			>
				<view class="work-media">
					<image 
						v-if="work.type === 'image'"
						:src="work.thumbnail" 
						class="work-image" 
						mode="aspectFill"
					></image>
					<video 
						v-else-if="work.type === 'video'"
						:src="work.videoUrl"
						:poster="work.thumbnail"
						class="work-video"
						controls="false"
						muted
					></video>
					
					<!-- 媒体类型标识 -->
					<view class="media-type-badge">
						<uni-icons 
							:type="work.type === 'video' ? 'videocam' : 'image'" 
							size="14" 
							color="#fff"
						></uni-icons>
					</view>
					
					<!-- 视频时长 -->
					<view v-if="work.type === 'video'" class="video-duration">
						<text class="duration-text">{{ work.duration }}</text>
					</view>
				</view>
				
				<view class="work-info">
					<text class="work-title">{{ work.title }}</text>
					<view class="work-meta">
						<text class="work-style">{{ work.style }}</text>
						<text class="work-date">{{ formatDate(work.createTime) }}</text>
					</view>
					<view class="work-stats">
						<view class="stat-item">
							<uni-icons type="heart" size="12" color="#ff4757"></uni-icons>
							<text class="stat-text">{{ work.likes }}</text>
						</view>
						<view class="stat-item">
							<uni-icons type="redo" size="12" color="#007AFF"></uni-icons>
							<text class="stat-text">{{ work.shares }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 空状态 -->
		<view v-if="filteredWorks.length === 0" class="empty-state">
			<uni-icons type="color" size="60" color="#ccc"></uni-icons>
			<text class="empty-text">暂无生成作品</text>
			<text class="empty-desc">去创作页面生成你的第一个作品吧</text>
			<button class="create-btn" @click="goToCreation">
				开始创作
			</button>
		</view>

		<!-- 筛选弹窗 -->
		<uni-popup ref="filterPopup" type="bottom">
			<view class="filter-popup">
				<view class="popup-header">
					<text class="popup-title">筛选作品</text>
					<button class="close-btn" @click="closeFilterPopup">
						<uni-icons type="close" size="20" color="#666"></uni-icons>
					</button>
				</view>
				
				<view class="filter-section">
					<text class="section-title">作品类型</text>
					<view class="filter-options">
						<view 
							v-for="type in typeOptions" 
							:key="type.key"
							class="option-item"
							:class="{ 'selected': filterOptions.type === type.key }"
							@click="filterOptions.type = type.key"
						>
							<text class="option-text">{{ type.label }}</text>
						</view>
					</view>
				</view>
				
				<view class="filter-section">
					<text class="section-title">艺术风格</text>
					<view class="filter-options">
						<view 
							v-for="style in styleOptions" 
							:key="style.key"
							class="option-item"
							:class="{ 'selected': filterOptions.style === style.key }"
							@click="filterOptions.style = style.key"
						>
							<text class="option-text">{{ style.label }}</text>
						</view>
					</view>
				</view>
				
				<view class="popup-actions">
					<button class="reset-btn" @click="resetFilter">重置</button>
					<button class="confirm-btn" @click="applyFilter">确定</button>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'

// 筛选相关
const currentFilter = ref('all')
const filterPopup = ref(null)

const filterTabs = [
	{ key: 'all', label: '全部', count: 24 },
	{ key: 'image', label: '图片', count: 18 },
	{ key: 'video', label: '视频', count: 6 },
	{ key: 'recent', label: '最近', count: 8 }
]

const filterOptions = reactive({
	type: 'all',
	style: 'all'
})

const typeOptions = [
	{ key: 'all', label: '全部类型' },
	{ key: 'image', label: '图片' },
	{ key: 'video', label: '视频' }
]

const styleOptions = [
	{ key: 'all', label: '全部风格' },
	{ key: 'impressionism', label: '印象派' },
	{ key: 'abstract', label: '抽象派' },
	{ key: 'classical', label: '古典主义' },
	{ key: 'modern', label: '现代艺术' },
	{ key: 'chinese', label: '中国风' }
]

// 作品数据
const works = reactive([
	{
		id: 1,
		title: '印象派风格人像',
		type: 'image',
		style: '印象派',
		thumbnail: '/static/c1.png',
		originalUrl: '/static/c1.png',
		createTime: '2024-01-20T10:30:00',
		likes: 128,
		shares: 45,
		description: '基于莫奈风格生成的人物肖像'
	},
	{
		id: 2,
		title: '抽象艺术视频',
		type: 'video',
		style: '抽象派',
		thumbnail: '/static/c2.png',
		videoUrl: '/static/sample-video.mp4',
		duration: '00:15',
		createTime: '2024-01-19T15:20:00',
		likes: 89,
		shares: 23,
		description: '康定斯基风格的动态艺术视频'
	},
	{
		id: 3,
		title: '古典主义肖像',
		type: 'image',
		style: '古典主义',
		thumbnail: '/static/c3.png',
		originalUrl: '/static/c3.png',
		createTime: '2024-01-18T09:15:00',
		likes: 156,
		shares: 67,
		description: '文艺复兴时期风格的人物画像'
	},
	{
		id: 4,
		title: '现代艺术拼贴',
		type: 'image',
		style: '现代艺术',
		thumbnail: '/static/c4.png',
		originalUrl: '/static/c4.png',
		createTime: '2024-01-17T14:45:00',
		likes: 92,
		shares: 34,
		description: '毕加索立体主义风格创作'
	},
	{
		id: 5,
		title: '中国风水墨画',
		type: 'image',
		style: '中国风',
		thumbnail: '/static/c5.png',
		originalUrl: '/static/c5.png',
		createTime: '2024-01-16T11:30:00',
		likes: 203,
		shares: 89,
		description: '传统水墨画风格人物肖像'
	},
	{
		id: 6,
		title: '梦幻风格视频',
		type: 'video',
		style: '印象派',
		thumbnail: '/static/c6.png',
		videoUrl: '/static/sample-video2.mp4',
		duration: '00:20',
		createTime: '2024-01-15T16:00:00',
		likes: 145,
		shares: 56,
		description: '梦幻色彩的动态艺术作品'
	}
])

// 过滤后的作品列表
const filteredWorks = computed(() => {
	let filtered = works
	
	// 按标签筛选
	if (currentFilter.value === 'image') {
		filtered = filtered.filter(work => work.type === 'image')
	} else if (currentFilter.value === 'video') {
		filtered = filtered.filter(work => work.type === 'video')
	} else if (currentFilter.value === 'recent') {
		const recentDate = new Date()
		recentDate.setDate(recentDate.getDate() - 7)
		filtered = filtered.filter(work => new Date(work.createTime) > recentDate)
	}
	
	// 按高级筛选
	if (filterOptions.type !== 'all') {
		filtered = filtered.filter(work => work.type === filterOptions.type)
	}
	
	if (filterOptions.style !== 'all') {
		filtered = filtered.filter(work => work.style === filterOptions.style)
	}
	
	return filtered
})

// 方法
const goBack = () => {
	uni.navigateBack()
}

const switchFilter = (filterKey) => {
	currentFilter.value = filterKey
}

const showFilterPopup = () => {
	filterPopup.value.open()
}

const closeFilterPopup = () => {
	filterPopup.value.close()
}

const resetFilter = () => {
	filterOptions.type = 'all'
	filterOptions.style = 'all'
}

const applyFilter = () => {
	closeFilterPopup()
	// 应用筛选后可以更新当前筛选标签
	if (filterOptions.type !== 'all' || filterOptions.style !== 'all') {
		currentFilter.value = 'custom'
	}
}

const formatDate = (dateStr) => {
	const date = new Date(dateStr)
	const now = new Date()
	const diff = now - date
	const days = Math.floor(diff / (1000 * 60 * 60 * 24))
	
	if (days === 0) {
		return '今天'
	} else if (days === 1) {
		return '昨天'
	} else if (days < 7) {
		return `${days}天前`
	} else {
		return `${date.getMonth() + 1}月${date.getDate()}日`
	}
}

const viewWork = (work) => {
	uni.navigateTo({
		url: `/pages/work-detail/work-detail?workId=${work.id}`
	})
}

const goToCreation = () => {
	uni.switchTab({
		url: '/pages/creation/creation'
	})
}

// 页面加载
onMounted(() => {
	console.log('生成作品页面加载完成')
})
</script>

<style scoped>
.generated-works-container {
	background-color: #f5f5f5;
	min-height: 100vh;
}

.nav-actions {
	display: flex;
	align-items: center;
}

.filter-btn {
	width: 32px;
	height: 32px;
	border-radius: 16px;
	background: #f8f9fa;
	border: none;
	display: flex;
	align-items: center;
	justify-content: center;
}

.filter-tabs {
	background: white;
	border-bottom: 1px solid #f0f0f0;
}

.tabs-scroll {
	white-space: nowrap;
}

.tabs-container {
	display: flex;
	padding: 0 15px;
}

.tab-item {
	display: flex;
	align-items: center;
	gap: 5px;
	padding: 12px 16px;
	margin-right: 10px;
	border-radius: 20px;
	background: #f8f9fa;
	transition: all 0.3s;
}

.tab-item.active {
	background: #007AFF;
	color: white;
}

.tab-text {
	font-size: 14px;
	color: inherit;
}

.works-grid {
	padding: 15px;
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 15px;
}

.work-item {
	background: white;
	border-radius: 12px;
	overflow: hidden;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.work-media {
	position: relative;
	aspect-ratio: 1;
}

.work-image,
.work-video {
	width: 100%;
	height: 100%;
}

.media-type-badge {
	position: absolute;
	top: 8px;
	left: 8px;
	width: 24px;
	height: 24px;
	border-radius: 12px;
	background: rgba(0, 0, 0, 0.6);
	display: flex;
	align-items: center;
	justify-content: center;
}

.video-duration {
	position: absolute;
	bottom: 8px;
	right: 8px;
	padding: 2px 6px;
	background: rgba(0, 0, 0, 0.7);
	border-radius: 4px;
}

.duration-text {
	color: white;
	font-size: 10px;
}

.work-info {
	padding: 12px;
}

.work-title {
	font-size: 14px;
	font-weight: 500;
	color: #333;
	display: block;
	margin-bottom: 6px;
	line-height: 1.3;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

.work-meta {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8px;
}

.work-style {
	font-size: 12px;
	color: #007AFF;
	background: #e3f2fd;
	padding: 2px 6px;
	border-radius: 10px;
}

.work-date {
	font-size: 12px;
	color: #999;
}

.work-stats {
	display: flex;
	gap: 12px;
}

.stat-item {
	display: flex;
	align-items: center;
	gap: 3px;
}

.stat-text {
	font-size: 11px;
	color: #666;
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
	margin-bottom: 30px;
}

.create-btn {
	padding: 12px 30px;
	background: #007AFF;
	color: white;
	border: none;
	border-radius: 25px;
	font-size: 16px;
}

/* 筛选弹窗样式 */
.filter-popup {
	background: white;
	border-radius: 20px 20px 0 0;
	padding: 20px;
	max-height: 60vh;
}

.popup-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
	padding-bottom: 15px;
	border-bottom: 1px solid #f0f0f0;
}

.popup-title {
	font-size: 18px;
	font-weight: bold;
	color: #333;
}

.close-btn {
	width: 32px;
	height: 32px;
	border-radius: 16px;
	background: #f8f9fa;
	border: none;
	display: flex;
	align-items: center;
	justify-content: center;
}

.filter-section {
	margin-bottom: 25px;
}

.section-title {
	font-size: 16px;
	font-weight: 500;
	color: #333;
	margin-bottom: 12px;
	display: block;
}

.filter-options {
	display: flex;
	flex-wrap: wrap;
	gap: 10px;
}

.option-item {
	padding: 8px 16px;
	border: 1px solid #e0e0e0;
	border-radius: 20px;
	background: #f8f9fa;
	transition: all 0.3s;
}

.option-item.selected {
	background: #007AFF;
	border-color: #007AFF;
}

.option-text {
	font-size: 14px;
	color: #666;
}

.option-item.selected .option-text {
	color: white;
}

.popup-actions {
	display: flex;
	gap: 15px;
	padding-top: 20px;
	border-top: 1px solid #f0f0f0;
}

.reset-btn,
.confirm-btn {
	flex: 1;
	padding: 12px;
	border-radius: 8px;
	font-size: 16px;
	border: none;
}

.reset-btn {
	background: #f8f9fa;
	color: #666;
}

.confirm-btn {
	background: #007AFF;
	color: white;
}
</style>