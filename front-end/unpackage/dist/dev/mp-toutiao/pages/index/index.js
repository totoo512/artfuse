"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const services_api_backendApi = require("../../services/api/backendApi.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  _easycom_uni_icons2();
}
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  _easycom_uni_icons();
}
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const activeTab = common_vendor.ref("image");
    const images = common_vendor.ref([]);
    const videos = common_vendor.ref([]);
    const isLoading = common_vendor.ref(false);
    const currentUserId = common_vendor.ref(null);
    const currentAssets = common_vendor.computed(() => {
      return activeTab.value === "image" ? images.value : videos.value;
    });
    async function loadUserInfo() {
      try {
        const userId = common_vendor.index.getStorageSync("userId") || 1001;
        currentUserId.value = userId;
        const userInfo = await services_api_backendApi.getUserInfo(userId);
        common_vendor.index.__f__("log", "at pages/index/index.vue:85", "用户信息:", userInfo);
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/index/index.vue:87", "获取用户信息失败:", error);
        currentUserId.value = 1001;
      }
    }
    async function loadUserCreations(type) {
      if (!currentUserId.value) {
        common_vendor.index.__f__("warn", "at pages/index/index.vue:96", "用户ID未获取，无法加载创作历史");
        return;
      }
      isLoading.value = true;
      try {
        const result = await services_api_backendApi.getUserCreations(currentUserId.value, type);
        common_vendor.index.__f__("log", "at pages/index/index.vue:103", `获取${type}创作历史:`, result);
        if (type === "image") {
          images.value = result.creations || result.images || [];
        } else if (type === "video") {
          videos.value = result.creations || result.videos || [];
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/index/index.vue:113", `获取${type}创作历史失败:`, error);
        if (type === "image") {
          images.value = [];
        } else {
          videos.value = [];
        }
        common_vendor.index.showToast({
          title: `加载${type === "image" ? "图片" : "视频"}失败`,
          icon: "none"
        });
      } finally {
        isLoading.value = false;
      }
    }
    function switchTab(tab) {
      activeTab.value = tab;
      loadUserCreations(tab);
    }
    async function initializePage() {
      await loadUserInfo();
      await loadUserCreations("image");
    }
    common_vendor.onMounted(() => {
      initializePage();
      common_vendor.index.$on("refreshCreations", () => {
        common_vendor.index.__f__("log", "at pages/index/index.vue:150", "收到刷新创作列表事件");
        loadUserCreations(activeTab.value);
      });
    });
    common_vendor.onUnmounted(() => {
      common_vendor.index.$off("refreshCreations");
    });
    function navigateToAIExplore() {
      common_vendor.index.navigateTo({
        url: "/pages/ai-explore/ai-explore",
        success: (res) => {
          common_vendor.index.__f__("log", "at pages/index/index.vue:172", "跳转AI Explore成功", res);
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at pages/index/index.vue:175", "跳转AI Explore失败", err);
          common_vendor.index.redirectTo({
            url: "/pages/ai-explore/ai-explore",
            success: () => common_vendor.index.__f__("log", "at pages/index/index.vue:179", "重定向AI Explore成功"),
            fail: (err2) => common_vendor.index.__f__("error", "at pages/index/index.vue:180", "重定向AI Explore也失败", err2)
          });
        }
      });
    }
    function navigateToUpload() {
      common_vendor.index.navigateTo({
        url: "/pages/upload/upload",
        success: (res) => {
          common_vendor.index.__f__("log", "at pages/index/index.vue:191", "跳转成功", res);
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at pages/index/index.vue:194", "跳转失败", err);
          common_vendor.index.redirectTo({
            url: "/pages/upload/upload",
            success: () => common_vendor.index.__f__("log", "at pages/index/index.vue:198", "重定向成功"),
            fail: (err2) => common_vendor.index.__f__("error", "at pages/index/index.vue:199", "重定向也失败", err2)
          });
        }
      });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_assets._imports_0,
        b: common_vendor.o(navigateToAIExplore),
        c: common_vendor.p({
          type: "arrow-right",
          size: "16",
          color: "#000000"
        }),
        d: common_vendor.o(navigateToUpload),
        e: activeTab.value === "image" ? 1 : "",
        f: common_vendor.o(($event) => switchTab("image")),
        g: activeTab.value === "video" ? 1 : "",
        h: common_vendor.o(($event) => switchTab("video")),
        i: isLoading.value
      }, isLoading.value ? {} : currentAssets.value.length === 0 ? {
        k: common_vendor.t(activeTab.value === "image" ? "图片" : "视频")
      } : {
        l: common_vendor.f(currentAssets.value, (item, index, i0) => {
          return common_vendor.e(activeTab.value === "image" ? {
            a: item.imageUrl
          } : {
            b: common_vendor.t(item.title || item.videoUrl)
          }, {
            c: index
          });
        }),
        m: activeTab.value === "image"
      }, {
        j: currentAssets.value.length === 0
      });
    };
  }
};
tt.createPage(_sfc_main);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/index/index.js.map
