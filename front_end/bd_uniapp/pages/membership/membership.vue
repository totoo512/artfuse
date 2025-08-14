<template>
	<view class="membership-container">
		<!-- 导航栏 -->
		<uni-nav-bar 
			title="会员中心" 
			:border="false" 
			background-color="transparent"
			color="#ffffff"
			left-icon="back"
			@clickLeft="goBack"
		></uni-nav-bar>

		<!-- 会员状态卡片 -->
		<view class="membership-header">
			<view class="status-card">
				<view class="member-info">
					<view class="member-avatar">
						<image src="/static/avatar.png" class="avatar-img"></image>
						<view v-if="membershipInfo.isVip" class="vip-badge">
							<uni-icons type="vip-filled" size="16" color="#FFD700"></uni-icons>
						</view>
					</view>
					
					<view class="member-details">
						<text class="member-name">{{ membershipInfo.nickname }}</text>
						<view class="member-status">
							<text 
								class="status-text"
								:class="{ 'vip': membershipInfo.isVip }"
							>
								{{ membershipInfo.isVip ? 'VIP会员' : '普通用户' }}
							</text>
							<text v-if="membershipInfo.isVip" class="expire-date">
								到期时间：{{ formatDate(membershipInfo.expireDate) }}
							</text>
						</view>
					</view>
				</view>
				
				<view v-if="!membershipInfo.isVip" class="upgrade-prompt">
					<text class="prompt-text">开通VIP，享受专属特权</text>
					<button class="upgrade-btn" @click="showPricingPlans">
						立即开通
					</button>
				</view>
			</view>
		</view>

		<!-- 会员权益 -->
		<view class="benefits-section">
			<uni-section title="会员权益" type="line">
				<view class="benefits-grid">
					<view 
						v-for="benefit in memberBenefits" 
						:key="benefit.id"
						class="benefit-item"
						:class="{ 'available': membershipInfo.isVip || benefit.freeUser }"
					>
						<view class="benefit-icon">
							<uni-icons 
								:type="benefit.icon" 
								size="24" 
								:color="(membershipInfo.isVip || benefit.freeUser) ? '#007AFF' : '#ccc'"
							></uni-icons>
						</view>
						<text class="benefit-title">{{ benefit.title }}</text>
						<text class="benefit-desc">{{ benefit.description }}</text>
						<view v-if="!benefit.freeUser && !membershipInfo.isVip" class="vip-only">
							<uni-icons type="vip-filled" size="12" color="#FFD700"></uni-icons>
							<text class="vip-text">VIP专享</text>
						</view>
					</view>
				</view>
			</uni-section>
		</view>

		<!-- 使用统计 -->
		<view class="usage-section">
			<uni-section title="本月使用情况" type="line">
				<view class="usage-stats">
					<view class="usage-item">
						<view class="usage-info">
							<text class="usage-label">图片生成</text>
							<text class="usage-count">{{ usageStats.imageGeneration }}/{{ membershipInfo.isVip ? '无限' : '10' }}</text>
						</view>
						<uni-progress 
							:percent="getUsagePercent('imageGeneration')" 
							stroke-width="6" 
							active-color="#007AFF"
							background-color="#f0f0f0"
						></uni-progress>
					</view>
					
					<view class="usage-item">
						<view class="usage-info">
							<text class="usage-label">视频生成</text>
							<text class="usage-count">{{ usageStats.videoGeneration }}/{{ membershipInfo.isVip ? '无限' : '3' }}</text>
						</view>
						<uni-progress 
							:percent="getUsagePercent('videoGeneration')" 
							stroke-width="6" 
							active-color="#52c41a"
							background-color="#f0f0f0"
						></uni-progress>
					</view>
					
					<view class="usage-item">
						<view class="usage-info">
							<text class="usage-label">AI解析</text>
							<text class="usage-count">{{ usageStats.aiAnalysis }}/{{ membershipInfo.isVip ? '无限' : '20' }}</text>
						</view>
						<uni-progress 
							:percent="getUsagePercent('aiAnalysis')" 
							stroke-width="6" 
							active-color="#ff7875"
							background-color="#f0f0f0"
						></uni-progress>
					</view>
				</view>
			</uni-section>
		</view>

		<!-- 套餐选择 -->
		<view v-if="!membershipInfo.isVip" class="pricing-section">
			<uni-section title="选择套餐" type="line">
				<view class="pricing-plans">
					<view 
						v-for="plan in pricingPlans" 
						:key="plan.id"
						class="plan-item"
						:class="{ 'recommended': plan.recommended, 'selected': selectedPlan === plan.id }"
						@click="selectPlan(plan.id)"
					>
						<view v-if="plan.recommended" class="recommend-badge">
							<text class="recommend-text">推荐</text>
						</view>
						
						<view class="plan-header">
							<text class="plan-name">{{ plan.name }}</text>
							<view class="plan-price">
								<text class="price-symbol">¥</text>
								<text class="price-amount">{{ plan.price }}</text>
								<text class="price-unit">/{{ plan.unit }}</text>
							</view>
							<text v-if="plan.originalPrice" class="original-price">原价¥{{ plan.originalPrice }}</text>
						</view>
						
						<view class="plan-features">
							<view 
								v-for="feature in plan.features" 
								:key="feature"
								class="feature-item"
							>
								<uni-icons type="checkmarkempty" size="14" color="#52c41a"></uni-icons>
								<text class="feature-text">{{ feature }}</text>
							</view>
						</view>
					</view>
				</view>
				
				<view class="purchase-actions">
					<button 
						class="purchase-btn" 
						:disabled="!selectedPlan"
						@click="purchaseMembership"
					>
						立即购买
					</button>
					<text class="purchase-note">购买即表示同意《会员服务协议》</text>
				</view>
			</uni-section>
		</view>

		<!-- VIP用户续费 -->
		<view v-else class="renewal-section">
			<uni-section title="续费服务" type="line">
				<view class="renewal-info">
					<text class="renewal-text">您的VIP会员将于 {{ formatDate(membershipInfo.expireDate) }} 到期</text>
					<button class="renewal-btn" @click="renewMembership">
						续费会员
					</button>
				</view>
			</uni-section>
		</view>
	</view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'

