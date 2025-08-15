"use strict";
const common_vendor = require("../../common/vendor.js");
const services_api_config = require("./config.js");
function convertImageToBase64(filePath) {
  return new Promise((resolve, reject) => {
    if (typeof window !== "undefined" && window.FileReader) {
      const img = new Image();
      img.crossOrigin = "anonymous";
      img.onload = function() {
        const canvas = document.createElement("canvas");
        const ctx = canvas.getContext("2d");
        canvas.width = img.width;
        canvas.height = img.height;
        ctx.drawImage(img, 0, 0);
        const base64 = canvas.toDataURL("image/jpeg");
        resolve(base64);
      };
      img.onerror = function() {
        reject(new Error("图片加载失败"));
      };
      img.src = filePath;
    } else if (typeof common_vendor.index !== "undefined" && common_vendor.index.getFileSystemManager) {
      common_vendor.index.getFileSystemManager().readFile({
        filePath,
        encoding: "base64",
        success: (res) => {
          resolve(`data:image/jpeg;base64,${res.data}`);
        },
        fail: (err) => {
          reject(new Error(`图片转换失败: ${err.errMsg}`));
        }
      });
    } else {
      reject(new Error("不支持的运行环境，无法转换图片"));
    }
  });
}
async function imageToImage(personalImage, exhibitionImage, prompt = "把这两个图片融合", size = "adaptive", guidanceScale = 5.5) {
  var _a, _b;
  try {
    const base64PersonalImage = await convertImageToBase64(personalImage);
    const base64ExhibitionImage = await convertImageToBase64(exhibitionImage);
    const response = await common_vendor.index.request({
      url: `${services_api_config.API_CONFIG.BASE_URL}${services_api_config.API_CONFIG.IMAGE_TO_IMAGE_ENDPOINT}`,
      method: "POST",
      header: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${services_api_config.API_CONFIG.API_KEY}`
      },
      data: {
        model: services_api_config.API_CONFIG.IMAGE_TO_IMAGE_MODEL,
        prompt,
        image: base64PersonalImage,
        exhibition_image: base64ExhibitionImage,
        response_format: "url",
        size,
        guidance_scale: guidanceScale,
        num_images: 2
        // 请求生成两张图片
      }
    });
    common_vendor.index.__f__("log", "at services/api/imageGeneration.js:85", "API响应内容:", response);
    if (response.statusCode === 200) {
      return response.data;
    } else {
      throw new Error(`API请求失败: ${response.statusCode}, ${((_b = (_a = response.data) == null ? void 0 : _a.error) == null ? void 0 : _b.message) || "未知错误"}`);
    }
  } catch (error) {
    common_vendor.index.__f__("error", "at services/api/imageGeneration.js:93", "图像生成失败:", error);
    throw error;
  }
}
exports.imageToImage = imageToImage;
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/services/api/imageGeneration.js.map
