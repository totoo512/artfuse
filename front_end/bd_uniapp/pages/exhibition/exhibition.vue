<template>
  <view class="exhibition-container">
    <!-- 
      TODO: AI看展解析页面主体结构
      
      功能模块：
      1. 展览会话列表 - 显示所有创建的展览会话
      2. 会话详情页 - 展示具体会话的解析记录
      3. 创建会话功能 - 通过链接或照片创建新会话
      4. 拍照解析功能 - 实时解读展品
      
      页面状态：
      - sessionList: 会话列表视图
      - sessionDetail: 会话详情视图
      - createSession: 创建会话视图
    -->
    
    <!-- 会话列表视图 -->
    <view v-if="currentView === 'sessionList'" class="session-list-view">
      <!-- 页面标题 -->
      <view class="page-header">
        <text class="page-title">AI看展解析</text>
        <button 
          type="button" 
          @click="showCreateSession"
          class="create-btn"
        >
          <uni-icons type="plus" size="20" color="#fff"></uni-icons>
          <text class="create-text">创建会话</text>
        </button>
      </view>
      
      <!-- 会话列表 -->
      <view class="session-list">
        <view 
          v-for="session in sessionList" 
          :key="session.id"
          class="session-item"
          @click="enterSession(session)"
        >
          <view class="session-info">
            <view class="session-title">{{ session.title }}</view>
            <view class="session-meta">
              <text class="session-time">{{ formatTime(session.createTime) }}</text>
              <text class="session-count">{{ session.analysisCount }}次解析</text>
            </view>
            <view class="session-desc">{{ session.description }}</view>
          </view>
          <view class="session-actions">
            <button 
              type="button" 
              @click.stop="editSession(session)"
              class="action-btn edit-btn"
            >
              <uni-icons type="compose" size="16" color="#666"></uni-icons>
            </button>
            <button 
              type="button" 
              @click.stop="deleteSession(session.id)"
              class="action-btn delete-btn"
            >
              <uni-icons type="trash" size="16" color="#ff4757"></uni-icons>
            </button>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view v-if="sessionList.length === 0" class="empty-state">
          <uni-icons type="image" size="60" color="#ccc"></uni-icons>
          <text class="empty-text">还没有展览会话</text>
          <text class="empty-desc">点击右上角创建您的第一个展览会话</text>
        </view>
      </view>
    </view>
    
    <!-- 创建会话视图 -->
    <view v-if="currentView === 'createSession'" class="create-session-view">
      <!-- 导航栏 -->
      <view class="nav-header">
        <button 
          type="button" 
          @click="backToList"
          class="nav-back-btn"
        >
          <uni-icons type="back" size="20" color="#333"></uni-icons>
        </button>
        <text class="nav-title">创建展览会话</text>
        <view class="nav-placeholder"></view>
      </view>
      
      <!-- 创建方式选择 -->
      <view class="create-methods">
        <view class="method-title">选择创建方式</view>
        
        <!-- 方式1: 导入链接 -->
        <view class="method-item">
          <view class="method-header">
            <uni-icons type="link" size="24" color="#007aff"></uni-icons>
            <text class="method-name">导入展览链接</text>
          </view>
          <view class="method-desc">支持公众号文章、小红书等展览介绍链接</view>
          <view class="link-input-area">
            <input 
              v-model="linkUrl"
              placeholder="请粘贴展览介绍链接"
              class="link-input"
            />
            <button 
              type="button" 
              @click="importFromLink"
              :disabled="!linkUrl.trim()"
              class="import-btn"
            >
              导入
            </button>
          </view>
        </view>
        
        <!-- 方式2: 拍照上传 -->
        <view class="method-item">
          <view class="method-header">
            <uni-icons type="camera" size="24" color="#007aff"></uni-icons>
            <text class="method-name">拍照创建</text>
          </view>
          <view class="method-desc">拍摄展览标题、前言、艺术家简介等信息</view>
          
          <!-- 上传的照片展示 -->
          <view class="upload-photos">
            <view 
              v-for="(photo, index) in uploadedPhotos" 
              :key="index"
              class="photo-item"
            >
              <image :src="photo.url" class="photo-image" mode="aspectFill"></image>
              <view class="photo-label">{{ photo.label }}</view>
              <view 
                class="photo-delete"
                @click="removePhoto(index)"
              >
                <uni-icons type="close" size="14" color="#fff"></uni-icons>
              </view>
            </view>
          </view>
          
          <!-- 拍照按钮组 -->
          <view class="photo-buttons">
            <button 
              type="button" 
              @click="takePhoto('title')"
              class="photo-btn"
            >
              <uni-icons type="image" size="16" color="#007aff"></uni-icons>
              展览标题
            </button>
            <button 
              type="button" 
              @click="takePhoto('intro')"
              class="photo-btn"
            >
              <uni-icons type="image" size="16" color="#007aff"></uni-icons>
              展览前言
            </button>
            <button 
              type="button" 
              @click="takePhoto('artist')"
              class="photo-btn"
            >
              <uni-icons type="image" size="16" color="#007aff"></uni-icons>
              艺术家简介
            </button>
            <button 
              type="button" 
              @click="takePhoto('guide')"
              class="photo-btn"
            >
              <uni-icons type="image" size="16" color="#007aff"></uni-icons>
              导览手册
            </button>
          </view>
          
          <!-- 创建按钮 -->
          <button 
            type="button" 
            @click="createFromPhotos"
            :disabled="uploadedPhotos.length === 0"
            class="create-session-btn"
          >
            创建会话
          </button>
        </view>
      </view>
    </view>
    
    <!-- 会话详情视图 -->
    <view v-if="currentView === 'sessionDetail'" class="session-detail-view">
      <!-- 导航栏 -->
      <view class="nav-header">
        <button 
          type="button" 
          @click="backToList"
          class="nav-back-btn"
        >
          <uni-icons type="back" size="20" color="#333"></uni-icons>
        </button>
        <text class="nav-title">{{ currentSession?.title || '展览解析' }}</text>
        <view class="nav-placeholder"></view>
      </view>
      
      <!-- 解析记录列表 -->
      <view class="analysis-list">
        <view 
          v-for="analysis in currentSession?.analysisList || []" 
          :key="analysis.id"
          class="analysis-item"
        >
          <!-- 用户拍摄的照片 -->
          <view class="user-photo">
            <image :src="analysis.photo" class="analysis-photo" mode="aspectFill"></image>
            <view class="photo-time">{{ formatTime(analysis.createTime) }}</view>
          </view>
          
          <!-- AI解析结果 -->
          <view class="analysis-result">
            <view class="result-header">
              <uni-icons type="robot" size="20" color="#007aff"></uni-icons>
              <text class="result-title">AI解析</text>
            </view>
            
            <view class="result-content">
              <!-- 作品基本信息 -->
              <view v-if="analysis.result.basicInfo" class="info-section">
                <view class="section-title">作品信息</view>
                <view class="info-item">
                  <text class="info-label">作品名称：</text>
                  <text class="info-value">{{ analysis.result.basicInfo.title }}</text>
                </view>
                <view class="info-item">
                  <text class="info-label">艺术家：</text>
                  <text class="info-value">{{ analysis.result.basicInfo.artist }}</text>
                </view>
                <view class="info-item">
                  <text class="info-label">创作年代：</text>
                  <text class="info-value">{{ analysis.result.basicInfo.year }}</text>
                </view>
              </view>
              
              <!-- 艺术风格 -->
              <view v-if="analysis.result.style" class="info-section">
                <view class="section-title">艺术风格</view>
                <text class="section-content">{{ analysis.result.style }}</text>
              </view>
              
              <!-- 创作背景 -->
              <view v-if="analysis.result.background" class="info-section">
                <view class="section-title">创作背景</view>
                <text class="section-content">{{ analysis.result.background }}</text>
              </view>
              
              <!-- 趣味细节 -->
              <view v-if="analysis.result.details" class="info-section">
                <view class="section-title">趣味细节</view>
                <text class="section-content">{{ analysis.result.details }}</text>
              </view>
              
              <!-- 作者故事 -->
              <view v-if="analysis.result.story" class="info-section">
                <view class="section-title">作者故事</view>
                <text class="section-content">{{ analysis.result.story }}</text>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view v-if="!currentSession?.analysisList?.length" class="empty-analysis">
          <uni-icons type="camera" size="60" color="#ccc"></uni-icons>
          <text class="empty-text">还没有解析记录</text>
          <text class="empty-desc">点击下方拍照按钮开始解析展品</text>
        </view>
      </view>
      
      <!-- 底部拍照按钮 -->
      <view class="bottom-actions">
        <button 
          type="button" 
          @click="takeAnalysisPhoto"
          class="camera-btn"
        >
          <uni-icons type="camera-filled" size="30" color="#fff"></uni-icons>
          <text class="camera-text">拍照解析</text>
        </button>
      </view>
    </view>
    
    <!-- 加载弹窗 -->
    <uni-popup ref="loadingPopup" type="center">
      <view class="loading-content">
        <uni-load-more status="loading" :content-text="{ contentdown: loadingText }"></uni-load-more>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