// 会员信息
const membershipInfo = reactive({
	nickname: '艺术爱好者',
	isVip: false, // 改为true可以测试VIP状态
	expireDate: '2024-12-31T23:59:59'
})

// 使用统计
const usageStats = reactive({
	imageGeneration: 8,
	videoGeneration: 2,
	aiAnalysis: 15
})

// 选中的套餐
const selectedPlan = ref(null)

// 会员权益
const memberBenefits = [
	{
		id: 1,
		title: '无限生成',
		description: '图片和视频无限次生成',
		icon: 'loop',
		freeUser: false
	},
	{
		id: 2,
		title: '优先处理',
		description: '生成队列优先处理',
		icon: 'forward',
		freeUser: false
	},
	{
		id: 3,
		title: '专属风格',
		description: '50+专属艺术风格模板',
		icon: 'color',
		freeUser: false
	},
	{
		id: 4,
		title: '高清导出',
		description: '支持4K高清图片导出',
		icon: 'image',
		freeUser: false
	},
	{
		id: 5,
		title: '无水印',
		description: '生成作品无水印',
		icon: 'eye-slash',
		freeUser: false
	},
	{
		id: 6,
		title: '基础功能',
		description: '每日免费生成次数',
		icon: 'gift',
		freeUser: true
	},
	{
		id: 7,
		title: 'AI解析',
		description: '展品智能解析服务',
		icon: 'eye',
		freeUser: true
	},
	{
		id: 8,
		title: '作品保存',
		description: '云端作品永久保存',
		icon: 'cloud-upload',
		freeUser: true
	}
]

// 套餐计划
const pricingPlans = [
	{
		id: 1,
		name: '月度会员',
		price: 29,
		originalPrice: null,
		unit: '月',
		recommended: false,
		features: [
			'无限图片生成',
			'无限视频生成',
			'优先处理队列',
			'专属风格模板',
			'4K高清导出'
		]
	},
	{
		id: 2,
		name: '季度会员',
		price: 69,
		originalPrice: 87,
		unit: '季',
		recommended: true,
		features: [
			'包含月度会员所有权益',
			'额外赠送100积分',
			'专属客服支持',
			'新功能抢先体验'
		]
	},
	{
		id: 3,
		name: '年度会员',
		price: 199,
		originalPrice: 348,
		unit: '年',
		recommended: false,
		features: [
			'包含季度会员所有权益',
			'额外赠送500积分',
			'专属定制风格',
			'线下活动优先邀请'
		]
	}
]

