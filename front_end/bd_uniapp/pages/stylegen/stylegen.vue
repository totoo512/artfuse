<template>
  <view class="home-container">
	
    <!-- 导航栏 -->
    <!-- <uni-nav-bar 
      title="艺术风格化" 
      :border="false" 
      background-color="#ffffff"
      color="#333333"
    /> -->
    
    <!-- 推荐展示区域 -->
    <uni-section title="精选推荐" type="line">
      <!-- 热门风格推荐 -->
      <view class="style-recommendations">
        <view class="section-title">热门艺术风格</view>
        <scroll-view scroll-x="true" class="style-scroll">
          <view 
            v-for="(style, index) in popularStyles" 
            :key="index" 
            class="style-card"
            @click="previewStyle(style)"
          >
            <image :src="style.preview" class="style-preview" mode="aspectFill"></image>
            <view class="style-info">
              <text class="style-name">{{ style.name }}</text>
              <text class="style-desc">{{ style.description }}</text>
            </view>
          </view>
        </scroll-view>
      </view>
      
      <!-- 用户作品展示 -->
      <view class="user-works">
        <view class="section-title">社区精选作品</view>
        <view class="works-grid">
          <view 
            v-for="(work, index) in featuredWorks" 
            :key="index" 
            class="work-item"
            @click="viewWork(work)"
          >
            <image :src="work.image" class="work-image" mode="aspectFill"></image>
            <view class="work-overlay">
              <view class="work-info">
                <text class="work-style">{{ work.style }}</text>
                <view class="work-stats">
                  <uni-icons type="heart" size="12" color="#fff"></uni-icons>
                  <text class="stat-text">{{ work.likes }}</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 快速入门指南 -->
      <view class="guide-section">
        <view class="section-title">如何开始创作</view>
        <view class="guide-steps">
          <view 
            v-for="(step, index) in guideSteps" 
            :key="index" 
            class="guide-step"
          >
            <view class="step-number">{{ index + 1 }}</view>
            <view class="step-content">
              <text class="step-title">{{ step.title }}</text>
              <text class="step-desc">{{ step.description }}</text>
            </view>
          </view>
        </view>
      </view>
    </uni-section>
    
    <!-- 悬浮创作按钮 -->
    <view 
      class="fab-container" 
      :style="{ bottom: fabPosition.bottom + 'rpx', right: fabPosition.right + 'rpx' }"
      @touchstart="onFabTouchStart"
      @touchmove="onFabTouchMove"
      @touchend="onFabTouchEnd"
    >
      <view class="fab-button" @click="startCreation">
        <uni-icons type="plus" size="24" color="#fff"></uni-icons>
        <text class="fab-text">创作</text>
      </view>
    </view>
    

  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import uniNavBar from '@/uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.vue'
import uniSection from '@/uni_modules/uni-section/components/uni-section/uni-section.vue'
import uniIcons from '@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue'

// 响应式数据
const popularStyles = ref([
  {
    name: '印象派',
    description: '莫奈风格',
    preview: '/static/c1.png'
  },
  {
    name: '立体主义',
    description: '毕加索风格',
    preview: '/static/c2.png'
  },
  {
    name: '浮世绘',
    description: '日式传统',
    preview: '/static/c3.png'
  },
  {
    name: '抽象表现',
    description: '现代艺术',
    preview: '/static/c4.png'
  }
])

const featuredWorks = ref([
  {
    image: '/static/c5.png',
    style: '印象派风格',
    likes: 128
  },
  {
    image: '/static/c6.png',
    style: '立体主义',
    likes: 95
  },
  {
    image: '/static/c7.png',
    style: '浮世绘风格',
    likes: 156
  },
  {
    image: '/static/c8.png',
    style: '抽象表现',
    likes: 203
  }
])

const guideSteps = ref([
  {
    title: '上传人物照片',
    description: '选择一张清晰的人物照片作为基础'
  },
  {
    title: '添加看展照片',
    description: '上传您在展览中拍摄的艺术作品照片'
  },
  {
    title: 'AI风格化生成',
    description: '系统将自动生成多种艺术风格的图片'
  },
  {
    title: '选择并分享',
    description: '挑选喜欢的作品，生成文案并分享'
  }
])

