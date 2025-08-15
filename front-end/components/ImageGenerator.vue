<template>
  <view class="image-generator-container">
    <view class="generator-card">
      <text class="title">AI图像生成</text>
      
      <view class="form-group">
        <text class="label">上传个人图像</text>
        <view class="upload-area" @click="choosePersonalImage">
          <image v-if="personalImage" :src="personalImage" class="preview-image" mode="aspectFit"></image>
          <view v-else class="upload-placeholder">
            <text class="upload-text">点击上传个人图像</text>
          </view>
        </view>
      </view>

      <view class="form-group">
        <text class="label">上传展览图像</text>
        <view class="upload-area" @click="chooseExhibitionImage">
          <image v-if="exhibitionImage" :src="exhibitionImage" class="preview-image" mode="aspectFit"></image>
          <view v-else class="upload-placeholder">
            <text class="upload-text">点击上传展览图像</text>
          </view>
        </view>
      </view>

      <!-- 提示词已设为默认值，不在界面显示 -->
      
      <view class="form-group">
        <text class="label">图像尺寸</text>
        <picker class="picker" @change="onSizeChange" :range="sizes">
          <view class="picker-text">{{ selectedSize }}</view>
        </picker>
      </view>
      
      <view class="form-group">
        <text class="label">引导比例 (1-10)</text>
        <slider class="slider" :min="1" :max="10" :value="guidanceScale" @change="onGuidanceScaleChange" />
        <text class="slider-value">{{ guidanceScale }}</text>
      </view>
      
      <!-- 水印功能已移除 -->
      
      <button class="generate-btn" :loading="isGenerating" @click="generateImage">
        {{ isGenerating ? '生成中...' : '立即生成' }}
      </button>

      <view class="result-container" v-if="resultImages.length > 0">
        <text class="result-title">生成结果</text>
        <view class="result-images-container">
          <view class="result-image-item" v-for="(imageUrl, index) in resultImages" :key="index">
            <image class="result-image" :src="imageUrl" mode="aspectFit"></image>
            <button class="save-btn" @click="saveImage(index)">保存图片 {{ index + 1 }}</button>
          </view>
        </view>
      </view>

      <view class="error-message" v-if="errorMessage">
        {{ errorMessage }}
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import { uploadImages, multimodalAnalysis, generateImages } from '@/services/api/backendApi';

// 响应式数据
const personalImage = ref('');
const exhibitionImage = ref('');
const sizes = ['adaptive'];
const selectedSize = ref('adaptive');
const guidanceScale = ref(5.5);
const isGenerating = ref(false);
const resultImages = ref([]);
const errorMessage = ref('');

// 选择个人图像
function choosePersonalImage() {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      personalImage.value = res.tempFilePaths[0];
      errorMessage.value = '';
    },
    fail: (err) => {
      errorMessage.value = `选择个人图像失败: ${err.errMsg}`;
    }
  });
}

// 选择展览图像
function chooseExhibitionImage() {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      exhibitionImage.value = res.tempFilePaths[0];
      errorMessage.value = '';
    },
    fail: (err) => {
      errorMessage.value = `选择展览图像失败: ${err.errMsg}`;
    }
  });
}

// 处理尺寸选择变化
function onSizeChange(e) {
  selectedSize.value = sizes[e.detail.value];
}

// 处理引导比例变化
function onGuidanceScaleChange(e) {
  guidanceScale.value = e.detail.value;
}

// 生成图像
async function generateImage() {
  if (!personalImage.value || !exhibitionImage.value) {
    errorMessage.value = '请先上传个人图像和展览图像';
    return;
  }

  isGenerating.value = true;
  errorMessage.value = '';
  resultImages.value = [];

  try {
    // TODO: API集成点7 - 图片上传接口（已实现）
    // 1. 上传图片 - 使用 uploadImages API
    const uploadResult = await uploadImages(personalImage.value, [exhibitionImage.value]);
    console.log('图片上传成功:', uploadResult);

    // TODO: API集成点8 - 多模态分析接口（已实现）
    // 2. 多模态分析 - 使用 multimodalAnalysis API
    const analysisResult = await multimodalAnalysis(
      uploadResult.portraitImageId,
      uploadResult.exhibitionImageIds
    );
    console.log('多模态分析成功:', analysisResult);

    // TODO: API集成点9 - 生成风格化图片接口（已实现）
    // 3. 生成风格化图片 - 使用 generateImages API
    const generateResult = await generateImages(analysisResult.analysisId);
    console.log('图片生成成功:', generateResult);

    // 提取图片URL列表
    if (generateResult.images && generateResult.images.length > 0) {
      resultImages.value = generateResult.images.map(img => img.imageUrl);
    } else {
      throw new Error('未获取到生成的图片');
    }

  } catch (error) {
    errorMessage.value = `生成失败: ${error.message}`;
    console.error('图像生成错误:', error);
  } finally {
    isGenerating.value = false;
  }
}