// TODO: AI看展解析功能实现
// 
// 核心功能模块：
// 1. 展览会话管理 - 创建、编辑、删除会话
// 2. 链接导入功能 - 解析公众号、小红书等链接
// 3. 照片上传功能 - 拍摄展览相关信息
// 4. AI解析功能 - 识别展品并生成解读内容
// 5. 数据存储功能 - 自动保存到个人相册
import { ref, reactive, computed, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import uniIcons from '@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue'
import uniPopup from '@/uni_modules/uni-popup/components/uni-popup/uni-popup.vue'
import uniLoadMore from '@/uni_modules/uni-load-more/components/uni-load-more/uni-load-more.vue'

// TODO: 页面状态管理
// 
// 视图状态：
// - sessionList: 会话列表页
// - createSession: 创建会话页
// - sessionDetail: 会话详情页
const currentView = ref('sessionList')
const currentSession = ref(null)

// TODO: 会话数据管理
// 
// sessionList: 所有展览会话列表
// 数据结构：
// - id: 会话唯一标识
// - title: 展览名称
// - description: 展览描述
// - createTime: 创建时间
// - analysisCount: 解析次数
// - analysisList: 解析记录列表
// - context: 展览上下文信息
const sessionList = ref([
  // 示例数据
  {
    id: '1',
    title: '印象派大师展',
    description: '探索印象派艺术的光影世界',
    createTime: Date.now() - 86400000,
    analysisCount: 5,
    analysisList: [
      {
        id: 'a1',
        photo: '/static/c1.png',
        createTime: Date.now() - 3600000,
        result: {
          basicInfo: {
            title: '日出·印象',
            artist: '克劳德·莫奈',
            year: '1872年'
          },
          style: '印象派风格，注重光线和色彩的变化，笔触松散而富有表现力。',
          background: '这幅画描绘了勒阿弗尔港口的日出景象，是印象派运动的开山之作。',
          details: '画面中的太阳反射在水面上形成橙色的光带，船只的轮廓在晨雾中若隐若现。',
          story: '莫奈在1872年的一个早晨，在勒阿弗尔的港口画下了这幅作品，后来成为了印象派的代表作。'
        }
      }
    ],
    context: {
      exhibition: '印象派大师展',
      venue: '国家美术馆',
      period: '2024年1月-3月'
    }
  }
])

// TODO: 创建会话相关数据
// 
// linkUrl: 导入的链接地址
// uploadedPhotos: 上传的照片列表
// loadingText: 加载提示文本
const linkUrl = ref('')
const uploadedPhotos = ref([])
const loadingText = ref('')
const loadingPopup = ref(null)

// TODO: 页面导航方法
// 
// 视图切换和导航逻辑
const showCreateSession = () => {
  currentView.value = 'createSession'
  // 重置创建会话的数据
  linkUrl.value = ''
  uploadedPhotos.value = []
}

const backToList = () => {
  currentView.value = 'sessionList'
  currentSession.value = null
}

const enterSession = (session) => {
  currentSession.value = session
  currentView.value = 'sessionDetail'
}

// TODO: 会话管理方法
// 
// 编辑、删除会话功能
const editSession = (session) => {
  uni.showModal({
    title: '编辑会话',
    editable: true,
    placeholderText: session.title,
    success: (res) => {
      if (res.confirm && res.content) {
        session.title = res.content
        uni.showToast({
          title: '修改成功',
          icon: 'success'
        })
      }
    }
  })
}

const deleteSession = (sessionId) => {
  uni.showModal({
    title: '确认删除',
    content: '删除后无法恢复，确定要删除这个会话吗？',
    success: (res) => {
      if (res.confirm) {
        const index = sessionList.value.findIndex(s => s.id === sessionId)
        if (index > -1) {
          sessionList.value.splice(index, 1)
          uni.showToast({
            title: '删除成功',
            icon: 'success'
          })
        }
      }
    }
  })
}

// TODO: 链接导入功能
// 
// 解析公众号、小红书等展览介绍链接
// 提取展览信息并创建会话
const importFromLink = async () => {
  if (!linkUrl.value.trim()) return
  
  loadingText.value = '正在解析链接内容...'
  loadingPopup.value?.open()
  
  try {
    // 模拟API调用解析链接
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 模拟解析结果
    const newSession = {
      id: Date.now().toString(),
      title: '从链接导入的展览',
      description: '通过链接解析获得的展览信息',
      createTime: Date.now(),
      analysisCount: 0,
      analysisList: [],
      context: {
        source: 'link',
        url: linkUrl.value
      }
    }
    
    sessionList.value.unshift(newSession)
    
    loadingPopup.value?.close()
    uni.showToast({
      title: '导入成功',
      icon: 'success'
    })
    
    // 跳转到会话详情
    enterSession(newSession)
    
  } catch (error) {
    loadingPopup.value?.close()
    uni.showToast({
      title: '导入失败',
      icon: 'none'
    })
  }
}

// TODO: 拍照上传功能
// 
// 支持拍摄展览标题、前言、艺术家简介等
// 不同类型的照片有不同的标签
const takePhoto = (type) => {
  const typeLabels = {
    title: '展览标题',
    intro: '展览前言', 
    artist: '艺术家简介',
    guide: '导览手册'
  }
  
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['camera', 'album'],
    success: (res) => {
      uploadedPhotos.value.push({
        url: res.tempFilePaths[0],
        type: type,
        label: typeLabels[type]
      })
      
      uni.showToast({
        title: `${typeLabels[type]}上传成功`,
        icon: 'success'
      })
    }
  })
}

