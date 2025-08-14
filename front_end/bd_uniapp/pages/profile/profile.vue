<template>
	<view class="profile-container">
		<!-- 用户信息头部 -->
		<view class="user-header">
			<view class="user-avatar">
				<image :src="userInfo.avatar" class="avatar-img" mode="aspectFill"></image>
				<uni-badge :text="userInfo.vipLevel" type="error" v-if="userInfo.isVip" class="vip-badge"></uni-badge>
			</view>
			<view class="user-info">
				<text class="user-name">{{ userInfo.nickname }}</text>
				<text class="user-phone">{{ userInfo.phone || '未绑定手机号' }}</text>
				<view class="user-stats">
					<view class="stat-item">
						<text class="stat-number">{{ userInfo.aiPoints }}</text>
						<text class="stat-label">AI积分</text>
					</view>
					<view class="stat-item">
						<text class="stat-number">{{ userInfo.worksCount }}</text>
						<text class="stat-label">作品数</text>
					</view>
					<view class="stat-item">
						<text class="stat-number">{{ userInfo.exhibitionCount }}</text>
						<text class="stat-label">看展次数</text>
					</view>
				</view>
			</view>
			<view class="user-actions">
				<button class="edit-btn" @click="editProfile">
					<uni-icons type="gear" size="16" color="#666"></uni-icons>
				</button>
			</view>
		</view>

		<!-- 我的展藏 -->
		<uni-section title="我的展藏" type="line" class="section">
			<uni-list>
				<uni-list-item 
					title="看展相册" 
					note="按展览会话分类的展品照片" 
					thumb="/static/c7.png"
					show-arrow
					@click="goToExhibitionAlbum"
				>
					<template v-slot:footer>
						<uni-badge :text="userInfo.exhibitionPhotos" type="primary"></uni-badge>
					</template>
				</uni-list-item>
				<uni-list-item 
					title="生成作品" 
					note="AIGC图片和视频作品集" 
					thumb="/static/c5.png"
					show-arrow
					@click="goToGeneratedWorks"
				>
					<template v-slot:footer>
						<uni-badge :text="userInfo.worksCount" type="success"></uni-badge>
					</template>
				</uni-list-item>
			</uni-list>
		</uni-section>

		<!-- 账户信息 -->
		<uni-section title="账户信息" type="line" class="section">
			<uni-list>
				<uni-list-item 
					title="个人资料" 
					note="头像、昵称等基础信息" 
					thumb="/static/c3.png"
					show-arrow
					@click="editProfile"
				></uni-list-item>
				<uni-list-item 
					title="手机号码" 
					:note="userInfo.phone || '点击绑定手机号'" 
					thumb="/static/c4.png"
					show-arrow
					@click="bindPhone"
				></uni-list-item>
				<uni-list-item 
					title="隐私设置" 
					note="作品公开性、数据使用等" 
					thumb="/static/c1.png"
					show-arrow
					@click="privacySettings"
				></uni-list-item>
			</uni-list>
		</uni-section>

		<!-- AI积分系统 -->
		<uni-section title="AI积分系统" type="line" class="section">
			<uni-card :is-shadow="false" :border="true" class="points-card">
				<view class="points-header">
					<view class="points-info">
						<text class="points-title">当前积分</text>
						<text class="points-value">{{ userInfo.aiPoints }}</text>
					</view>
					<button class="sign-btn" @click="dailySign" :disabled="userInfo.hasSignedToday">
						{{ userInfo.hasSignedToday ? '已签到' : '每日签到' }}
					</button>
				</view>
				<view class="points-actions">
					<view class="action-item" @click="viewPointsRules">
						<uni-icons type="help" size="20" color="#007AFF"></uni-icons>
						<text class="action-text">获取规则</text>
					</view>
					<view class="action-item" @click="viewPointsHistory">
						<uni-icons type="list" size="20" color="#007AFF"></uni-icons>
						<text class="action-text">消费明细</text>
					</view>
					<view class="action-item" @click="shareApp">
						<uni-icons type="redo" size="20" color="#007AFF"></uni-icons>
						<text class="action-text">分享赚积分</text>
					</view>
				</view>
			</uni-card>
		</uni-section>

		<!-- 会员功能 -->
		<uni-section title="会员功能" type="line" class="section">
			<uni-card :is-shadow="false" :border="true" class="vip-card" :class="{ 'vip-active': userInfo.isVip }">
				<view class="vip-header">
					<view class="vip-info">
						<text class="vip-title">{{ userInfo.isVip ? 'VIP会员' : '开通会员' }}</text>
						<text class="vip-desc" v-if="userInfo.isVip">到期时间：{{ userInfo.vipExpireTime }}</text>
						<text class="vip-desc" v-else>解锁更多特权功能</text>
					</view>
					<uni-badge :text="userInfo.vipLevel" type="error" v-if="userInfo.isVip"></uni-badge>
				</view>
				<view class="vip-benefits">
					<view class="benefit-item">
						<uni-icons type="checkmarkempty" size="16" :color="userInfo.isVip ? '#FF6B35' : '#999'"></uni-icons>
						<text class="benefit-text" :class="{ 'active': userInfo.isVip }">无限生成次数</text>
					</view>
					<view class="benefit-item">
						<uni-icons type="checkmarkempty" size="16" :color="userInfo.isVip ? '#FF6B35' : '#999'"></uni-icons>
						<text class="benefit-text" :class="{ 'active': userInfo.isVip }">优先处理队列</text>
					</view>
					<view class="benefit-item">
						<uni-icons type="checkmarkempty" size="16" :color="userInfo.isVip ? '#FF6B35' : '#999'"></uni-icons>
						<text class="benefit-text" :class="{ 'active': userInfo.isVip }">专属风格模板</text>
					</view>
					<view class="benefit-item">
						<uni-icons type="checkmarkempty" size="16" :color="userInfo.isVip ? '#FF6B35' : '#999'"></uni-icons>
						<text class="benefit-text" :class="{ 'active': userInfo.isVip }">高清无水印导出</text>
					</view>
				</view>
				<button class="vip-btn" @click="manageVip" :class="{ 'renew': userInfo.isVip }">
					{{ userInfo.isVip ? '续费会员' : '立即开通' }}
				</button>
			</uni-card>
		</uni-section>

		<!-- 其他功能 -->
		<uni-section title="其他" type="line" class="section">
			<uni-list>
				<uni-list-item 
					title="帮助与反馈" 
					note="使用指南、问题反馈" 
					thumb="/static/c2.png"
					show-arrow
					@click="helpAndFeedback"
				></uni-list-item>
				<uni-list-item 
					title="关于我们" 
					note="版本信息、用户协议" 
					thumb="/static/c9.png"
					show-arrow
					@click="aboutUs"
				></uni-list-item>
			</uni-list>
		</uni-section>
	</view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'

