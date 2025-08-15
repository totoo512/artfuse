<template>
  <view class="page-container">
   

    <!-- 主图展示区 -->
    <view class="main-banner">
      <image class="banner-image" src="/static/c1.png"></image>
      <view class="banner-text">
        <text class="banner-title">穆夏——新艺术运动时期</text>
        <text class="banner-subtitle">于20世纪第18世纪</text>
        <view class="ai-explore-btn" @click="navigateToAIExplore">
          <text class="ai-btn-text">AI看展</text>
        </view>
        <view class="try-btn-container">
          <text class="try-btn-text">立即试试</text>
          <view class="try-btn-icon" @click="navigateToUpload">
            <uni-icons type="arrow-right" size="16" color="#000000"></uni-icons>
          </view>
        </view>
      </view>
    </view>

    <!-- 创作展示区 -->
    <view class="assets-section">
      <view class="section-title">我的创作</view>
      <view class="tab-bar">
        <button class="tab-btn" :class="{ active: activeTab === 'image' }" @click="switchTab('image')">图片</button>
        <button class="tab-btn" :class="{ active: activeTab === 'video' }" @click="switchTab('video')">视频</button>
      </view>

      <!-- 瀑布流布局 -->
      <view class="masonry-container">
        <!-- 加载状态 -->
        <view v-if="isLoading" class="loading-container">
          <text class="loading-text">加载中...</text>
        </view>
        
        <!-- 空状态 -->
        <view v-else-if="currentAssets.length === 0" class="empty-container">
          <text class="empty-text">暂无{{ activeTab === 'image' ? '图片' : '视频' }}创作</text>
          <text class="empty-hint">快去创作一些精彩内容吧！</text>
        </view>
        
        <!-- 内容列表 -->
        <view v-else class="masonry-item" v-for="(item, index) in currentAssets" :key="index">
          <template v-if="activeTab === 'image'">
            <image :src="item.imageUrl" mode="aspectFill"></image>
          </template>
          <template v-else>
            <view class="video-placeholder">
              <text class="video-text">{{ item.title || item.videoUrl }}</text>
            </view>
          </template>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { getUserCreations, getUserInfo } from '@/services/api/backendApi';

// 定义响应式数据
const activeTab = ref('image'); // 'image' 或 'video'
const images = ref([]); // 用户创作的图片列表
const videos = ref([]); // 用户创作的视频列表
const isLoading = ref(false); // 加载状态
const currentUserId = ref(null); // 当前用户ID

// 定义计算属性
const currentAssets = computed(() => {
  return activeTab.value === 'image' ? images.value : videos.value;
});

// 获取用户信息
async function loadUserInfo() {
  try {
    // TODO: 从本地存储或全局状态获取用户ID
    // 这里暂时使用模拟用户ID，实际应该从登录状态获取
    const userId = uni.getStorageSync('userId') || 1001; // 模拟用户ID
    currentUserId.value = userId;
    
    const userInfo = await getUserInfo(userId);
    console.log('用户信息:', userInfo);
  } catch (error) {
    console.error('获取用户信息失败:', error);
    // 如果获取用户信息失败，使用默认用户ID
    currentUserId.value = 1001;
  }
}

// 获取用户创作历史
async function loadUserCreations(type) {
  if (!currentUserId.value) {
    console.warn('用户ID未获取，无法加载创作历史');
    return;
  }

  isLoading.value = true;
  try {
    const result = await getUserCreations(currentUserId.value, type);
    console.log(`获取${type}创作历史:`, result);
    
    if (type === 'image') {
      // 假设返回的数据结构：{ creations: [{ id, imageUrl, title, createTime }] }
      images.value = result.creations || result.images || [];
    } else if (type === 'video') {
      // 假设返回的数据结构：{ creations: [{ id, videoUrl, title, createTime }] }
      videos.value = result.creations || result.videos || [];
    }
  } catch (error) {
    console.error(`获取${type}创作历史失败:`, error);
    // 加载失败时显示空列表
    if (type === 'image') {
      images.value = [];
    } else {
      videos.value = [];
    }
    
    // 显示错误提示
    uni.showToast({
      title: `加载${type === 'image' ? '图片' : '视频'}失败`,
      icon: 'none'
    });
  } finally {
    isLoading.value = false;
  }
}

// 定义方法
function switchTab(tab) {
  activeTab.value = tab;
  // 切换标签时重新加载对应类型的数据
  loadUserCreations(tab);
}

// 页面初始化时加载数据
async function initializePage() {
  await loadUserInfo();
  await loadUserCreations('image'); // 默认加载图片
}

// 页面加载时执行
onMounted(() => {
  initializePage();
  
  // 监听刷新创作列表事件
  uni.$on('refreshCreations', () => {
    console.log('收到刷新创作列表事件');
    loadUserCreations(activeTab.value);
  });
});

// 页面卸载时移除事件监听
onUnmounted(() => {
  uni.$off('refreshCreations');
});

// 下拉刷新
function onPullDownRefresh() {
  loadUserCreations(activeTab.value).then(() => {
    uni.stopPullDownRefresh();
  });
}