const removePhoto = (index) => {
  uploadedPhotos.value.splice(index, 1)
}

// TODO: 从照片创建会话
// 
// 基于上传的照片创建展览会话
// AI解析照片内容提取展览信息
const createFromPhotos = async () => {
  if (uploadedPhotos.value.length === 0) return
  
  loadingText.value = 'AI正在解析展览信息...'
  loadingPopup.value?.open()
  
  try {
    // 模拟AI解析照片内容
    await new Promise(resolve => setTimeout(resolve, 3000))
    
    // 模拟解析结果
    const newSession = {
      id: Date.now().toString(),
      title: '从照片创建的展览',
      description: 'AI解析照片内容生成的展览会话',
      createTime: Date.now(),
      analysisCount: 0,
      analysisList: [],
      context: {
        source: 'photos',
        photos: [...uploadedPhotos.value]
      }
    }
    
    sessionList.value.unshift(newSession)
    
    loadingPopup.value?.close()
    uni.showToast({
      title: '会话创建成功',
      icon: 'success'
    })
    
    // 跳转到会话详情
    enterSession(newSession)
    
  } catch (error) {
    loadingPopup.value?.close()
    uni.showToast({
      title: '创建失败',
      icon: 'none'
    })
  }
}

// TODO: 展品拍照解析功能
// 
// 在会话中拍摄具体展品进行AI解析
// 结合展览上下文返回详细解读
const takeAnalysisPhoto = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['camera', 'album'],
    success: async (res) => {
      const photoUrl = res.tempFilePaths[0]
      
      loadingText.value = 'AI正在解析展品...'
      loadingPopup.value?.open()
      
      try {
        // 模拟AI解析展品
        await new Promise(resolve => setTimeout(resolve, 4000))
        
        // 模拟解析结果
        const analysisResult = {
          id: Date.now().toString(),
          photo: photoUrl,
          createTime: Date.now(),
          result: {
            basicInfo: {
              title: '星夜',
              artist: '文森特·梵高',
              year: '1889年'
            },
            style: '后印象派风格，运用旋转的笔触和强烈的色彩对比，表现出强烈的情感和动感。',
            background: '梵高在圣雷米精神病院期间创作，描绘了他从病房窗户看到的夜空景象。',
            details: '画面中的星星和月亮散发着光芒，天空中的云彩呈现螺旋状，柏树像火焰一样向上伸展。',
            story: '这是梵高最著名的作品之一，创作时他正处于精神状态不稳定的时期，但艺术创作力却达到了巅峰。'
          }
        }
        
        // 添加到当前会话的解析列表
        if (!currentSession.value.analysisList) {
          currentSession.value.analysisList = []
        }
        currentSession.value.analysisList.unshift(analysisResult)
        currentSession.value.analysisCount = currentSession.value.analysisList.length
        
        // TODO: 自动保存到个人相册
        // 这里应该调用保存到个人相册的API
        saveToPersonalAlbum(analysisResult)
        
        loadingPopup.value?.close()
        uni.showToast({
          title: '解析完成',
          icon: 'success'
        })
        
      } catch (error) {
        loadingPopup.value?.close()
        uni.showToast({
          title: '解析失败',
          icon: 'none'
        })
      }
    }
  })
}

