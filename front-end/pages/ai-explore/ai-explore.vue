<template>
  <view class="ai-explore-container">
    <!-- 页面标题栏 -->

    <!-- 对话内容区域 -->
    <view class="chat-container">
      <!-- AI欢迎消息 -->
      <view class="message ai-message">
        <view class="avatar">
          <image src="/static/uni.png" mode="aspectFill"></image>
        </view>
        <view class="message-content">
          <text>欢迎来到AI看展！我可以为你介绍艺术作品、解释艺术风格，或者回答任何关于艺术的问题。</text>
        </view>
      </view>

      <!-- 消息列表 -->
      <view class="message-list">
        <view class="message" :class="msg.type === 'ai' ? 'ai-message' : 'user-message'" v-for="(msg, index) in messages" :key="index">
          <view class="avatar">
            <image :src="msg.type === 'ai' ? '/static/uni.png' : '/static/logo.png'" mode="aspectFill"></image>
          </view>
          <view class="message-content">
            <!-- 图片消息 -->
            <image v-if="msg.isImage" :src="msg.content" mode="aspectFit" class="message-image" @click="previewImage(msg.content)"></image>
            <!-- 文本消息 -->
            <text v-else>{{ msg.content }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 输入区域 -->
    <view class="input-container">
      <view class="input-wrapper">
        <input class="input" v-model="inputContent" placeholder="请输入您的问题..." @confirm="sendMessage" />
        <view class="camera-btn" @click="openCamera">
          <uni-icons type="camera" size="20" color="#666"></uni-icons>
        </view>
      </view>
      <button class="send-btn" @click="sendMessage">发送</button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';

// 定义响应式数据
const messages = ref([]);
const inputContent = ref('');
const isThinking = ref(false);
const selectedImage = ref(''); // 选中的图片

// 打开相机/选择图片
function openCamera() {
  uni.showActionSheet({
    itemList: ['拍照', '从相册选择'],
    success: (res) => {
      if (res.tapIndex === 0) {
        // 拍照
        uni.chooseImage({
          count: 1,
          sourceType: ['camera'],
          success: (res) => {
            selectedImage.value = res.tempFilePaths[0];
            // 可以在这里预览图片或直接发送
            handleImageMessage(res.tempFilePaths[0]);
          },
          fail: (err) => {
            console.error('拍照失败:', err);
            uni.showToast({
              title: '拍照失败',
              icon: 'none'
            });
          }
        });
      } else if (res.tapIndex === 1) {
        // 从相册选择
        uni.chooseImage({
          count: 1,
          sourceType: ['album'],
          success: (res) => {
            selectedImage.value = res.tempFilePaths[0];
            // 可以在这里预览图片或直接发送
            handleImageMessage(res.tempFilePaths[0]);
          },
          fail: (err) => {
            console.error('选择图片失败:', err);
            uni.showToast({
              title: '选择图片失败',
              icon: 'none'
            });
          }
        });
      }
    }
  });
}

// 处理图片消息
function handleImageMessage(imagePath) {
  // 添加图片消息到列表
  messages.value.push({
    content: imagePath,
    type: 'user',
    isImage: true
  });

  // TODO: 这里可以调用后端API进行图片分析
  // 例如：调用视觉理解API分析图片内容
  // 然后AI回复关于图片的分析结果
  
  // 模拟AI回复
  setTimeout(() => {
    messages.value.push({
      content: '我看到了您上传的图片！这是一个很有趣的图像，我可以为您分析其中的内容...',
      type: 'ai'
    });
  }, 1000);
}

// 预览图片
function previewImage(imageUrl) {
  uni.previewImage({
    urls: [imageUrl],
    current: imageUrl
  });
}

// 发送消息方法
async function sendMessage() {
  if (!inputContent.value.trim()) return;

  // 添加用户消息到列表
  messages.value.push({
    content: inputContent.value,
    type: 'user'
  });

  // 清空输入框
  inputContent.value = '';

  // TODO: API集成点4 - AI聊天接口
  // 需要后端提供聊天接口，替换当前的模拟回复
  // 建议接口：POST /user/chat
  // 请求参数：{ message: string, userId?: string }
  // 返回：{ reply: string, type: 'ai' }
  try {
    isThinking.value = true;
    
    // 当前使用模拟回复，等待后端接入
    setTimeout(() => {
      messages.value.push({
        content: '感谢您的提问！这是一个非常有趣的艺术话题。我会为您提供更详细的信息...',
        type: 'ai'
      });
      isThinking.value = false;
    }, 1000);
    
    // TODO: 替换为真实API调用
    // const response = await chatWithAI(inputContent.value);
    // messages.value.push({
    //   content: response.reply,
    //   type: 'ai'
    // });
    
  } catch (error) {
    console.error('AI 回复失败:', error);
    messages.value.push({
      content: '抱歉，AI 暂时无法回复，请稍后再试。',
      type: 'ai'
    });
  } finally {
    isThinking.value = false;
  }
}

// TODO: API集成点5 - 聊天历史记录
// 需要在页面加载时获取用户的聊天历史记录
// 建议接口：GET /user/chat/history?userId=xxx
// async function loadChatHistory() {
//   try {
//     const history = await getChatHistory();
//     messages.value = history;
//   } catch (error) {
//     console.error('加载聊天历史失败:', error);
//   }
// }

// TODO: API集成点6 - 用户身份验证
// 需要实现用户登录/注册功能，获取用户ID
// 建议接口：POST /user/login 或 POST /user/register
// async function authenticateUser() {
//   // 实现用户认证逻辑
// }
</script>

<style lang="scss">
.ai-explore-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.header {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
  border-bottom: 1px solid #eeeeee;

  .title {
    font-size: 18px;
    font-weight: bold;
    color: #000000;
  }
}

.chat-container {
  flex: 1;
  padding: 40px;
  overflow-y: auto;
}

.message-list {
  width: 100%;
}

.message {
  display: flex;
  margin-bottom: 12px;
  align-items: flex-start;

  .avatar {
    width: 32px;
    height: 32px;
    flex: 0 0 32px;
    min-width: 32px;
    min-height: 32px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 8px;

    image {
      width: 100%;
      height: 100%;
    }
  }

  .message-content {
    padding: 8px 12px;
    border-radius: 14px;
    word-break: break-all;
    line-height: 1.45;
    max-width: 75%;

    text {
      font-size: 13px;
    }

    .message-image {
      max-width: 200px;
      max-height: 200px;
      border-radius: 8px;
      cursor: pointer;
    }
  }
}

.ai-message {
  align-self: flex-start;

  .message-content {
    background-color: #ffffff;
    color: #000000;
    box-shadow: 0 1px 2px rgba(0,0,0,0.04);
  }
}

.user-message {
  flex-direction: row-reverse;
  align-self: flex-end;

  .avatar {
    margin-right: 0;
    margin-left: 8px;
  }

  .message-content {
    background-color: #4a6cf7;
    color: #ffffff;
    box-shadow: 0 1px 2px rgba(0,0,0,0.06);
  }
}

.input-container {
  display: flex;
  padding: 8px;
  background-color: #ffffff;
  border-top: 1px solid #eeeeee;

  .input-wrapper {
    display: flex;
    align-items: center;
    flex: 1;
    height: 36px;
    padding: 0 12px;
    background-color: #f5f5f5;
    border-radius: 18px;
    margin-right: 8px;
  }

  .input {
    flex: 1;
    height: 36px;
    padding: 0;
    background: none;
    border: none;
    border-radius: 0;
    font-size: 13px;
  }

  .camera-btn {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    background-color: #e0e0e0;
    margin-left: 8px;
    transition: background-color 0.2s;
    
    &:active {
      background-color: #d0d0d0;
    }
  }

  .send-btn {
    width: 72px;
    height: 36px;
    margin-left: 8px;
    background-color: #4a6cf7;
    color: #ffffff;
    border-radius: 18px;
    font-size: 13px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
