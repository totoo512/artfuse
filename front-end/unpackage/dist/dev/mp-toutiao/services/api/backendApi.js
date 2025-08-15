"use strict";
const common_vendor = require("../../common/vendor.js");
const services_api_config = require("./config.js");
async function request(endpoint, options = {}) {
  var _a;
  const url = `${services_api_config.API_CONFIG.BASE_URL}${endpoint}`;
  try {
    const response = await common_vendor.index.request({
      url,
      method: options.method || "GET",
      header: {
        "Content-Type": "application/json",
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
        throw new Error(result.msg || "请求失败");
      }
    } else {
      throw new Error(`HTTP ${response.statusCode}: ${((_a = response.data) == null ? void 0 : _a.msg) || "请求失败"}`);
    }
  } catch (error) {
    common_vendor.index.__f__("error", "at services/api/backendApi.js:36", `API请求失败 [${endpoint}]:`, error);
    throw error;
  }
}
async function uploadImages(portraitImage, exhibitionImages) {
  const formData = new FormData();
  formData.append("portraitImage", portraitImage);
  if (Array.isArray(exhibitionImages)) {
    exhibitionImages.forEach((image, index) => {
      formData.append("exhibitionImages", image);
    });
  }
  return request(services_api_config.API_CONFIG.ENDPOINTS.UPLOAD_IMAGES, {
    method: "POST",
    header: {
      "Content-Type": "multipart/form-data"
    },
    data: formData
  });
}
async function multimodalAnalysis(portraitImageId, exhibitionImageIds) {
  return request(services_api_config.API_CONFIG.ENDPOINTS.MULTIMODAL_ANALYSIS, {
    method: "POST",
    data: {
      portraitImageId,
      exhibitionImageIds
    }
  });
}
async function generateImages(analysisId) {
  return request(services_api_config.API_CONFIG.ENDPOINTS.GENERATE_IMAGES, {
    method: "POST",
    data: {
      analysisId
    }
  });
}
async function getUserCreations(userId, type) {
  return request(`/user/creations?userId=${userId}&type=${type}`, {
    method: "GET"
  });
}
async function getUserInfo(userId) {
  return request(`/user/info?userId=${userId}`, {
    method: "GET"
  });
}
exports.generateImages = generateImages;
exports.getUserCreations = getUserCreations;
exports.getUserInfo = getUserInfo;
exports.multimodalAnalysis = multimodalAnalysis;
exports.uploadImages = uploadImages;
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/services/api/backendApi.js.map