// 预览艺术风格
const previewStyle = (style) => {
  uni.showModal({
    title: style.name,
    content: `${style.description}\n\n点击"开始创作"按钮体验这种风格的AI生成`,
    confirmText: '开始创作',
    cancelText: '取消',
    success: (res) => {
      if (res.confirm) {
        startCreation(style)
      }
    }
  })
}

// 查看用户作品
const viewWork = (work) => {
  uni.previewImage({
    urls: [work.image],
    current: 0
  })
}

// 开始创作 - 跳转到创作页面
const startCreation = (selectedStyle = null) => {
  // TODO: 跳转到实际的风格化生成页面
  const url = selectedStyle 
    ? `/pages/creation/creation?style=${encodeURIComponent(JSON.stringify(selectedStyle))}`
    : '/pages/creation/creation'
  
  uni.navigateTo({
    url: url,
    success: () => {
      uni.showToast({
        title: '进入创作模式',
        icon: 'success'
      })
    },
    fail: () => {
      uni.showToast({
        title: '页面跳转失败，请稍后重试',
        icon: 'none'
      })
    }
  })
}

// TODO: 页面初始化时可以加载更多推荐内容
const loadMoreRecommendations = () => {
  // 加载更多热门风格或用户作品
  uni.showToast({
    title: '加载更多内容...',
    icon: 'loading'
  })
}

// TODO: 搜索功能
const searchStyles = (keyword) => {
  // 根据关键词搜索艺术风格
  console.log('搜索关键词:', keyword)
}

// 悬浮按钮位置和拖拽相关
const fabPosition = ref({
  bottom: 120, // 向上调整位置，从40rpx改为120rpx
  right: 30
})

const dragState = ref({
  isDragging: false,
  startX: 0,
  startY: 0,
  startBottom: 0,
  startRight: 0
})

// 双击重置相关
const lastTapTime = ref(0)
const initialPosition = { bottom: 120, right: 30 }

// 悬浮按钮触摸开始
const onFabTouchStart = (e) => {
  const touch = e.touches[0]
  dragState.value = {
    isDragging: true,
    startX: touch.clientX,
    startY: touch.clientY,
    startBottom: fabPosition.value.bottom,
    startRight: fabPosition.value.right
  }
}

// 悬浮按钮拖拽移动
const onFabTouchMove = (e) => {
  if (!dragState.value.isDragging) return
  
  e.preventDefault()
  const touch = e.touches[0]
  const deltaX = touch.clientX - dragState.value.startX
  const deltaY = touch.clientY - dragState.value.startY
  
  // 获取屏幕尺寸
  const systemInfo = uni.getSystemInfoSync()
  const screenWidth = systemInfo.windowWidth
  const screenHeight = systemInfo.windowHeight
  
  // 转换为rpx单位
  const deltaXRpx = (deltaX / screenWidth) * 750
  const deltaYRpx = (deltaY / screenHeight) * 750
  
  // 更新位置，Y轴方向相反（屏幕坐标向下为正，bottom值向上为正）
  let newRight = dragState.value.startRight - deltaXRpx
  let newBottom = dragState.value.startBottom - deltaYRpx
  
  // 按钮尺寸为120rpx，设置合理的边界限制
  const buttonSize = 120
  const margin = 10
  
  // 边界限制：确保按钮完全在屏幕内
  newRight = Math.max(margin, Math.min(newRight, 750 - buttonSize - margin))
  newBottom = Math.max(margin, Math.min(newBottom, (screenHeight / screenWidth) * 750 - buttonSize - margin))
  
  fabPosition.value.right = newRight
  fabPosition.value.bottom = newBottom
}

// 悬浮按钮触摸结束
const onFabTouchEnd = () => {
  const currentTime = Date.now()
  
  // 检测双击（300ms内的两次点击）
  if (currentTime - lastTapTime.value < 300) {
    // 双击重置到初始位置
    fabPosition.value.bottom = initialPosition.bottom
    fabPosition.value.right = initialPosition.right
    
    uni.showToast({
      title: '按钮已重置到初始位置',
      icon: 'success',
      duration: 1500
    })
  }
  
  lastTapTime.value = currentTime
  dragState.value.isDragging = false
}

