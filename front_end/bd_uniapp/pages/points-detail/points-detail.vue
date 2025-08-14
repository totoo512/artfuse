<template>
	<view class="points-detail-container">
		<!-- 导航栏 -->
		<uni-nav-bar 
			title="积分明细" 
			:border="false" 
			background-color="#ffffff"
			color="#333333"
			left-icon="back"
			@clickLeft="goBack"
		></uni-nav-bar>

		<!-- 积分概览 -->
		<view class="points-overview">
			<view class="overview-card">
				<view class="points-info">
					<text class="current-points">{{ userPoints.current }}</text>
					<text class="points-label">当前积分</text>
				</view>
				<view class="points-stats">
					<view class="stat-item">
						<text class="stat-value">+{{ userPoints.totalEarned }}</text>
						<text class="stat-label">累计获得</text>
					</view>
					<view class="stat-item">
						<text class="stat-value">-{{ userPoints.totalSpent }}</text>
						<text class="stat-label">累计消费</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 筛选标签 -->
		<view class="filter-tabs">
			<view 
				v-for="tab in filterTabs" 
				:key="tab.key"
				class="tab-item"
				:class="{ 'active': currentFilter === tab.key }"
				@click="switchFilter(tab.key)"
			>
				<text class="tab-text">{{ tab.label }}</text>
			</view>
		</view>

		<!-- 积分记录列表 -->
		<view class="records-list">
			<view 
				v-for="record in filteredRecords" 
				:key="record.id"
				class="record-item"
			>
				<view class="record-icon">
					<uni-icons 
						:type="getRecordIcon(record.type)" 
						size="20" 
						:color="record.amount > 0 ? '#52c41a' : '#ff4d4f'"
					></uni-icons>
				</view>
				
				<view class="record-content">
					<view class="record-header">
						<text class="record-title">{{ record.title }}</text>
						<text 
							class="record-amount"
							:class="{ 'positive': record.amount > 0, 'negative': record.amount < 0 }"
						>
							{{ record.amount > 0 ? '+' : '' }}{{ record.amount }}
						</text>
					</view>
					
					<view class="record-meta">
						<text class="record-desc">{{ record.description }}</text>
						<text class="record-time">{{ formatTime(record.createTime) }}</text>
					</view>
					
					<view v-if="record.orderId" class="record-order">
						<text class="order-label">订单号：</text>
						<text class="order-id">{{ record.orderId }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 空状态 -->
		<view v-if="filteredRecords.length === 0" class="empty-state">
			<uni-icons type="wallet" size="60" color="#ccc"></uni-icons>
			<text class="empty-text">暂无积分记录</text>
			<text class="empty-desc">完成任务即可获得积分奖励</text>
		</view>

		<!-- 加载更多 -->
		<view v-if="hasMore && filteredRecords.length > 0" class="load-more">
			<button class="load-more-btn" @click="loadMore" :loading="loading">
				{{ loading ? '加载中...' : '加载更多' }}
			</button>
		</view>
	</view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'

// 筛选相关
const currentFilter = ref('all')
const loading = ref(false)
const hasMore = ref(true)

const filterTabs = [
	{ key: 'all', label: '全部' },
	{ key: 'earn', label: '获得' },
	{ key: 'spend', label: '消费' }
]

// 用户积分信息
const userPoints = reactive({
	current: 1250,
	totalEarned: 3680,
	totalSpent: 2430
})

// 积分记录数据
const records = reactive([
	{
		id: 1,
		type: 'daily_signin',
		title: '每日签到',
		description: '连续签到第7天',
		amount: 20,
		createTime: '2024-01-20T09:00:00',
		orderId: null
	},
	{
		id: 2,
		type: 'generation',
		title: 'AIGC图片生成',
		description: '生成印象派风格人像',
		amount: -50,
		createTime: '2024-01-20T10:30:00',
		orderId: 'GEN20240120001'
	},
	{
		id: 3,
		type: 'share',
		title: '分享应用',
		description: '成功邀请好友注册',
		amount: 100,
		createTime: '2024-01-19T15:20:00',
		orderId: null
	},
	{
		id: 4,
		type: 'generation',
		title: 'AIGC视频生成',
		description: '生成抽象艺术视频',
		amount: -80,
		createTime: '2024-01-19T14:15:00',
		orderId: 'GEN20240119002'
	},
	{
		id: 5,
		type: 'task',
		title: '完成新手任务',
		description: '上传首张展品照片',
		amount: 50,
		createTime: '2024-01-18T16:45:00',
		orderId: null
	},
	{
		id: 6,
		type: 'generation',
		title: 'AIGC图片生成',
		description: '生成古典主义肖像',
		amount: -50,
		createTime: '2024-01-18T11:30:00',
		orderId: 'GEN20240118003'
	},
	{
		id: 7,
		type: 'daily_signin',
		title: '每日签到',
		description: '连续签到第6天',
		amount: 15,
		createTime: '2024-01-18T09:00:00',
		orderId: null
	},
	{
		id: 8,
		type: 'recharge',
		title: '积分充值',
		description: '购买积分包（500积分）',
		amount: 500,
		createTime: '2024-01-17T20:15:00',
		orderId: 'PAY20240117001'
	},
	{
		id: 9,
		type: 'generation',
		title: 'AIGC图片生成',
		description: '生成现代艺术拼贴',
		amount: -50,
		createTime: '2024-01-17T14:45:00',
		orderId: 'GEN20240117004'
	},
	{
		id: 10,
		type: 'exhibition',
		title: 'AI看展解析',
		description: '解析展品《蒙娜丽莎》',
		amount: -20,
		createTime: '2024-01-17T13:20:00',
		orderId: 'EXH20240117001'
	}
])

// 过滤后的记录列表
const filteredRecords = computed(() => {
	if (currentFilter.value === 'earn') {
		return records.filter(record => record.amount > 0)
	} else if (currentFilter.value === 'spend') {
		return records.filter(record => record.amount < 0)
	}
	return records
})

// 方法
const goBack = () => {
	uni.navigateBack()
}

const switchFilter = (filterKey) => {
	currentFilter.value = filterKey
}

const getRecordIcon = (type) => {
	const iconMap = {
		daily_signin: 'calendar',
		generation: 'color',
		share: 'redo',
		task: 'checkmarkempty',
		recharge: 'wallet',
		exhibition: 'eye'
	}
	return iconMap[type] || 'help'
}

const formatTime = (timeStr) => {
	const date = new Date(timeStr)
	const now = new Date()
	const diff = now - date
	const minutes = Math.floor(diff / (1000 * 60))
	const hours = Math.floor(diff / (1000 * 60 * 60))
	const days = Math.floor(diff / (1000 * 60 * 60 * 24))
	
	if (minutes < 1) {
		return '刚刚'
	} else if (minutes < 60) {
		return `${minutes}分钟前`
	} else if (hours < 24) {
		return `${hours}小时前`
	} else if (days < 7) {
		return `${days}天前`
	} else {
		return `${date.getMonth() + 1}月${date.getDate()}日`
	}
}

const loadMore = async () => {
	loading.value = true
	
	// 模拟加载更多数据
	setTimeout(() => {
		// 这里应该调用API加载更多记录
		console.log('加载更多积分记录')
		loading.value = false
		hasMore.value = false // 假设没有更多数据
	}, 1000)
}

// 页面加载
onMounted(() => {
	console.log('积分明细页面加载完成')
})
</script>

<style scoped>
.points-detail-container {
	background-color: #f5f5f5;
	min-height: 100vh;
}

.points-overview {
	padding: 20px 15px;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.overview-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 16px;
	padding: 20px;
	backdrop-filter: blur(10px);
}

.points-info {
	text-align: center;
	margin-bottom: 20px;
}

.current-points {
	font-size: 36px;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 5px;
}

.points-label {
	font-size: 14px;
	color: #666;
}

.points-stats {
	display: flex;
	justify-content: space-around;
}

.stat-item {
	text-align: center;
}

.stat-value {
	font-size: 18px;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 3px;
}

.stat-label {
	font-size: 12px;
	color: #999;
}

.filter-tabs {
	background: white;
	display: flex;
	padding: 0 15px;
	border-bottom: 1px solid #f0f0f0;
}

.tab-item {
	flex: 1;
	text-align: center;
	padding: 15px 0;
	position: relative;
}

.tab-item.active::after {
	content: '';
	position: absolute;
	bottom: 0;
	left: 50%;
	transform: translateX(-50%);
	width: 30px;
	height: 3px;
	background: #007AFF;
	border-radius: 2px;
}

.tab-text {
	font-size: 15px;
	color: #666;
	transition: color 0.3s;
}

.tab-item.active .tab-text {
	color: #007AFF;
	font-weight: 500;
}

.records-list {
	padding: 15px;
}

.record-item {
	display: flex;
	align-items: flex-start;
	background: white;
	border-radius: 12px;
	padding: 15px;
	margin-bottom: 10px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.record-icon {
	width: 40px;
	height: 40px;
	border-radius: 20px;
	background: #f8f9fa;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 12px;
	flex-shrink: 0;
}

.record-content {
	flex: 1;
}

.record-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 5px;
}

.record-title {
	font-size: 16px;
	font-weight: 500;
	color: #333;
}

.record-amount {
	font-size: 16px;
	font-weight: bold;
}

.record-amount.positive {
	color: #52c41a;
}

.record-amount.negative {
	color: #ff4d4f;
}

.record-meta {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 5px;
}

.record-desc {
	font-size: 14px;
	color: #666;
}

.record-time {
	font-size: 12px;
	color: #999;
}

.record-order {
	display: flex;
	align-items: center;
	margin-top: 5px;
}

.order-label {
	font-size: 12px;
	color: #999;
}

.order-id {
	font-size: 12px;
	color: #666;
	font-family: monospace;
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

.load-more {
	padding: 20px;
	text-align: center;
}

.load-more-btn {
	padding: 12px 30px;
	background: #f8f9fa;
	color: #666;
	border: 1px solid #e0e0e0;
	border-radius: 25px;
	font-size: 14px;
}
</style>