// 计算使用百分比
const getUsagePercent = (type) => {
	if (membershipInfo.isVip) {
		return 0 // VIP用户显示为0%（无限制）
	}
	
	const limits = {
		imageGeneration: 10,
		videoGeneration: 3,
		aiAnalysis: 20
	}
	
	const used = usageStats[type]
	const limit = limits[type]
	return Math.min((used / limit) * 100, 100)
}

// 方法
const goBack = () => {
	uni.navigateBack()
}

const formatDate = (dateStr) => {
	const date = new Date(dateStr)
	return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

const showPricingPlans = () => {
	// 滚动到套餐选择区域
	uni.pageScrollTo({
		selector: '.pricing-section',
		duration: 300
	})
}

const selectPlan = (planId) => {
	selectedPlan.value = planId
}

const purchaseMembership = () => {
	if (!selectedPlan.value) {
		uni.showToast({
			title: '请选择套餐',
			icon: 'none'
		})
		return
	}
	
	const plan = pricingPlans.find(p => p.id === selectedPlan.value)
	uni.showModal({
		title: '确认购买',
		content: `确认购买${plan.name}（¥${plan.price}）？`,
		success: (res) => {
			if (res.confirm) {
				// 这里应该调用支付接口
				uni.showToast({
					title: '支付成功',
					icon: 'success'
				})
				
				// 更新会员状态
				setTimeout(() => {
					membershipInfo.isVip = true
					const expireDate = new Date()
					if (plan.unit === '月') {
						expireDate.setMonth(expireDate.getMonth() + 1)
					} else if (plan.unit === '季') {
						expireDate.setMonth(expireDate.getMonth() + 3)
					} else if (plan.unit === '年') {
						expireDate.setFullYear(expireDate.getFullYear() + 1)
					}
					membershipInfo.expireDate = expireDate.toISOString()
				}, 1000)
			}
		}
	})
}

const renewMembership = () => {
	uni.showModal({
		title: '续费会员',
		content: '是否续费一年会员（¥199）？',
		success: (res) => {
			if (res.confirm) {
				// 这里应该调用续费接口
				uni.showToast({
					title: '续费成功',
					icon: 'success'
				})
				
				// 更新到期时间
				setTimeout(() => {
					const expireDate = new Date(membershipInfo.expireDate)
					expireDate.setFullYear(expireDate.getFullYear() + 1)
					membershipInfo.expireDate = expireDate.toISOString()
				}, 1000)
			}
		}
	})
}

// 页面加载
onMounted(() => {
	console.log('会员中心页面加载完成')
})
</script>

<style scoped>
.membership-container {
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	min-height: 100vh;
}

.membership-header {
	padding: 20px 15px 30px;
}

.status-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 20px;
	padding: 25px;
	backdrop-filter: blur(10px);
	box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.member-info {
	display: flex;
	align-items: center;
	margin-bottom: 20px;
}

.member-avatar {
	position: relative;
	margin-right: 15px;
}

.avatar-img {
	width: 60px;
	height: 60px;
	border-radius: 30px;
}

.vip-badge {
	position: absolute;
	bottom: -2px;
	right: -2px;
	width: 20px;
	height: 20px;
	border-radius: 10px;
	background: #333;
	display: flex;
	align-items: center;
	justify-content: center;
}

.member-details {
	flex: 1;
}

.member-name {
	font-size: 20px;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 5px;
}

.member-status {
	display: flex;
	flex-direction: column;
	gap: 3px;
}

.status-text {
	font-size: 14px;
	color: #666;
}

.status-text.vip {
	color: #FFD700;
	font-weight: 500;
}

.expire-date {
	font-size: 12px;
	color: #999;
}

.upgrade-prompt {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding-top: 15px;
	border-top: 1px solid #f0f0f0;
}

.prompt-text {
	font-size: 14px;
	color: #666;
}

.upgrade-btn {
	padding: 8px 20px;
	background: linear-gradient(45deg, #667eea, #764ba2);
	color: white;
	border: none;
	border-radius: 20px;
	font-size: 14px;
}

.benefits-section,
.usage-section,
.pricing-section,
.renewal-section {
	background: white;
	margin: 0 15px 15px;
	border-radius: 16px;
	overflow: hidden;
}

.benefits-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 15px;
	padding: 15px;
}

.benefit-item {
	text-align: center;
	padding: 20px 10px;
	border-radius: 12px;
	background: #f8f9fa;
	position: relative;
	transition: all 0.3s;
}

.benefit-item.available {
	background: #e3f2fd;
	border: 1px solid #007AFF;
}

.benefit-icon {
	margin-bottom: 10px;
}

.benefit-title {
	font-size: 14px;
	font-weight: 500;
	color: #333;
	display: block;
	margin-bottom: 5px;
}

.benefit-desc {
	font-size: 12px;
	color: #666;
	display: block;
	line-height: 1.4;
}

.vip-only {
	position: absolute;
	top: 5px;
	right: 5px;
	display: flex;
	align-items: center;
	gap: 2px;
	padding: 2px 6px;
	background: rgba(255, 215, 0, 0.2);
	border-radius: 8px;
}

.vip-text {
	font-size: 10px;
	color: #FFD700;
}

.usage-stats {
	padding: 15px;
}

.usage-item {
	margin-bottom: 20px;
}

.usage-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8px;
}