// 保存图片
function saveImage(index) {
  if (!resultImages.value[index]) return;

  // TODO: API集成点10 - 图片保存到相册和云端
  // 当前使用uni-app原生API保存到本地相册
  // 同时保存到云端数据库
  const imageUrl = resultImages.value[index];
  
  // 保存到本地相册
  uni.saveImageToPhotosAlbum({
    filePath: imageUrl,
    success: () => {
      uni.showToast({
        title: '保存成功',
        icon: 'success'
      });
      
      // 同时保存到云端数据库
      saveImageToCloud(imageUrl);
    },
    fail: (err) => {
      console.error('保存失败:', err);
      uni.showToast({
        title: '保存失败',
        icon: 'none'
      });
    }
  });
}

// 保存图片到云端数据库
async function saveImageToCloud(imageUrl) {
  try {
    // TODO: 从全局状态或本地存储获取用户ID
    const userId = uni.getStorageSync('userId') || 1001;
    
    const result = await saveImageToCloud(imageUrl, userId);
    console.log('图片已保存到云端:', result);
    
    // 保存成功后刷新首页的创作列表
    // 可以通过事件总线或全局状态管理通知首页刷新
    uni.$emit('refreshCreations');
    
  } catch (error) {
    console.error('保存到云端失败:', error);
    // 静默失败，不影响用户体验
  }
}

// TODO: API集成点11 - 用户选择图片功能
// 需要实现用户从生成的4张图片中选择1张的功能
// 建议接口：POST /user/select/image
// async function selectGeneratedImage(imageId) {
//   try {
//     const result = await selectImage(imageId);
//     // 处理选择结果，可能跳转到下一步（生成文本或视频）
//   } catch (error) {
//     console.error('选择图片失败:', error);
//   }
// }

// TODO: API集成点12 - 生成文本内容功能
// 基于用户选择的图片生成简介和朋友圈文案
// 建议接口：POST /user/generate/text
// async function generateTextContent(selectionId) {
//   try {
//     const result = await generateText(selectionId);
//     // 显示生成的文本内容
//   } catch (error) {
//     console.error('生成文本失败:', error);
//   }
// }

// TODO: API集成点13 - 创建视频生成任务
// 触发视频生成任务（异步）
// 建议接口：POST /user/task/create-video
// async function createVideoGenerationTask(selectionId, videoPrompt) {
//   try {
//     const result = await createVideoTask(selectionId, videoPrompt);
//     // 开始轮询任务状态
//     pollVideoTaskStatus(result.task_id);
//   } catch (error) {
//     console.error('创建视频任务失败:', error);
//   }
// }

// TODO: API集成点14 - 轮询视频任务状态
// 查询视频生成任务的实时状态
// 建议接口：GET /user/task/query-video
// async function pollVideoTaskStatus(taskId) {
//   try {
//     const status = await pollVideoTaskStatus(userId, taskId);
//     if (status.status === 3) {
//       // 视频生成完成，显示结果
//       showVideoResult(status.videoUrl);
//     }
//   } catch (error) {
//     console.error('查询视频任务状态失败:', error);
//   }
// }
</script>

<style lang="scss">
.image-generator-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.generator-card {
  width: 100%;
  max-width: 600px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 25px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  display: block;
  text-align: center;
  margin-bottom: 25px;
}

.prompt-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  margin-top: 8px;
}

.form-group {
  margin-bottom: 20px;
}

.label {
  font-size: 14px;
  color: #666;
  display: block;
  margin-bottom: 8px;
}

.upload-area {
  width: 100%;
  height: 200px;
  border: 2px dashed #ff0050;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.7);
  box-sizing: border-box;
  cursor: pointer;
}

.preview-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  object-fit: contain;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.upload-text {
  font-size: 14px;
  color: #666;
  margin-top: 10px;
}

.picker {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.7);
  box-sizing: border-box;
}

.picker-text {
  font-size: 14px;
  color: #333;
}

.slider {
  width: 100%;
}

.slider-value {
  font-size: 14px;
  color: #666;
  text-align: right;
  margin-top: 5px;
}

.switch {
  transform: scale(1.2);
  margin-left: 10px;
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
  margin-top: 10px;
  box-shadow: 0 4px 15px rgba(255, 0, 80, 0.3);
}

.result-container {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px dashed #eee;
}

.result-images-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.result-image-item {
  width: 100%;
}

.result-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 15px;
}

.result-image {
  width: 100%;
  height: auto;
  border-radius: 10px;
  margin-bottom: 15px;
}

.save-btn {
  width: 100%;
  height: 40px;
  background-color: #07c160;
  color: white;
  border: none;
  border-radius: 20px;
  font-size: 14px;
}

.error-message {
  margin-top: 15px;
  padding: 10px;
  background-color: rgba(255, 0, 0, 0.1);
  color: #ff0000;
  border-radius: 8px;
  font-size: 14px;
}
</style>