// 用户信息数据
const userInfo = reactive({
	avatar: '/static/logo.png',
	nickname: '艺术爱好者',
	phone: '',
	aiPoints: 1280,
	worksCount: 15,
	exhibitionCount: 8,
	exhibitionPhotos: 45,
	isVip: false,
	vipLevel: 'VIP',
	vipExpireTime: '2024-12-31',
	hasSignedToday: false
})

// 页面方法
const editProfile = () => {
	uni.showToast({
		title: '编辑个人资料',
		icon: 'none'
	})
}

const goToExhibitionAlbum = () => {
	uni.navigateTo({
		url: '/pages/exhibition-album/exhibition-album'
	})
}

const goToGeneratedWorks = () => {
	uni.navigateTo({
		url: '/pages/generated-works/generated-works'
	})
}

const navigateToPointsDetail = () => {
	uni.navigateTo({
		url: '/pages/points-detail/points-detail'
	})
}

const navigateToMembership = () => {
	uni.navigateTo({
		url: '/pages/membership/membership'
	})
}

const bindPhone = () => {
	uni.showToast({
		title: '绑定手机号',
		icon: 'none'
	})
}

const privacySettings = () => {
	uni.showToast({
		title: '隐私设置',
		icon: 'none'
	})
}

const dailySign = () => {
	if (userInfo.hasSignedToday) return
	
	userInfo.hasSignedToday = true
	userInfo.aiPoints += 10
	uni.showToast({
		title: '签到成功，获得10积分',
		icon: 'success'
	})
}

const viewPointsRules = () => {
	uni.showModal({
		title: '积分获取规则',
		content: '每日签到：10积分\n分享应用：20积分\n完成看展：30积分\n生成作品：消耗50积分',
		showCancel: false
	})
}

const viewPointsHistory = () => {
	uni.navigateTo({
		url: '/pages/points-history/points-history'
	})
}

