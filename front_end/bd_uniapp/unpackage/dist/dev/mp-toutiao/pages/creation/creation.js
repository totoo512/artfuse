"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Math) {
  (uniIcons + uniSection + uniCol + uniRow + uniLoadMore + uniPopup)();
}
const uniSection = () => "../../uni_modules/uni-section/components/uni-section/uni-section.js";
const uniIcons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const uniRow = () => "../../uni_modules/uni-row/components/uni-row/uni-row.js";
const uniCol = () => "../../uni_modules/uni-row/components/uni-col/uni-col.js";
const uniPopup = () => "../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
const uniLoadMore = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _sfc_main = {
  __name: "creation",
  setup(__props) {
    const portraitImage = common_vendor.ref("");
    const exhibitionImages = common_vendor.ref([]);
    const generatedImages = common_vendor.ref([]);
    const selectedImageIndex = common_vendor.ref(-1);
    const isGenerating = common_vendor.ref(false);
    const enableVideoGeneration = common_vendor.ref(false);
    const loadingText = common_vendor.ref("");
    const loadingPopup = common_vendor.ref(null);
    const selectedStyle = common_vendor.ref(null);
    const generatedCaptions = common_vendor.ref([
      "今天在艺术的海洋里遇见了另一个自己 ✨ #艺术风格化 #AIGC",
      "当我遇上经典艺术，原来我也可以这么美 🎨 #风格化生成 #艺术创作",
      "穿越时空，与大师作品融为一体的奇妙体验 🌟 #AI艺术 #创意无限"
    ]);
    const canGenerate = common_vendor.computed(() => {
      return portraitImage.value && exhibitionImages.value.length > 0;
    });
    common_vendor.onLoad((options) => {
      if (options.style) {
        try {
          selectedStyle.value = JSON.parse(decodeURIComponent(options.style));
          common_vendor.index.showToast({
            title: `已选择${selectedStyle.value.name}风格`,
            icon: "success"
          });
        } catch (e) {
          common_vendor.index.__f__("error", "at pages/creation/creation.vue:481", "解析风格参数失败:", e);
        }
      }
    });
    const uploadPortrait = () => {
      common_vendor.index.chooseImage({
        count: 1,
        sizeType: ["compressed"],
        sourceType: ["camera", "album"],
        success: (res) => {
          portraitImage.value = res.tempFilePaths[0];
          common_vendor.index.showToast({
            title: "上传成功",
            icon: "success"
          });
        }
      });
    };
    const uploadExhibitionImages = () => {
      const remainingCount = 9 - exhibitionImages.value.length;
      common_vendor.index.chooseImage({
        count: remainingCount,
        sizeType: ["compressed"],
        sourceType: ["camera", "album"],
        success: (res) => {
          exhibitionImages.value.push(...res.tempFilePaths);
          common_vendor.index.showToast({
            title: "选择成功",
            icon: "success"
          });
        }
      });
    };
    const openMyAlbum = () => {
      common_vendor.index.navigateTo({
        url: "/pages/album/album"
      });
    };
    const previewImage = (imageUrl, index) => {
      common_vendor.index.previewImage({
        urls: exhibitionImages.value,
        current: index
      });
    };
    const removeExhibitionImage = (index) => {
      exhibitionImages.value.splice(index, 1);
    };
    const generateImages = () => {
      var _a;
      if (!canGenerate.value)
        return;
      isGenerating.value = true;
      loadingText.value = "AI正在为您生成风格化图片...";
      (_a = loadingPopup.value) == null ? void 0 : _a.open();
      setTimeout(() => {
        loadingText.value = "正在分析人物特征...";
        setTimeout(() => {
          loadingText.value = "正在应用艺术风格...";
          setTimeout(() => {
            loadingText.value = "正在优化图片质量...";
            setTimeout(() => {
              var _a2;
              generatedImages.value = [
                "/static/c1.png",
                "/static/c2.png",
                "/static/c3.png",
                "/static/c4.png"
              ];
              isGenerating.value = false;
              (_a2 = loadingPopup.value) == null ? void 0 : _a2.close();
              common_vendor.index.showToast({
                title: "生成完成！",
                icon: "success"
              });
            }, 1500);
          }, 1500);
        }, 1500);
      }, 1500);
    };
    const selectImage = (index) => {
      selectedImageIndex.value = index;
      common_vendor.index.showToast({
        title: "已选择图片",
        icon: "success"
      });
    };
    const toggleVideoGeneration = (e) => {
      enableVideoGeneration.value = e.detail.value;
      if (e.detail.value) {
        common_vendor.index.showModal({
          title: "视频生成提示",
          content: '视频生成需要较长时间排队，预计5-10分钟，完成后将通知您，您可稍后在"我的"页面查看。',
          confirmText: "确定",
          cancelText: "取消",
          success: (res) => {
            if (res.confirm) {
              common_vendor.index.showToast({
                title: "已加入生成队列",
                icon: "success"
              });
            } else {
              enableVideoGeneration.value = false;
            }
          }
        });
      }
    };
    const selectCaption = (index) => {
    };
    const copyCaption = (caption) => {
      common_vendor.index.setClipboardData({
        data: caption,
        success: () => {
          common_vendor.index.showToast({
            title: "文案已复制",
            icon: "success"
          });
        }
      });
    };
    const saveImage = () => {
      if (selectedImageIndex.value === -1)
        return;
      const imageUrl = generatedImages.value[selectedImageIndex.value];
      common_vendor.index.downloadFile({
        url: imageUrl,
        success: (res) => {
          common_vendor.index.saveImageToPhotosAlbum({
            filePath: res.tempFilePath,
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
        }
      });
    };
    const shareToSocial = () => {
      if (selectedImageIndex.value === -1)
        return;
      common_vendor.index.share({
        provider: "weixin",
        scene: "WXSceneTimeline",
        type: 1,
        imageUrl: generatedImages.value[selectedImageIndex.value],
        success: () => {
          common_vendor.index.showToast({
            title: "分享成功",
            icon: "success"
          });
        },
        fail: () => {
          common_vendor.index.showToast({
            title: "分享失败",
            icon: "none"
          });
        }
      });
    };
    common_vendor.onMounted(() => {
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !portraitImage.value
      }, !portraitImage.value ? {
        b: common_vendor.p({
          type: "camera",
          size: "40",
          color: "#999"
        })
      } : {
        c: portraitImage.value
      }, {
        d: common_vendor.o(uploadPortrait),
        e: common_vendor.t(exhibitionImages.value.length),
        f: common_vendor.f(exhibitionImages.value, (image, index, i0) => {
          return {
            a: image,
            b: "4ffa4d58-2-" + i0 + ",4ffa4d58-0",
            c: common_vendor.o(($event) => removeExhibitionImage(index)),
            d: index,
            e: common_vendor.o(($event) => previewImage(image, index))
          };
        }),
        g: common_vendor.p({
          type: "close",
          size: "16",
          color: "#fff"
        }),
        h: exhibitionImages.value.length < 9
      }, exhibitionImages.value.length < 9 ? {
        i: common_vendor.p({
          type: "plus",
          size: "30",
          color: "#999"
        }),
        j: common_vendor.o(uploadExhibitionImages)
      } : {}, {
        k: common_vendor.p({
          type: "info",
          size: "16",
          color: "#007aff"
        }),
        l: common_vendor.o(openMyAlbum),
        m: common_vendor.p({
          title: "上传素材",
          type: "line"
        }),
        n: common_vendor.t(isGenerating.value ? "生成中..." : "开始生成风格化图片"),
        o: !canGenerate.value,
        p: common_vendor.o(generateImages),
        q: isGenerating.value ? 1 : "",
        r: generatedImages.value.length > 0
      }, generatedImages.value.length > 0 ? common_vendor.e({
        s: common_vendor.f(generatedImages.value, (image, index, i0) => {
          return common_vendor.e({
            a: image,
            b: selectedImageIndex.value === index
          }, selectedImageIndex.value === index ? {
            c: "4ffa4d58-6-" + i0 + ",4ffa4d58-5",
            d: common_vendor.p({
              type: "checkmarkempty",
              size: "20",
              color: "#fff"
            })
          } : {}, {
            e: index,
            f: selectedImageIndex.value === index ? 1 : "",
            g: common_vendor.o(($event) => selectImage(index))
          });
        }),
        t: selectedImageIndex.value !== -1
      }, selectedImageIndex.value !== -1 ? common_vendor.e({
        v: enableVideoGeneration.value,
        w: common_vendor.o(toggleVideoGeneration),
        x: enableVideoGeneration.value
      }, enableVideoGeneration.value ? {
        y: common_vendor.p({
          type: "info",
          size: "14",
          color: "#ff9500"
        })
      } : {}) : {}, {
        z: common_vendor.p({
          title: "生成结果",
          type: "line"
        })
      }) : {}, {
        A: selectedImageIndex.value !== -1
      }, selectedImageIndex.value !== -1 ? {
        B: common_vendor.f(generatedCaptions.value, (caption, index, i0) => {
          return {
            a: common_vendor.t(caption),
            b: common_vendor.o(($event) => copyCaption(caption)),
            c: index,
            d: common_vendor.o(($event) => selectCaption())
          };
        }),
        C: common_vendor.p({
          title: "智能文案",
          type: "line"
        })
      } : {}, {
        D: selectedImageIndex.value !== -1
      }, selectedImageIndex.value !== -1 ? {
        E: common_vendor.o(saveImage),
        F: common_vendor.p({
          span: 12
        }),
        G: common_vendor.o(shareToSocial),
        H: common_vendor.p({
          span: 12
        }),
        I: common_vendor.p({
          gutter: 10
        })
      } : {}, {
        J: common_vendor.p({
          status: "loading",
          ["content-text"]: {
            contentdown: loadingText.value
          }
        }),
        K: common_vendor.sr(loadingPopup, "4ffa4d58-12", {
          "k": "loadingPopup"
        }),
        L: common_vendor.p({
          type: "center"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-4ffa4d58"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/creation/creation.js.map
