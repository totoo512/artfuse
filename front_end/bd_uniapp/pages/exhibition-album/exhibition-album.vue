<template>
	<view class="exhibition-album-container">
		<!-- 导航栏 -->
		<uni-nav-bar 
			title="看展相册" 
			:border="false" 
			background-color="#ffffff"
			color="#333333"
			left-icon="back"
			@clickLeft="goBack"
		/>

		<!-- 搜索栏 -->
		<view class="search-section">
			<uni-search-bar 
				v-model="searchKeyword"
				placeholder="搜索展览或展品"
				@confirm="searchExhibitions"
				@input="onSearchInput"
				cancelButton="none"
			></uni-search-bar>
		</view>

		<!-- 展览会话列表 -->
		<view class="exhibition-sessions">
			<view 
				v-for="session in filteredSessions" 
				:key="session.id"
				class="session-card"
				@click="viewSessionPhotos(session)"
			>
				<view class="session-header">
					<view class="session-info">
						<text class="session-title">{{ session.title }}</text>
						<text class="session-date">{{ formatDate(session.date) }}</text>
						<text class="session-location">{{ session.location }}</text>
					</view>
					<view class="session-stats">
						<uni-badge :text="session.photoCount" type="primary"></uni-badge>
					</view>
				</view>
				
				<!-- 照片预览网格 -->
				<view class="photo-grid">
					<view 
						v-for="(photo, index) in session.photos.slice(0, 6)" 
						:key="index"
						class="photo-item"
						:class="{ 'more-indicator': index === 5 && session.photos.length > 6 }"
					>
						<image 
							:src="photo.thumbnail" 
							class="photo-thumbnail" 
							mode="aspectFill"
						></image>
						<view v-if="index === 5 && session.photos.length > 6" class="more-overlay">
							<text class="more-text">+{{ session.photos.length - 5 }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 空状态 -->
		<view v-if="filteredSessions.length === 0" class="empty-state">
			<uni-icons type="image" size="60" color="#ccc"></uni-icons>
			<text class="empty-text">暂无看展相册</text>
			<text class="empty-desc">去看展解析页面拍摄展品照片吧</text>
			<button class="go-exhibition-btn" @click="goToExhibition">
				去看展
			</button>
		</view>
	</view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'

// 搜索关键词
const searchKeyword = ref('')

// 展览会话数据
const exhibitionSessions = reactive([
	{
		id: 1,
		title: '印象派大师展',
		date: '2024-01-15',
		location: '中国美术馆',
		photoCount: 12,
		photos: [
			{ id: 1, thumbnail: '/static/c1.png', original: '/static/c1.png', title: '莫奈-睡莲' },
			{ id: 2, thumbnail: '/static/c2.png', original: '/static/c2.png', title: '梵高-星空' },
			{ id: 3, thumbnail: '/static/c3.png', original: '/static/c3.png', title: '雷诺阿-舞会' },
			{ id: 4, thumbnail: '/static/c4.png', original: '/static/c4.png', title: '德加-芭蕾舞者' },
			{ id: 5, thumbnail: '/static/c5.png', original: '/static/c5.png', title: '塞尚-苹果' },
			{ id: 6, thumbnail: '/static/c6.png', original: '/static/c6.png', title: '毕沙罗-乡村' },
			{ id: 7, thumbnail: '/static/c7.png', original: '/static/c7.png', title: '马奈-奥林匹亚' },
			{ id: 8, thumbnail: '/static/c8.png', original: '/static/c8.png', title: '西斯莱-风景' }
		]
	},
	{
		id: 2,
		title: '现代艺术回顾展',
		date: '2024-01-20',
		location: '上海当代艺术博物馆',
		photoCount: 8,
		photos: [
			{ id: 9, thumbnail: '/static/c9.png', original: '/static/c9.png', title: '毕加索-立体主义' },
			{ id: 10, thumbnail: '/static/c1.png', original: '/static/c1.png', title: '康定斯基-抽象' },
			{ id: 11, thumbnail: '/static/c2.png', original: '/static/c2.png', title: '蒙德里安-几何' },
			{ id: 12, thumbnail: '/static/c3.png', original: '/static/c3.png', title: '达利-超现实' }
		]
	},
	{
		id: 3,
		title: '中国古代书画展',
		date: '2024-01-25',
		location: '故宫博物院',
		photoCount: 15,
		photos: [
			{ id: 13, thumbnail: '/static/c4.png', original: '/static/c4.png', title: '清明上河图' },
			{ id: 14, thumbnail: '/static/c5.png', original: '/static/c5.png', title: '富春山居图' },
			{ id: 15, thumbnail: '/static/c6.png', original: '/static/c6.png', title: '千里江山图' },
			{ id: 16, thumbnail: '/static/c7.png', original: '/static/c7.png', title: '韩熙载夜宴图' },
			{ id: 17, thumbnail: '/static/c8.png', original: '/static/c8.png', title: '步辇图' },
			{ id: 18, thumbnail: '/static/c9.png', original: '/static/c9.png', title: '洛神赋图' }
		]
	}
])

// 过滤后的会话列表
const filteredSessions = computed(() => {
	if (!searchKeyword.value) {
		return exhibitionSessions
	}
	return exhibitionSessions.filter(session => 
		session.title.includes(searchKeyword.value) ||
		session.location.includes(searchKeyword.value)
	)
})

// 方法
const goBack = () => {
	uni.navigateBack()
}

const searchExhibitions = () => {
	console.log('搜索展览:', searchKeyword.value)
}

const onSearchInput = (value) => {
	searchKeyword.value = value
}

const formatDate = (dateStr) => {
	const date = new Date(dateStr)
	return `${date.getMonth() + 1}月${date.getDate()}日`
}

const viewSessionPhotos = (session) => {
	uni.navigateTo({
		url: `/pages/photo-gallery/photo-gallery?sessionId=${session.id}&title=${encodeURIComponent(session.title)}`
	})
}

const goToExhibition = () => {
	uni.switchTab({
		url: '/pages/exhibition/exhibition'
	})
}

// 页面加载
onMounted(() => {
	console.log('看展相册页面加载完成')
})
</script>

<style scoped>
.exhibition-album-container {
	background-color: #f5f5f5;
	min-height: 100vh;
}

.search-section {
	padding: 10px 15px;
	background: white;
	border-bottom: 1px solid #f0f0f0;
}

.exhibition-sessions {
	padding: 15px;
}

.session-card {
	background: white;
	border-radius: 12px;
	padding: 15px;
	margin-bottom: 15px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.session-header {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	margin-bottom: 15px;
}

.session-info {
	flex: 1;
}

.session-title {
	font-size: 18px;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 5px;
}

.session-date {
	font-size: 14px;
	color: #666;
	display: block;
	margin-bottom: 3px;
}

.session-location {
	font-size: 14px;
	color: #999;
	display: block;
}

.session-stats {
	margin-left: 10px;
}

.photo-grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 8px;
}

.photo-item {
	position: relative;
	aspect-ratio: 1;
	border-radius: 8px;
	overflow: hidden;
}

.photo-thumbnail {
	width: 100%;
	height: 100%;
}

.more-overlay {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.6);
	display: flex;
	align-items: center;
	justify-content: center;
}

.more-text {
	color: white;
	font-size: 16px;
	font-weight: bold;
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

.go-exhibition-btn {
	padding: 12px 30px;
	background: #007AFF;
	color: white;
	border: none;
	border-radius: 25px;
	font-size: 16px;
}
</style>