// 后端API服务
import { API_CONFIG } from './config';

/**
 * 通用请求方法
 * @param {string} endpoint - API端点
 * @param {object} options - 请求选项
 * @returns {Promise<object>} 响应数据
 */
async function request(endpoint, options = {}) {
  const url = `${API_CONFIG.BASE_URL}${endpoint}`;
  
  try {
    const response = await uni.request({
      url,
      method: options.method || 'GET',
      header: {
        'Content-Type': 'application/json',
        ...options.header
      },
      data: options.data,
      ...options
    });

    if (response.statusCode === 200) {
      const result = response.data;
      if (result.code === 1) {
        return result.data;
      } else {
        throw new Error(result.msg || '请求失败');
      }
    } else {
      throw new Error(`HTTP ${response.statusCode}: ${response.data?.msg || '请求失败'}`);
    }
  } catch (error) {
    console.error(`API请求失败 [${endpoint}]:`, error);
    throw error;
  }
}

/**
 * 1. 用户上传图片接口
 * @param {File} portraitImage - 人物照文件
 * @param {File[]} exhibitionImages - 看展照文件列表
 * @returns {Promise<object>} 上传结果
 */
export async function uploadImages(portraitImage, exhibitionImages) {
  const formData = new FormData();
  formData.append('portraitImage', portraitImage);
  
  if (Array.isArray(exhibitionImages)) {
    exhibitionImages.forEach((image, index) => {
      formData.append('exhibitionImages', image);
    });
  }

  return request(API_CONFIG.ENDPOINTS.UPLOAD_IMAGES, {
    method: 'POST',
    header: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  });
}

/**
 * 2. 多模态分析接口
 * @param {number} portraitImageId - 人物照ID
 * @param {number[]} exhibitionImageIds - 看展照ID列表
 * @returns {Promise<object>} 分析结果
 */
export async function multimodalAnalysis(portraitImageId, exhibitionImageIds) {
  return request(API_CONFIG.ENDPOINTS.MULTIMODAL_ANALYSIS, {
    method: 'POST',
    data: {
      portraitImageId,
      exhibitionImageIds
    }
  });
}

/**
 * 3. 生成风格化图片接口
 * @param {number} analysisId - 分析ID
 * @returns {Promise<object>} 生成的图片信息
 */
export async function generateImages(analysisId) {
  return request(API_CONFIG.ENDPOINTS.GENERATE_IMAGES, {
    method: 'POST',
    data: {
      analysisId
    }
  });
}

/**
 * 4. 用户选择图片接口
 * @param {number} genImageId - 生成的图片ID
 * @returns {Promise<object>} 选择结果
 */
export async function selectImage(genImageId) {
  return request(API_CONFIG.ENDPOINTS.SELECT_IMAGE, {
    method: 'POST',
    data: {
      genImageId
    }
  });
}

/**
 * 5. 生成文本（简介 + 文案）接口
 * @param {number} selectionId - 选择ID
 * @returns {Promise<object>} 生成的文本内容
 */
export async function generateText(selectionId) {
  return request(API_CONFIG.ENDPOINTS.GENERATE_TEXT, {
    method: 'POST',
    data: {
      selectionId
    }
  });
}

/**
 * 6. 创建视频生成任务接口
 * @param {number} selectionId - 选择ID
 * @param {string} videoPrompt - 视频生成提示词
 * @returns {Promise<object>} 任务信息
 */
export async function createVideoTask(selectionId, videoPrompt) {
  return request(API_CONFIG.ENDPOINTS.CREATE_VIDEO_TASK, {
    method: 'POST',
    data: {
      selectionId,
      videoPrompt
    }
  });
}

/**
 * 7. 查询视频任务状态接口
 * @param {number} userId - 用户ID
 * @param {number} taskId - 任务ID
 * @returns {Promise<object>} 任务状态
 */
export async function queryVideoTask(userId, taskId) {
  return request(`${API_CONFIG.ENDPOINTS.QUERY_VIDEO_TASK}?userId=${userId}&taskId=${taskId}`, {
    method: 'GET'
  });
}

/**
 * 轮询查询视频任务状态
 * @param {number} userId - 用户ID
 * @param {number} taskId - 任务ID
 * @param {number} interval - 轮询间隔（毫秒）
 * @param {number} maxAttempts - 最大尝试次数
 * @returns {Promise<object>} 最终任务状态
 */
export async function pollVideoTaskStatus(userId, taskId, interval = 3000, maxAttempts = 60) {
  let attempts = 0;
  
  while (attempts < maxAttempts) {
    try {
      const status = await queryVideoTask(userId, taskId);
      
      // 任务完成或失败时停止轮询
      if (status.status === 3 || status.status === 4) {
        return status;
      }
      
      // 等待指定间隔后继续轮询
      await new Promise(resolve => setTimeout(resolve, interval));
      attempts++;
    } catch (error) {
      console.error('轮询任务状态失败:', error);
      attempts++;
      
      // 如果连续失败，增加等待时间
      await new Promise(resolve => setTimeout(resolve, interval * 2));
    }
  }
  
  throw new Error('轮询超时，任务状态查询失败');
}

// TODO: 需要后端新增的API接口

/**
 * 获取用户创作历史接口
 * @param {number} userId - 用户ID
 * @param {string} type - 类型：'image' | 'video'
 * @returns {Promise<object>} 创作历史列表
 */
export async function getUserCreations(userId, type) {
  return request(`/user/creations?userId=${userId}&type=${type}`, {
    method: 'GET'
  });
}

/**
 * 获取用户信息接口
 * @param {number} userId - 用户ID
 * @returns {Promise<object>} 用户信息
 */
export async function getUserInfo(userId) {
  return request(`/user/info?userId=${userId}`, {
    method: 'GET'
  });
}

/**
 * 用户登录接口
 * @param {string} username - 用户名
 * @param {string} password - 密码
 * @returns {Promise<object>} 登录结果，包含用户ID和token
 */
export async function userLogin(username, password) {
  return request('/user/login', {
    method: 'POST',
    data: { username, password }
  });
}

/**
 * 用户注册接口
 * @param {string} username - 用户名
 * @param {string} password - 密码
 * @param {string} email - 邮箱
 * @returns {Promise<object>} 注册结果
 */
export async function userRegister(username, password, email) {
  return request('/user/register', {
    method: 'POST',
    data: { username, password, email }
  });
}

/**
 * AI聊天接口
 * @param {string} message - 用户消息
 * @param {number} userId - 用户ID（可选）
 * @returns {Promise<object>} AI回复
 */
export async function chatWithAI(message, userId) {
  return request('/user/chat', {
    method: 'POST',
    data: { message, userId }
  });
}

/**
 * 获取聊天历史记录接口
 * @param {number} userId - 用户ID
 * @returns {Promise<object>} 聊天历史记录
 */
export async function getChatHistory(userId) {
  return request(`/user/chat/history?userId=${userId}`, {
    method: 'GET'
  });
}

/**
 * 保存图片到云端接口
 * @param {string} imageUrl - 图片URL
 * @param {number} userId - 用户ID
 * @returns {Promise<object>} 保存结果
 */
export async function saveImageToCloud(imageUrl, userId) {
  return request('/user/save-image', {
    method: 'POST',
    data: { imageUrl, userId }
  });
}

/**
 * 删除创作内容接口
 * @param {number} creationId - 创作内容ID
 * @param {number} userId - 用户ID
 * @returns {Promise<object>} 删除结果
 */
export async function deleteCreation(creationId, userId) {
  return request('/user/delete-creation', {
    method: 'DELETE',
    data: { creationId, userId }
  });
}