.usage-label {
	font-size: 14px;
	color: #333;
}

.usage-count {
	font-size: 14px;
	color: #666;
	font-weight: 500;
}

.pricing-plans {
	padding: 15px;
}

.plan-item {
	position: relative;
	border: 2px solid #f0f0f0;
	border-radius: 16px;
	padding: 20px;
	margin-bottom: 15px;
	transition: all 0.3s;
}

.plan-item.recommended {
	border-color: #007AFF;
	box-shadow: 0 4px 20px rgba(0, 122, 255, 0.2);
}

.plan-item.selected {
	border-color: #007AFF;
	background: #f0f8ff;
}

.recommend-badge {
	position: absolute;
	top: -10px;
	left: 20px;
	padding: 4px 12px;
	background: #007AFF;
	border-radius: 10px;
}

.recommend-text {
	font-size: 12px;
	color: white;
}

.plan-header {
	text-align: center;
	margin-bottom: 20px;
}

.plan-name {
	font-size: 18px;
	font-weight: bold;
	color: #333;
	display: block;
	margin-bottom: 10px;
}

.plan-price {
	display: flex;
	align-items: baseline;
	justify-content: center;
	gap: 2px;
	margin-bottom: 5px;
}

.price-symbol {
	font-size: 16px;
	color: #007AFF;
}

.price-amount {
	font-size: 32px;
	font-weight: bold;
	color: #007AFF;
}

.price-unit {
	font-size: 14px;
	color: #666;
}

.original-price {
	font-size: 14px;
	color: #999;
	text-decoration: line-through;
}

.plan-features {
	display: flex;
	flex-direction: column;
	gap: 8px;
}

.feature-item {
	display: flex;
	align-items: center;
	gap: 8px;
}

.feature-text {
	font-size: 14px;
	color: #666;
}

.purchase-actions {
	padding: 20px 15px;
	text-align: center;
}

.purchase-btn {
	width: 100%;
	padding: 15px;
	background: linear-gradient(45deg, #667eea, #764ba2);
	color: white;
	border: none;
	border-radius: 12px;
	font-size: 16px;
	font-weight: 500;
	margin-bottom: 10px;
}

.purchase-btn:disabled {
	background: #ccc;
}

.purchase-note {
	font-size: 12px;
	color: #999;
}

.renewal-info {
	padding: 20px 15px;
	text-align: center;
}

.renewal-text {
	font-size: 14px;
	color: #666;
	display: block;
	margin-bottom: 15px;
}

.renewal-btn {
	padding: 12px 30px;
	background: linear-gradient(45deg, #667eea, #764ba2);
	color: white;
	border: none;
	border-radius: 25px;
	font-size: 16px;
}
</style>