function navigateToAIExplore() {
  // 跳转到AI Explore页面
  uni.navigateTo({
    url: '/pages/ai-explore/ai-explore',
    success: (res) => {
      console.log('跳转AI Explore成功', res);
    },
    fail: (err) => {
      console.error('跳转AI Explore失败', err);
      // 尝试使用redirectTo作为备选方案
      uni.redirectTo({
        url: '/pages/ai-explore/ai-explore',
        success: () => console.log('重定向AI Explore成功'),
        fail: (err2) => console.error('重定向AI Explore也失败', err2)
      });
    }
  });
}

function navigateToUpload() {
  // 尝试使用Promise形式调用并添加错误处理
  uni.navigateTo({
    url: '/pages/upload/upload',
    success: (res) => {
      console.log('跳转成功', res);
    },
    fail: (err) => {
      console.error('跳转失败', err);
      // 尝试使用redirectTo作为备选方案
      uni.redirectTo({
        url: '/pages/upload/upload',
        success: () => console.log('重定向成功'),
        fail: (err2) => console.error('重定向也失败', err2)
      });
    }
  });
}
</script>

<style lang="scss">
.page-container {
  padding: 0;
  margin: 0;
  background-color: #ffffff;
}

.brand-header {
  display: flex;
  align-items: center;
  padding: 10px 15px;
  background-color: #ffffff;
  border-bottom: 1px solid #eeeeee;

  .logo {
    width: 30px;
    height: 30px;
    margin-right: 10px;
    border-radius: 50%;
  }

  .brand-name {
    flex: 1;
    font-size: 16px;
    font-weight: bold;
  }

  .header-buttons {
    display: flex;
    align-items: center;
  }

  .feedback-btn,
  .more-btn,
  .close-btn {
    background: none;
    border: none;
    padding: 5px 10px;
    font-size: 14px;
  }
}

.main-banner {
  position: relative;
  width: 100%;
  height: 300px;
  overflow: hidden;

  .banner-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .banner-text {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    padding: 20px;
    background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
    color: #ffffff;

    .banner-title {
      font-size: 18px;
      font-weight: bold;
      display: block;
      margin-bottom: 5px;
    }

    .banner-subtitle {
      font-size: 14px;
      display: block;
      margin-bottom: 15px;
    }

    .ai-explore-btn {
      position: absolute;
      bottom: 70px;
      right: 50px;
      background-color: #4a6cf7;
      color: #ffffff;
      width: 80px;
      height: 80px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 12px rgba(74, 108, 247, 0.4);
      z-index: 10;
    }

    .ai-btn-text {
      font-size: 14px;
      font-weight: bold;
      text-align: center;
      line-height: 1.3;
    }

    .try-btn-container {
      position: absolute;
      bottom: 20px;
      right: 50px;
      display: flex;
      align-items: center;
    }

    .try-btn-text {
      color: #ffffff;
      font-size: 14px;
      margin-right: 10px;
    }

    .try-btn-icon {
      background-color: #dfd7c9;
      color: #949494;
      border: none;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0;
      box-shadow: 0 2px 8px rgba(96, 96, 96, 0.4); /* 灰色阴影 */
      cursor: pointer;
      transition: background-color 0.2s;
      
      &:active {
        background-color: #c8c0b2;
      }
    }

    .arrow-right {
      font-size: 24px;
      font-weight: bold;
      color: #000000;
    }
  }
}

.assets-section {
  padding: 15px;

  .section-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 15px;
  }

  .tab-bar {
    display: flex;
    width: 50%;
    margin-bottom: 15px;
    justify-content: space-between;

    .tab-btn {
      width: 48%;
      background: none;
      //border: none;
      padding: 4px 0;
      font-size: 14px;
      color: #999999;
      position: relative;

      &.active {
        color: #000000;
        font-weight: bold;
      }

      &.active::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 25px;
        height: 3px;
        background-color: #ff0050;
      }
    }
  }

  .masonry-container {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 10px;

    .loading-container {
      width: 100%;
      height: 200px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f5f5f5;
      border-radius: 8px;
    }

    .loading-text {
      color: #666666;
      font-size: 14px;
    }

    .empty-container {
      width: 100%;
      height: 200px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background-color: #f5f5f5;
      border-radius: 8px;
    }

    .empty-text {
      color: #666666;
      font-size: 16px;
      margin-bottom: 8px;
    }

    .empty-hint {
      color: #999999;
      font-size: 12px;
    }

    .masonry-item {      width: calc(50% - 5px); /* 减去一半的gap宽度 */      border-radius: 8px;      overflow: hidden;      background-color: #f5f5f5;      margin-bottom: 10px;      display: flex;      align-items: center;      justify-content: center;      aspect-ratio: 1/1; /* 保持正方形比例 */

      .video-placeholder {        width: 100%;        height: 100%;        background-color: #e0e0e0;        display: flex;        align-items: center;        justify-content: center;      }

      .video-text {        color: #666666;        font-size: 14px;      }

      image {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }
}
</style>
