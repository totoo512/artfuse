"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Math) {
  (uniIcons + uniSection)();
}
const uniSection = () => "../../uni_modules/uni-section/components/uni-section/uni-section.js";
const uniIcons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _sfc_main = {
  __name: "stylegen",
  setup(__props) {
    const popularStyles = common_vendor.ref([
      {
        name: "印象派",
        description: "莫奈风格",
        preview: "/static/c1.png"
      },
      {
        name: "立体主义",
        description: "毕加索风格",
        preview: "/static/c2.png"
      },
      {
        name: "浮世绘",
        description: "日式传统",
        preview: "/static/c3.png"
      },
      {
        name: "抽象表现",
        description: "现代艺术",
        preview: "/static/c4.png"
      }
    ]);
    const featuredWorks = common_vendor.ref([
      {
        image: "/static/c5.png",
        style: "印象派风格",
        likes: 128
      },
      {
        image: "/static/c6.png",
        style: "立体主义",
        likes: 95
      },
      {
        image: "/static/c7.png",
        style: "浮世绘风格",
        likes: 156
      },
      {
        image: "/static/c8.png",
        style: "抽象表现",
        likes: 203
      }
    ]);
    const guideSteps = common_vendor.ref([
      {
        title: "上传人物照片",
        description: "选择一张清晰的人物照片作为基础"
      },
      {
        title: "添加看展照片",
        description: "上传您在展览中拍摄的艺术作品照片"
      },
      {
        title: "AI风格化生成",
        description: "系统将自动生成多种艺术风格的图片"
      },
      {
        title: "选择并分享",
        description: "挑选喜欢的作品，生成文案并分享"
      }
    ]);
    const previewStyle = (style) => {
      common_vendor.index.showModal({
        title: style.name,
        content: `${style.description}

点击"开始创作"按钮体验这种风格的AI生成`,
        confirmText: "开始创作",
        cancelText: "取消",
        success: (res) => {
          if (res.confirm) {
            startCreation(style);
          }
        }
      });
    };
    const viewWork = (work) => {
      common_vendor.index.previewImage({
        urls: [work.image],
        current: 0
      });
    };
    const startCreation = (selectedStyle = null) => {
      const url = selectedStyle ? `/pages/creation/creation?style=${encodeURIComponent(JSON.stringify(selectedStyle))}` : "/pages/creation/creation";
      common_vendor.index.navigateTo({
        url,
        success: () => {
          common_vendor.index.showToast({
            title: "进入创作模式",
            icon: "success"
          });
        },
        fail: () => {
          common_vendor.index.showToast({
            title: "页面跳转失败，请稍后重试",
            icon: "none"
          });
        }
      });
    };
    const fabPosition = common_vendor.ref({
      bottom: 120,
      // 向上调整位置，从40rpx改为120rpx
      right: 30
    });
    const dragState = common_vendor.ref({
      isDragging: false,
      startX: 0,
      startY: 0,
      startBottom: 0,
      startRight: 0
    });
    const lastTapTime = common_vendor.ref(0);
    const initialPosition = { bottom: 120, right: 30 };
    const onFabTouchStart = (e) => {
      const touch = e.touches[0];
      dragState.value = {
        isDragging: true,
        startX: touch.clientX,
        startY: touch.clientY,
        startBottom: fabPosition.value.bottom,
        startRight: fabPosition.value.right
      };
    };
    const onFabTouchMove = (e) => {
      if (!dragState.value.isDragging)
        return;
      e.preventDefault();
      const touch = e.touches[0];
      const deltaX = touch.clientX - dragState.value.startX;
      const deltaY = touch.clientY - dragState.value.startY;
      const systemInfo = common_vendor.index.getSystemInfoSync();
      const screenWidth = systemInfo.windowWidth;
      const screenHeight = systemInfo.windowHeight;
      const deltaXRpx = deltaX / screenWidth * 750;
      const deltaYRpx = deltaY / screenHeight * 750;
      let newRight = dragState.value.startRight - deltaXRpx;
      let newBottom = dragState.value.startBottom - deltaYRpx;
      const buttonSize = 120;
      const margin = 10;
      newRight = Math.max(margin, Math.min(newRight, 750 - buttonSize - margin));
      newBottom = Math.max(margin, Math.min(newBottom, screenHeight / screenWidth * 750 - buttonSize - margin));
      fabPosition.value.right = newRight;
      fabPosition.value.bottom = newBottom;
    };
    const onFabTouchEnd = () => {
      const currentTime = Date.now();
      if (currentTime - lastTapTime.value < 300) {
        fabPosition.value.bottom = initialPosition.bottom;
        fabPosition.value.right = initialPosition.right;
        common_vendor.index.showToast({
          title: "按钮已重置到初始位置",
          icon: "success",
          duration: 1500
        });
      }
      lastTapTime.value = currentTime;
      dragState.value.isDragging = false;
    };
    common_vendor.onMounted(() => {
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(popularStyles.value, (style, index, i0) => {
          return {
            a: style.preview,
            b: common_vendor.t(style.name),
            c: common_vendor.t(style.description),
            d: index,
            e: common_vendor.o(($event) => previewStyle(style))
          };
        }),
        b: common_vendor.f(featuredWorks.value, (work, index, i0) => {
          return {
            a: work.image,
            b: common_vendor.t(work.style),
            c: "1c568d9b-1-" + i0 + ",1c568d9b-0",
            d: common_vendor.t(work.likes),
            e: index,
            f: common_vendor.o(($event) => viewWork(work))
          };
        }),
        c: common_vendor.p({
          type: "heart",
          size: "12",
          color: "#fff"
        }),
        d: common_vendor.f(guideSteps.value, (step, index, i0) => {
          return {
            a: common_vendor.t(index + 1),
            b: common_vendor.t(step.title),
            c: common_vendor.t(step.description),
            d: index
          };
        }),
        e: common_vendor.p({
          title: "精选推荐",
          type: "line"
        }),
        f: common_vendor.p({
          type: "plus",
          size: "24",
          color: "#fff"
        }),
        g: common_vendor.o(startCreation),
        h: fabPosition.value.bottom + "rpx",
        i: fabPosition.value.right + "rpx",
        j: common_vendor.o(onFabTouchStart),
        k: common_vendor.o(onFabTouchMove),
        l: common_vendor.o(onFabTouchEnd)
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1c568d9b"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/stylegen/stylegen.js.map
