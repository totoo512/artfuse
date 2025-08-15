"use strict";
const API_CONFIG = {
  // 后端服务基础URL，根据实际部署环境调整
  BASE_URL: "http://localhost:3000",
  // 开发环境
  // BASE_URL: 'https://your-backend-domain.com', // 生产环境
  // API端点
  ENDPOINTS: {
    UPLOAD_IMAGES: "/user/upload/images",
    MULTIMODAL_ANALYSIS: "/user/analysis/multimodal",
    GENERATE_IMAGES: "/user/generate/images",
    SELECT_IMAGE: "/user/select/image",
    GENERATE_TEXT: "/user/generate/text",
    CREATE_VIDEO_TASK: "/user/task/create-video",
    QUERY_VIDEO_TASK: "/user/task/query-video"
  }
};
exports.API_CONFIG = API_CONFIG;
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/services/api/config.js.map
