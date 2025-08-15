"use strict";
const common_vendor = require("../common/vendor.js");
const services_api_backendApi = require("../services/api/backendApi.js");
const _sfc_main = {
  __name: "ImageGenerator",
  setup(__props) {
    const personalImage = common_vendor.ref("");
    const exhibitionImage = common_vendor.ref("");
    const sizes = ["adaptive"];
    const selectedSize = common_vendor.ref("adaptive");
    const guidanceScale = common_vendor.ref(5.5);
    const isGenerating = common_vendor.ref(false);
    const resultImages = common_vendor.ref([]);
    const errorMessage = common_vendor.ref("");
    function choosePersonalImage() {
      common_vendor.index.chooseImage({
        count: 1,
        success: (res) => {
          personalImage.value = res.tempFilePaths[0];
          errorMessage.value = "";
        },
        fail: (err) => {
          errorMessage.value = `选择个人图像失败: ${err.errMsg}`;
        }
      });
    }
    function chooseExhibitionImage() {
      common_vendor.index.chooseImage({
        count: 1,
        success: (res) => {
          exhibitionImage.value = res.tempFilePaths[0];
          errorMessage.value = "";
        },
        fail: (err) => {
          errorMessage.value = `选择展览图像失败: ${err.errMsg}`;
        }
      });
    }
    function onSizeChange(e) {
      selectedSize.value = sizes[e.detail.value];
    }
    function onGuidanceScaleChange(e) {
      guidanceScale.value = e.detail.value;
    }
    async function generateImage() {
      if (!personalImage.value || !exhibitionImage.value) {
        errorMessage.value = "请先上传个人图像和展览图像";
        return;
      }
      isGenerating.value = true;
      errorMessage.value = "";
      resultImages.value = [];
      try {
        const uploadResult = await services_api_backendApi.uploadImages(personalImage.value, [exhibitionImage.value]);
        common_vendor.index.__f__("log", "at components/ImageGenerator.vue:131", "图片上传成功:", uploadResult);
        const analysisResult = await services_api_backendApi.multimodalAnalysis(
          uploadResult.portraitImageId,
          uploadResult.exhibitionImageIds
        );
        common_vendor.index.__f__("log", "at components/ImageGenerator.vue:139", "多模态分析成功:", analysisResult);
        const generateResult = await services_api_backendApi.generateImages(analysisResult.analysisId);
        common_vendor.index.__f__("log", "at components/ImageGenerator.vue:144", "图片生成成功:", generateResult);
        if (generateResult.images && generateResult.images.length > 0) {
          resultImages.value = generateResult.images.map((img) => img.imageUrl);
        } else {
          throw new Error("未获取到生成的图片");
        }
      } catch (error) {
        errorMessage.value = `生成失败: ${error.message}`;
        common_vendor.index.__f__("error", "at components/ImageGenerator.vue:155", "图像生成错误:", error);
      } finally {
        isGenerating.value = false;
      }
    }
    function saveImage(index) {
      if (!resultImages.value[index])
        return;
      const imageUrl = resultImages.value[index];
      common_vendor.index.saveImageToPhotosAlbum({
        filePath: imageUrl,
        success: () => {
          common_vendor.index.showToast({
            title: "保存成功",
            icon: "success"
          });
          saveImageToCloud(imageUrl);
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at components/ImageGenerator.vue:183", "保存失败:", err);
          common_vendor.index.showToast({
            title: "保存失败",
            icon: "none"
          });
        }
      });
    }
    async function saveImageToCloud(imageUrl) {
      try {
        const userId = common_vendor.index.getStorageSync("userId") || 1001;
        const result = await saveImageToCloud(imageUrl, userId);
        common_vendor.index.__f__("log", "at components/ImageGenerator.vue:199", "图片已保存到云端:", result);
        common_vendor.index.$emit("refreshCreations");
      } catch (error) {
        common_vendor.index.__f__("error", "at components/ImageGenerator.vue:206", "保存到云端失败:", error);
      }
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: personalImage.value
      }, personalImage.value ? {
        b: personalImage.value
      } : {}, {
        c: common_vendor.o(choosePersonalImage),
        d: exhibitionImage.value
      }, exhibitionImage.value ? {
        e: exhibitionImage.value
      } : {}, {
        f: common_vendor.o(chooseExhibitionImage),
        g: common_vendor.t(selectedSize.value),
        h: common_vendor.o(onSizeChange),
        i: sizes,
        j: guidanceScale.value,
        k: common_vendor.o(onGuidanceScaleChange),
        l: common_vendor.t(guidanceScale.value),
        m: common_vendor.t(isGenerating.value ? "生成中..." : "立即生成"),
        n: isGenerating.value,
        o: common_vendor.o(generateImage),
        p: resultImages.value.length > 0
      }, resultImages.value.length > 0 ? {
        q: common_vendor.f(resultImages.value, (imageUrl, index, i0) => {
          return {
            a: imageUrl,
            b: common_vendor.t(index + 1),
            c: common_vendor.o(($event) => saveImage(index)),
            d: index
          };
        })
      } : {}, {
        r: errorMessage.value
      }, errorMessage.value ? {
        s: common_vendor.t(errorMessage.value)
      } : {});
    };
  }
};
tt.createComponent(_sfc_main);
//# sourceMappingURL=../../.sourcemap/mp-toutiao/components/ImageGenerator.js.map