const shareApp = () => {
	uni.share({
		provider: 'weixin',
		scene: 'WXSceneSession',
		title: 'AIGC艺术风格化小程序',
		summary: '用AI创造你的艺术作品',
		imageUrl: '/static/logo.png',
		success: () => {
			userInfo.aiPoints += 20
			uni.showToast({
				title: '分享成功，获得20积分',
				icon: 'success'
			})
		}
	})
}

const manageVip = () => {
	uni.navigateTo({
		url: '/pages/vip-purchase/vip-purchase'
	})
}

const helpAndFeedback = () => {
	uni.navigateTo({
		url: '/pages/help-feedback/help-feedback'
	})
}

const aboutUs = () => {
	uni.navigateTo({
		url: '/pages/about/about'
	})
}

// 页面加载时获取用户信息
onMounted(() => {
	// 这里可以调用API获取真实的用户数据
	console.log('个人中心页面加载完成')
})
</script>

<style scoped>
.profile-container {
	background-color: #f5f5f5;
	min-height: 100vh;
}

/* 用户信息头部 */
.user-header {
	display: flex;
	align-items: center;
	padding: 20px;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	color: white;
	position: relative;
}

.user-avatar {
	position: relative;
	margin-right: 15px;
}

.avatar-img {
	width: 70px;
	height: 70px;
	border-radius: 35px;
	border: 3px solid rgba(255, 255, 255, 0.3);
}

.vip-badge {
	position: absolute;
	top: -5px;
	right: -5px;
}

.user-info {
	flex: 1;
}

.user-name {
	font-size: 20px;
	font-weight: bold;
	display: block;
	margin-bottom: 5px;
}

.user-phone {
	font-size: 14px;
	opacity: 0.8;
	display: block;
	margin-bottom: 10px;
}

.user-stats {
	display: flex;
	gap: 20px;
}

.stat-item {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-number {
	font-size: 16px;
	font-weight: bold;
}

.stat-label {
	font-size: 12px;
	opacity: 0.8;
}

.user-actions {
	margin-left: 10px;
}

.edit-btn {
	width: 40px;
	height: 40px;
	border-radius: 20px;
	background: rgba(255, 255, 255, 0.2);
	border: none;
	display: flex;
	align-items: center;
	justify-content: center;
}

/* 通用section样式 */
.section {
	margin: 15px 0;
	background: white;
	border-radius: 10px;
	overflow: hidden;
}

/* 积分卡片 */
.points-card {
	margin: 10px;
}

.points-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 15px;
}

.points-info {
	display: flex;
	flex-direction: column;
}

.points-title {
	font-size: 14px;
	color: #666;
	margin-bottom: 5px;
}

.points-value {
	font-size: 24px;
	font-weight: bold;
	color: #007AFF;
}

.sign-btn {
	padding: 8px 16px;
	background: #007AFF;
	color: white;
	border: none;
	border-radius: 20px;
	font-size: 14px;
}

.sign-btn:disabled {
	background: #ccc;
}

.points-actions {
	display: flex;
	justify-content: space-around;
	padding-top: 15px;
	border-top: 1px solid #f0f0f0;
}

.action-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 5px;
	cursor: pointer;
}

.action-text {
	font-size: 12px;
	color: #007AFF;
}

/* VIP卡片 */
.vip-card {
	margin: 10px;
	position: relative;
}

.vip-card.vip-active {
	background: linear-gradient(135deg, #FF6B35 0%, #F7931E 100%);
	color: white;
}

.vip-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 15px;
}

.vip-title {
	font-size: 18px;
	font-weight: bold;
	margin-bottom: 5px;
	display: block;
}

.vip-desc {
	font-size: 14px;
	opacity: 0.8;
}

.vip-benefits {
	display: flex;
	flex-direction: column;
	gap: 10px;
	margin-bottom: 20px;
}

.benefit-item {
	display: flex;
	align-items: center;
	gap: 8px;
}

.benefit-text {
	font-size: 14px;
	color: #666;
}

.benefit-text.active {
	color: white;
}

.vip-btn {
	width: 100%;
	padding: 12px;
	background: #007AFF;
	color: white;
	border: none;
	border-radius: 8px;
	font-size: 16px;
	font-weight: bold;
}

.vip-btn.renew {
	background: rgba(255, 255, 255, 0.2);
	color: white;
	border: 1px solid rgba(255, 255, 255, 0.3);
}
</style>
