<template>
  <view class="upload-container">
    <view class="glass-card">
      <view class="title">创作专属内容</view>
      
      <ImageGenerator />
      
     <!-- <view class="upload-area">
        <button class="upload-btn" @click="uploadPersonalPhoto">
          <image class="icon" src="/static/uni.png"></image>
          <text>上传个人照片</text>
        </button>
      </view> -->
      
     <!-- <view class="upload-area">
        <button class="upload-btn" @click="uploadExhibitionPhoto">
          <image class="icon" src="/static/logo.png"></image>
          <text>上传展览照片</text>
        </button>
      </view>
      -->
      <!-- <button class="generate-btn" @click="generateContent">立即生成</button> -->
      
      <!-- 添加更多内容以测试滚动 -->
     <!-- <view class="extra-content" v-for="i in 5" :key="i">
        <text>额外内容 {{ i }}（模拟长页面）</text>
      </view> -->
    </view>
  </view>
</template>

<script setup>
import ImageGenerator from '@/components/ImageGenerator.vue';

function uploadPersonalPhoto() {
  // 实现上传个人照片逻辑
  uni.chooseImage({
    count: 1,
    success: (res) => {
      console.log('个人照片上传成功', res.tempFilePaths);
      // 这里可以添加上传到服务器的逻辑
    }
  });
}

function uploadExhibitionPhoto() {
  // 实现上传展览照片逻辑
  uni.chooseImage({
    count: 1,
    success: (res) => {
      console.log('展览照片上传成功', res.tempFilePaths);
      // 这里可以添加上传到服务器的逻辑
    }
  });
}

function generateContent() {
  // 实现立即生成逻辑
  uni.showToast({
    title: '生成中...',
    icon: 'loading'
  });
  
  // 模拟生成过程
  setTimeout(() => {
    uni.hideLoading();
    uni.showToast({
      title: '生成成功',
      icon: 'success'
    });
  }, 2000);
}
</script>

<style lang="scss">
/* 关键修改点：移除固定定位，改为滚动布局 */
.upload-container {
  min-height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 20px;
  box-sizing: border-box;
  right:10px;
  /* 添加左侧安全边距，防止遮挡 */
  padding-left: 10px; 
  padding-right: 10px;
}

.glass-card {
  width: 100%;
  /* 减小 max-width，留出更多左右空间 */
  max-width: 450px; /* 原为 500px */
  /* 取消自动居中，改为左对齐（或微调 margin-left） */
  margin: 0 0 100 0px; /* 左移 10px */
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 0px; /* 减少内边距 */
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 30px;
}

.upload-area {
  margin-bottom: 20px;
}

.upload-btn {
  width: 100%;
  height: 120px;
  background: rgba(255, 255, 255, 0.7);
  border: 2px dashed #ff0050;
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  box-sizing: border-box;
}

.icon {
  width: 40px;
  height: 40px;
  margin-bottom: 10px;
}

.generate-btn {
  width: 100%;
  height: 50px;
  background: linear-gradient(45deg, #ff0050, #ff5080);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: bold;
  margin-top: 20px;
  box-shadow: 0 4px 15px rgba(255, 0, 80, 0.3);
}

.extra-content {
  padding: 15px;
  margin-top: 10px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  text-align: center;
  color: white;
}
</style>