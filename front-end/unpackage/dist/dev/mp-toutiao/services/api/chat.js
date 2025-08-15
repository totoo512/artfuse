"use strict";
const common_vendor = require("../../common/vendor.js");
const services_api_config = require("./config.js");
function readImageAsDataUrl(filePath, mimeType = "image/jpeg") {
  return new Promise((resolve, reject) => {
    if (typeof common_vendor.index !== "undefined" && common_vendor.index.getFileSystemManager) {
      common_vendor.index.getFileSystemManager().readFile({
        filePath,
        encoding: "base64",
        success: (res) => {
          resolve(`data:${mimeType};base64,${res.data}`);
        },
        fail: (err) => reject(new Error(`读取图片失败: ${err.errMsg}`))
      });
    } else if (typeof window !== "undefined" && window.FileReader) {
      const img = new Image();
      img.crossOrigin = "anonymous";
      img.onload = function() {
        const canvas = document.createElement("canvas");
        canvas.width = img.width;
        canvas.height = img.height;
        const ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0);
        resolve(canvas.toDataURL(mimeType));
      };
      img.onerror = function() {
        reject(new Error("图片加载失败"));
      };
      img.src = filePath;
    } else {
      reject(new Error("不支持的运行环境"));
    }
  });
}
async function visionChat(inputs, opts = {}) {
  var _a, _b, _c, _d, _e, _f, _g, _h;
  const containsImage = Array.isArray(inputs) && inputs.some((i) => i.type === "image");
  const model = opts.model || services_api_config.API_CONFIG.DEFAULT_CHAT_MODEL;
  const maxTokens = typeof opts.maxTokens === "number" ? opts.maxTokens : 300;
  let content = [];
  for (const item of inputs) {
    if (item.type === "image") {
      const isUrl = /^https?:\/\//i.test(item.value) || item.value.startsWith("data:");
      let imageUrl = item.value;
      if (!isUrl) {
        const mime = item.mimeType || "image/jpeg";
        imageUrl = await readImageAsDataUrl(item.value, mime);
      }
      content.push({
        type: "image_url",
        image_url: { url: imageUrl }
      });
    } else if (item.type === "text") {
      content.push({ type: "text", text: item.value });
    }
  }
  const isPureText = content.length > 0 && content.every((p) => p.type === "text");
  if (isPureText && !containsImage) {
    content = content.map((p) => p.text).join("\n");
  }
  const payload = {
    model,
    messages: [
      {
        role: "user",
        content
      }
    ],
    max_tokens: maxTokens
  };
  try {
    const debugPreview = {
      url: `${services_api_config.API_CONFIG.BASE_URL}${services_api_config.API_CONFIG.CHAT_COMPLETIONS_ENDPOINT}`,
      model,
      containsImage,
      isPureText: typeof content === "string",
      maxTokens,
      // 消息内容只展示类型与长度
      messagesPreview: typeof content === "string" ? `${content.slice(0, 120)}${content.length > 120 ? "…" : ""}` : content.map(
        (c, i) => c.type === "text" ? { idx: i, type: c.type, len: (c.text || "").length } : { idx: i, type: c.type, hasUrl: !!(c.image_url && c.image_url.url), urlPrefix: (c.image_url && c.image_url.url || "").slice(0, 16) }
      )
    };
    common_vendor.index.__f__("log", "at services/api/chat.js:111", "[visionChat] request preview:", debugPreview);
    const res = await common_vendor.index.request({
      url: `${services_api_config.API_CONFIG.BASE_URL}${services_api_config.API_CONFIG.CHAT_COMPLETIONS_ENDPOINT}`,
      method: "POST",
      header: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${services_api_config.API_CONFIG.API_KEY}`
      },
      data: payload
    });
    const requestId = ((_a = res.header) == null ? void 0 : _a["x-request-id"]) || ((_b = res.header) == null ? void 0 : _b["X-Request-Id"]) || ((_c = res.data) == null ? void 0 : _c.id);
    common_vendor.index.__f__("log", "at services/api/chat.js:124", "[visionChat] response status:", res.statusCode, "requestId:", requestId);
    if (res.statusCode !== 200) {
      common_vendor.index.__f__("log", "at services/api/chat.js:126", "[visionChat] response data (error):", res.data);
    } else {
      common_vendor.index.__f__("log", "at services/api/chat.js:128", "[visionChat] response data (ok):", res.data);
    }
    if (res.statusCode === 200) {
      const choice = (_e = (_d = res.data) == null ? void 0 : _d.choices) == null ? void 0 : _e[0];
      const msgContent = (_f = choice == null ? void 0 : choice.message) == null ? void 0 : _f.content;
      if (typeof msgContent === "string")
        return msgContent;
      if (Array.isArray(msgContent)) {
        const textPart = msgContent.find((p) => p.type === "text");
        return (textPart == null ? void 0 : textPart.text) || "";
      }
      return "";
    } else {
      const errMsg = ((_h = (_g = res.data) == null ? void 0 : _g.error) == null ? void 0 : _h.message) || JSON.stringify(res.data);
      throw new Error(`Chat API 失败: ${res.statusCode} ${errMsg}`);
    }
  } catch (err) {
    common_vendor.index.__f__("error", "at services/api/chat.js:145", "visionChat error:", err);
    throw err;
  }
}
exports.visionChat = visionChat;
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/services/api/chat.js.map
