"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  _easycom_uni_icons2();
}
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  _easycom_uni_icons();
}
const _sfc_main = {
  __name: "ai-explore",
  setup(__props) {
    const messages = common_vendor.ref([]);
    const inputContent = common_vendor.ref("");
    const isThinking = common_vendor.ref(false);
    const selectedImage = common_vendor.ref("");
    function openCamera() {
      common_vendor.index.showActionSheet({
        itemList: ["拍照", "从相册选择"],
        success: (res) => {
          if (res.tapIndex === 0) {
            common_vendor.index.chooseImage({
              count: 1,
              sourceType: ["camera"],
              success: (res2) => {
                selectedImage.value = res2.tempFilePaths[0];
                handleImageMessage(res2.tempFilePaths[0]);
              },
              fail: (err) => {
                common_vendor.index.__f__("error", "at pages/ai-explore/ai-explore.vue:71", "拍照失败:", err);
                common_vendor.index.showToast({
                  title: "拍照失败",
                  icon: "none"
                });
              }
            });
          } else if (res.tapIndex === 1) {
            common_vendor.index.chooseImage({
              count: 1,
              sourceType: ["album"],
              success: (res2) => {
                selectedImage.value = res2.tempFilePaths[0];
                handleImageMessage(res2.tempFilePaths[0]);
              },
              fail: (err) => {
                common_vendor.index.__f__("error", "at pages/ai-explore/ai-explore.vue:89", "选择图片失败:", err);
                common_vendor.index.showToast({
                  title: "选择图片失败",
                  icon: "none"
                });
              }
            });
          }
        }
      });
    }
    function handleImageMessage(imagePath) {
      messages.value.push({
        content: imagePath,
        type: "user",
        isImage: true
      });
      setTimeout(() => {
        messages.value.push({
          content: "我看到了您上传的图片！这是一个很有趣的图像，我可以为您分析其中的内容...",
          type: "ai"
        });
      }, 1e3);
    }
    function previewImage(imageUrl) {
      common_vendor.index.previewImage({
        urls: [imageUrl],
        current: imageUrl
      });
    }
    async function sendMessage() {
      if (!inputContent.value.trim())
        return;
      messages.value.push({
        content: inputContent.value,
        type: "user"
      });
      inputContent.value = "";
      try {
        isThinking.value = true;
        setTimeout(() => {
          messages.value.push({
            content: "感谢您的提问！这是一个非常有趣的艺术话题。我会为您提供更详细的信息...",
            type: "ai"
          });
          isThinking.value = false;
        }, 1e3);
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/ai-explore/ai-explore.vue:169", "AI 回复失败:", error);
        messages.value.push({
          content: "抱歉，AI 暂时无法回复，请稍后再试。",
          type: "ai"
        });
      } finally {
        isThinking.value = false;
      }
    }
    return (_ctx, _cache) => {
      return {
        a: common_assets._imports_0$1,
        b: common_vendor.f(messages.value, (msg, index, i0) => {
          return common_vendor.e({
            a: msg.type === "ai" ? "/static/uni.png" : "/static/logo.png",
            b: msg.isImage
          }, msg.isImage ? {
            c: msg.content,
            d: common_vendor.o(($event) => previewImage(msg.content))
          } : {
            e: common_vendor.t(msg.content)
          }, {
            f: common_vendor.n(msg.type === "ai" ? "ai-message" : "user-message"),
            g: index
          });
        }),
        c: common_vendor.o(sendMessage),
        d: inputContent.value,
        e: common_vendor.o(($event) => inputContent.value = $event.detail.value),
        f: common_vendor.p({
          type: "camera",
          size: "20",
          color: "#666"
        }),
        g: common_vendor.o(openCamera),
        h: common_vendor.o(sendMessage)
      };
    };
  }
};
tt.createPage(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/ai-explore/ai-explore.js.map