onMounted(() => {
  // 页面初始化
})
</script>

<style scoped>
.home-container {
  padding-bottom: 100rpx;
}

.style-recommendations {
  margin-bottom: 40rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  color: #333;
  padding: 0 20rpx;
}

.style-scroll {
  white-space: nowrap;
  padding: 0 20rpx;
}

.style-card {
  display: inline-block;
  width: 200rpx;
  margin-right: 20rpx;
  background-color: #fff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
  overflow: hidden;
}

.style-preview {
  width: 100%;
  height: 150rpx;
}

.style-info {
  padding: 15rpx;
}

.style-name {
  font-size: 26rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 5rpx;
}

.style-desc {
  font-size: 22rpx;
  color: #666;
}

.user-works {
  margin-bottom: 40rpx;
}

.works-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15rpx;
  padding: 0 20rpx;
}

.work-item {
  position: relative;
  border-radius: 12rpx;
  overflow: hidden;
}

.work-image {
  width: 100%;
  height: 250rpx;
}

.work-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  padding: 20rpx 15rpx 15rpx;
}

.work-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.work-style {
  font-size: 24rpx;
  color: #fff;
}

.work-stats {
  display: flex;
  align-items: center;
  gap: 5rpx;
}

.stat-text {
  font-size: 22rpx;
  color: #fff;
}

.guide-section {
  padding: 0 20rpx;
}

.guide-steps {
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 20rpx;
}

.guide-step {
  display: flex;
  align-items: flex-start;
  margin-bottom: 25rpx;
}

.guide-step:last-child {
  margin-bottom: 0;
}

.step-number {
  width: 40rpx;
  height: 40rpx;
  background-color: #007aff;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: bold;
  margin-right: 15rpx;
  flex-shrink: 0;
}

.step-content {
  flex: 1;
}

.step-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 5rpx;
}

.step-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.fab-container {
  position: fixed;
  z-index: 999;
  transition: all 0.1s ease;
}

.fab-button {
  width: 120rpx;
  height: 120rpx;
  background-color: #007aff;
  border-radius: 60rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 16rpx rgba(0,122,255,0.3);
}

.fab-text {
  font-size: 20rpx;
  color: #fff;
  margin-top: 5rpx;
}

.result-section {
  margin-top: 40rpx;
}

.image-grid-result {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.result-image-item {
  position: relative;
  border-radius: 10rpx;
  overflow: hidden;
  border: 4rpx solid transparent;
}

.result-image-item.selected {
  border-color: #007aff;
}

.result-image {
  width: 100%;
  height: 300rpx;
}

.selected-mark {
  position: absolute;
  top: 10rpx;
  right: 10rpx;
  width: 40rpx;
  height: 40rpx;
  background-color: #007aff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.video-option {
  padding: 20rpx;
  background-color: #f8f8f8;
  border-radius: 10rpx;
  margin-top: 20rpx;
}

.video-text {
  font-size: 28rpx;
  color: #333;
}

.video-tip {
  font-size: 22rpx;
  color: #666;
  display: block;
  margin-top: 5rpx;
}

.caption-section {
  margin-top: 40rpx;
}

.caption-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx;
  background-color: #f8f8f8;
  border-radius: 10rpx;
  margin-bottom: 15rpx;
}

.caption-content {
  flex: 1;
  font-size: 26rpx;
  color: #333;
  line-height: 1.5;
  margin-right: 20rpx;
}

.action-section {
  margin: 40rpx 0;
}

.copy-btn {
  padding: 10rpx 20rpx;
  background-color: #f8f8f8;
  border: 1rpx solid #ddd;
  border-radius: 6rpx;
  font-size: 24rpx;
  color: #333;
}

.action-btn {
  width: 100%;
  height: 70rpx;
  border: none;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.action-btn-default {
  background-color: #f8f8f8;
  color: #333;
  border: 1rpx solid #ddd;
}

.action-btn-primary {
  background-color: #007aff;
  color: white;
}

.loading-content {
  padding: 40rpx;
  background-color: #fff;
  border-radius: 10rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.loading-text {
  margin-top: 20rpx;
  font-size: 26rpx;
  color: #666;
}
</style>