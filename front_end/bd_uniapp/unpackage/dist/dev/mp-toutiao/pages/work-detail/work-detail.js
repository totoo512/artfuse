"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_nav_bar2 = common_vendor.resolveComponent("uni-nav-bar");
  const _easycom_uni_section2 = common_vendor.resolveComponent("uni-section");
  (_easycom_uni_icons2 + _easycom_uni_nav_bar2 + _easycom_uni_section2)();
}
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_nav_bar = () => "../../uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.js";
const _easycom_uni_section = () => "../../uni_modules/uni-section/components/uni-section/uni-section.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uni_nav_bar + _easycom_uni_section)();
}
const _sfc_main = {
  __name: "work-detail",
  setup(__props) {
    const workId = common_vendor.ref("");
    const isLiked = common_vendor.ref(false);
    const workDetail = common_vendor.reactive({
      id: 1,
      title: "印象派风格人像",
      type: "image",
      style: "印象派",
      originalUrl: "/static/c1.png",
      thumbnail: "/static/c1.png",
      resolution: "1024x1024",
      createTime: "2024-01-20T10:30:00",
      likes: 128,
      views: 456,
      shares: 45,
      description: "基于莫奈风格生成的人物肖像，展现了印象派独特的光影效果和色彩运用。",
      prompt: "portrait of a young woman in impressionist style, soft brushstrokes, natural lighting, vibrant colors",
      negativePrompt: "blurry, low quality, distorted",
      model: "AIGC-v2.0",
      steps: "50",
      guidance: "7.5",
      variations: [
        { url: "/static/c1.png", label: "变体1" },
        { url: "/static/c2.png", label: "变体2" },
        { url: "/static/c3.png", label: "变体3" }
      ],
      sourceImages: [
        { url: "/static/avatar.png", label: "人物照片" },
        { url: "/static/c5.png", label: "展品照片" }
      ]
    });
    const goBack = () => {
      common_vendor.index.navigateBack();
    };
    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日 ${date.getHours().toString().padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
    };
    const toggleLike = () => {
      isLiked.value = !isLiked.value;
      if (isLiked.value) {
        workDetail.likes++;
        common_vendor.index.showToast({
          title: "已点赞",
          icon: "success",
          duration: 1e3
        });
      } else {
        workDetail.likes--;
        common_vendor.index.showToast({
          title: "已取消点赞",
          icon: "none",
          duration: 1e3
        });
      }
    };
    const previewImage = (imageUrl) => {
      const urls = workDetail.variations ? workDetail.variations.map((v) => v.url) : [imageUrl];
      common_vendor.index.previewImage({
        current: imageUrl,
        urls
      });
    };
    const shareWork = () => {
      common_vendor.index.share({
        provider: "weixin",
        scene: "WXSceneSession",
        type: 0,
        href: `pages/work-detail/work-detail?workId=${workDetail.id}`,
        title: workDetail.title,
        summary: workDetail.description,
        imageUrl: workDetail.thumbnail,
        success: () => {
          common_vendor.index.showToast({
            title: "分享成功",
            icon: "success"
          });
          workDetail.shares++;
        },
        fail: () => {
          common_vendor.index.showToast({
            title: "分享失败",
            icon: "none"
          });
        }
      });
    };
    const downloadWork = () => {
      common_vendor.index.showLoading({
        title: "下载中..."
      });
      setTimeout(() => {
        common_vendor.index.hideLoading();
        common_vendor.index.showToast({
          title: "下载完成",
          icon: "success"
        });
      }, 2e3);
    };
    const regenerateWork = () => {
      common_vendor.index.showModal({
        title: "重新生成",
        content: "是否使用相同参数重新生成作品？",
        success: (res) => {
          if (res.confirm) {
            common_vendor.index.navigateTo({
              url: `/pages/creation/creation?regenerate=true&workId=${workDetail.id}`
            });
          }
        }
      });
    };
    const useAsReference = () => {
      common_vendor.index.navigateTo({
        url: `/pages/creation/creation?reference=${workDetail.id}`
      });
    };
    const saveToAlbum = () => {
      common_vendor.index.saveImageToPhotosAlbum({
        filePath: workDetail.originalUrl,
        success: () => {
          common_vendor.index.showToast({
            title: "保存成功",
            icon: "success"
          });
        },
        fail: () => {
          common_vendor.index.showToast({
            title: "保存失败",
            icon: "none"
          });
        }
      });
    };
    common_vendor.onMounted(() => {
      const pages = getCurrentPages();
      const currentPage = pages[pages.length - 1];
      workId.value = currentPage.options.workId || "1";
      common_vendor.index.__f__("log", "at pages/work-detail/work-detail.vue:330", "加载作品详情:", workId.value);
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          type: "redo",
          size: "18",
          color: "#fff"
        }),
        b: common_vendor.o(shareWork),
        c: common_vendor.p({
          type: "download",
          size: "18",
          color: "#fff"
        }),
        d: common_vendor.o(downloadWork),
        e: common_vendor.o(goBack),
        f: common_vendor.p({
          title: "作品详情",
          border: false,
          ["background-color"]: "#000000",
          color: "#ffffff",
          ["left-icon"]: "back"
        }),
        g: workDetail.type === "image" && workDetail.variations && workDetail.variations.length > 1
      }, workDetail.type === "image" && workDetail.variations && workDetail.variations.length > 1 ? {
        h: common_vendor.f(workDetail.variations, (variation, index, i0) => {
          return {
            a: variation.url,
            b: common_vendor.o(($event) => previewImage(variation.url)),
            c: index
          };
        })
      } : workDetail.type === "image" ? {
        j: workDetail.originalUrl,
        k: common_vendor.o(($event) => previewImage(workDetail.originalUrl))
      } : workDetail.type === "video" ? {
        m: workDetail.videoUrl,
        n: workDetail.thumbnail
      } : {}, {
        i: workDetail.type === "image",
        l: workDetail.type === "video",
        o: common_vendor.t(workDetail.title),
        p: common_vendor.p({
          type: isLiked.value ? "heart-filled" : "heart",
          size: "20",
          color: isLiked.value ? "#ff4757" : "#666"
        }),
        q: common_vendor.t(workDetail.likes),
        r: common_vendor.o(toggleLike),
        s: common_vendor.p({
          type: "eye",
          size: "20",
          color: "#666"
        }),
        t: common_vendor.t(workDetail.views),
        v: common_vendor.t(formatDate(workDetail.createTime)),
        w: common_vendor.t(workDetail.style),
        x: workDetail.resolution
      }, workDetail.resolution ? {
        y: common_vendor.t(workDetail.resolution)
      } : {}, {
        z: workDetail.duration
      }, workDetail.duration ? {
        A: common_vendor.t(workDetail.duration)
      } : {}, {
        B: workDetail.description
      }, workDetail.description ? {
        C: common_vendor.t(workDetail.description)
      } : {}, {
        D: common_vendor.t(workDetail.prompt || "无"),
        E: common_vendor.t(workDetail.negativePrompt || "无"),
        F: common_vendor.t(workDetail.model || "AIGC-v1.0"),
        G: common_vendor.t(workDetail.steps || "50"),
        H: common_vendor.t(workDetail.guidance || "7.5"),
        I: common_vendor.p({
          title: "生成参数",
          type: "line"
        }),
        J: workDetail.sourceImages && workDetail.sourceImages.length > 0
      }, workDetail.sourceImages && workDetail.sourceImages.length > 0 ? {
        K: common_vendor.f(workDetail.sourceImages, (source, index, i0) => {
          return {
            a: source.url,
            b: common_vendor.t(source.label),
            c: index,
            d: common_vendor.o(($event) => previewImage(source.url))
          };
        }),
        L: common_vendor.p({
          title: "原始素材",
          type: "line"
        })
      } : {}, {
        M: common_vendor.p({
          type: "refresh",
          size: "18",
          color: "#007AFF"
        }),
        N: common_vendor.o(regenerateWork),
        O: common_vendor.p({
          type: "color",
          size: "18",
          color: "#007AFF"
        }),
        P: common_vendor.o(useAsReference),
        Q: common_vendor.p({
          type: "download",
          size: "18",
          color: "#fff"
        }),
        R: common_vendor.o(saveToAlbum)
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-0671e18f"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/work-detail/work-detail.js.map