// TODO: 保存到个人相册功能
// 
// 将解析结果自动保存到个人中心的看展相册
// 按展览会话分类存储
const saveToPersonalAlbum = (analysisResult) => {
  // 这里应该调用API保存到个人相册
  // 暂时使用本地存储模拟
  const albumKey = 'exhibition_album'
  let album = uni.getStorageSync(albumKey) || []
  
  const albumItem = {
    sessionId: currentSession.value.id,
    sessionTitle: currentSession.value.title,
    photo: analysisResult.photo,
    analysis: analysisResult.result,
    createTime: analysisResult.createTime
  }
  
  album.unshift(albumItem)
  uni.setStorageSync(albumKey, album)
}

// TODO: 工具方法
// 
// 时间格式化等辅助功能
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) {
    return '刚刚'
  } else if (diff < 3600000) {
    return `${Math.floor(diff / 60000)}分钟前`
  } else if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)}小时前`
  } else {
    return `${Math.floor(diff / 86400000)}天前`
  }
}

// TODO: 页面生命周期
onMounted(() => {
  // 页面初始化
  // 可以在这里加载用户的会话列表
})
</script>

<style scoped>
/* TODO: AI看展解析页面样式 */

.exhibition-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 会话列表视图样式 */
.session-list-view {
  padding: 20rpx;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  margin-bottom: 30rpx;
}

.page-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.create-btn {
  display: flex;
  align-items: center;
  padding: 15rpx 25rpx;
  background-color: #007aff;
  color: white;
  border: none;
  border-radius: 25rpx;
  font-size: 26rpx;
}

.create-text {
  margin-left: 8rpx;
}

.session-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.session-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background-color: white;
  border-radius: 15rpx;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.1);
}

.session-info {
  flex: 1;
}

.session-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.session-meta {
  display: flex;
  gap: 20rpx;
  margin-bottom: 10rpx;
}

.session-time {
  font-size: 24rpx;
  color: #999;
}

.session-count {
  font-size: 24rpx;
  color: #007aff;
}

.session-desc {
  font-size: 26rpx;
  color: #666;
  line-height: 1.4;
}

.session-actions {
  display: flex;
  gap: 15rpx;
}

.action-btn {
  width: 60rpx;
  height: 60rpx;
  border: none;
  border-radius: 50%;
  background-color: #f8f8f8;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  margin-top: 20rpx;
}

.empty-desc {
  font-size: 24rpx;
  color: #ccc;
  margin-top: 10rpx;
}

/* 创建会话视图样式 */
.create-session-view {
  min-height: 100vh;
  background-color: white;
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.nav-back-btn {
  width: 60rpx;
  height: 60rpx;
  border: none;
  background: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.nav-placeholder {
  width: 60rpx;
}

.create-methods {
  padding: 30rpx;
}

.method-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
}

.method-item {
  margin-bottom: 40rpx;
  padding: 30rpx;
  background-color: #f8f8f8;
  border-radius: 15rpx;
}

.method-header {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.method-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-left: 15rpx;
}

.method-desc {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 20rpx;
  line-height: 1.4;
}

.link-input-area {
  display: flex;
  gap: 15rpx;
}

.link-input {
  flex: 1;
  padding: 20rpx;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 26rpx;
}

.import-btn {
  padding: 20rpx 30rpx;
  background-color: #007aff;
  color: white;
  border: none;
  border-radius: 8rpx;
  font-size: 26rpx;
}

.import-btn:disabled {
  background-color: #ccc;
}

.upload-photos {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15rpx;
  margin-bottom: 20rpx;
}

.photo-item {
  position: relative;
  aspect-ratio: 1;
  border-radius: 8rpx;
  overflow: hidden;
}

.photo-image {
  width: 100%;
  height: 100%;
}

.photo-label {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 8rpx;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: white;
  font-size: 20rpx;
  text-align: center;
}

.photo-delete {
  position: absolute;
  top: 8rpx;
  right: 8rpx;
  width: 30rpx;
  height: 30rpx;
  background-color: rgba(0,0,0,0.6);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.photo-buttons {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15rpx;
  margin-bottom: 30rpx;
}

.photo-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 25rpx;
  border: 2rpx dashed #007aff;
  background-color: white;
  color: #007aff;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.create-session-btn {
  width: 100%;
  padding: 25rpx;
  background-color: #007aff;
  color: white;
  border: none;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.create-session-btn:disabled {
  background-color: #ccc;
}

/* 会话详情视图样式 */
.session-detail-view {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.analysis-list {
  padding: 20rpx;
}

.analysis-item {
  margin-bottom: 30rpx;
  background-color: white;
  border-radius: 15rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.1);
}

.user-photo {
  position: relative;
}

.analysis-photo {
  width: 100%;
  height: 400rpx;
}

.photo-time {
  position: absolute;
  top: 20rpx;
  right: 20rpx;
  padding: 8rpx 15rpx;
  background-color: rgba(0,0,0,0.6);
  color: white;
  border-radius: 15rpx;
  font-size: 22rpx;
}

.analysis-result {
  padding: 30rpx;
}

.result-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.result-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-left: 10rpx;
}

.result-content {
  display: flex;
  flex-direction: column;
  gap: 25rpx;
}

.info-section {
  padding: 20rpx;
  background-color: #f8f8f8;
  border-radius: 10rpx;
}

.section-title {
  font-size: 26rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 15rpx;
}

.info-item {
  display: flex;
  margin-bottom: 8rpx;
}

.info-label {
  font-size: 24rpx;
  color: #666;
  min-width: 120rpx;
}

.info-value {
  font-size: 24rpx;
  color: #333;
  flex: 1;
}

.section-content {
  font-size: 26rpx;
  color: #333;
  line-height: 1.6;
}

.empty-analysis {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  background-color: white;
  border-top: 1rpx solid #eee;
}

.camera-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 25rpx;
  background-color: #007aff;
  color: white;
  border: none;
  border-radius: 25rpx;
  font-size: 28rpx;
}

.camera-text {
  margin-left: 15rpx;
}

.loading-content {
  padding: 40rpx;
  background-color: white;
  border-radius: 15rpx;
  text-align: center;
}
</style>