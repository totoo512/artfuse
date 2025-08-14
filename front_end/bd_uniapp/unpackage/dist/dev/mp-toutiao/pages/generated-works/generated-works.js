"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_nav_bar2 = common_vendor.resolveComponent("uni-nav-bar");
  const _easycom_uni_badge2 = common_vendor.resolveComponent("uni-badge");
  const _easycom_uni_popup2 = common_vendor.resolveComponent("uni-popup");
  (_easycom_uni_icons2 + _easycom_uni_nav_bar2 + _easycom_uni_badge2 + _easycom_uni_popup2)();
}
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_nav_bar = () => "../../uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.js";
const _easycom_uni_badge = () => "../../uni_modules/uni-badge/components/uni-badge/uni-badge.js";
const _easycom_uni_popup = () => "../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
if (!Math) {
  (_easycom_uni_icons + _easycom_uni_nav_bar + _easycom_uni_badge + _easycom_uni_popup)();
}
const _sfc_main = {
  __name: "generated-works",
  setup(__props) {
    const currentFilter = common_vendor.ref("all");
    const filterPopup = common_vendor.ref(null);
    const filterTabs = [
      { key: "all", label: "全部", count: 24 },
      { key: "image", label: "图片", count: 18 },
      { key: "video", label: "视频", count: 6 },
      { key: "recent", label: "最近", count: 8 }
    ];
    const filterOptions = common_vendor.reactive({
      type: "all",
      style: "all"
    });
    const typeOptions = [
      { key: "all", label: "全部类型" },
      { key: "image", label: "图片" },
      { key: "video", label: "视频" }
    ];
    const styleOptions = [
      { key: "all", label: "全部风格" },
      { key: "impressionism", label: "印象派" },
      { key: "abstract", label: "抽象派" },
      { key: "classical", label: "古典主义" },
      { key: "modern", label: "现代艺术" },
      { key: "chinese", label: "中国风" }
    ];
    const works = common_vendor.reactive([
      {
        id: 1,
        title: "印象派风格人像",
        type: "image",
        style: "印象派",
        thumbnail: "/static/c1.png",
        originalUrl: "/static/c1.png",
        createTime: "2024-01-20T10:30:00",
        likes: 128,
        shares: 45,
        description: "基于莫奈风格生成的人物肖像"
      },
      {
        id: 2,
        title: "抽象艺术视频",
        type: "video",
        style: "抽象派",
        thumbnail: "/static/c2.png",
        videoUrl: "/static/sample-video.mp4",
        duration: "00:15",
        createTime: "2024-01-19T15:20:00",
        likes: 89,
        shares: 23,
        description: "康定斯基风格的动态艺术视频"
      },
      {
        id: 3,
        title: "古典主义肖像",
        type: "image",
        style: "古典主义",
        thumbnail: "/static/c3.png",
        originalUrl: "/static/c3.png",
        createTime: "2024-01-18T09:15:00",
        likes: 156,
        shares: 67,
        description: "文艺复兴时期风格的人物画像"
      },
      {
        id: 4,
        title: "现代艺术拼贴",
        type: "image",
        style: "现代艺术",
        thumbnail: "/static/c4.png",
        originalUrl: "/static/c4.png",
        createTime: "2024-01-17T14:45:00",
        likes: 92,
        shares: 34,
        description: "毕加索立体主义风格创作"
      },
      {
        id: 5,
        title: "中国风水墨画",
        type: "image",
        style: "中国风",
        thumbnail: "/static/c5.png",
        originalUrl: "/static/c5.png",
        createTime: "2024-01-16T11:30:00",
        likes: 203,
        shares: 89,
        description: "传统水墨画风格人物肖像"
      },
      {
        id: 6,
        title: "梦幻风格视频",
        type: "video",
        style: "印象派",
        thumbnail: "/static/c6.png",
        videoUrl: "/static/sample-video2.mp4",
        duration: "00:20",
        createTime: "2024-01-15T16:00:00",
        likes: 145,
        shares: 56,
        description: "梦幻色彩的动态艺术作品"
      }
    ]);
    const filteredWorks = common_vendor.computed(() => {
      let filtered = works;
      if (currentFilter.value === "image") {
        filtered = filtered.filter((work) => work.type === "image");
      } else if (currentFilter.value === "video") {
        filtered = filtered.filter((work) => work.type === "video");
      } else if (currentFilter.value === "recent") {
        const recentDate = /* @__PURE__ */ new Date();
        recentDate.setDate(recentDate.getDate() - 7);
        filtered = filtered.filter((work) => new Date(work.createTime) > recentDate);
      }
      if (filterOptions.type !== "all") {
        filtered = filtered.filter((work) => work.type === filterOptions.type);
      }
      if (filterOptions.style !== "all") {
        filtered = filtered.filter((work) => work.style === filterOptions.style);
      }
      return filtered;
    });
    const goBack = () => {
      common_vendor.index.navigateBack();
    };
    const switchFilter = (filterKey) => {
      currentFilter.value = filterKey;
    };
    const showFilterPopup = () => {
      filterPopup.value.open();
    };
    const closeFilterPopup = () => {
      filterPopup.value.close();
    };
    const resetFilter = () => {
      filterOptions.type = "all";
      filterOptions.style = "all";
    };
    const applyFilter = () => {
      closeFilterPopup();
      if (filterOptions.type !== "all" || filterOptions.style !== "all") {
        currentFilter.value = "custom";
      }
    };
    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      const now = /* @__PURE__ */ new Date();
      const diff = now - date;
      const days = Math.floor(diff / (1e3 * 60 * 60 * 24));
      if (days === 0) {
        return "今天";
      } else if (days === 1) {
        return "昨天";
      } else if (days < 7) {
        return `${days}天前`;
      } else {
        return `${date.getMonth() + 1}月${date.getDate()}日`;
      }
    };
    const viewWork = (work) => {
      common_vendor.index.navigateTo({
        url: `/pages/work-detail/work-detail?workId=${work.id}`
      });
    };
    const goToCreation = () => {
      common_vendor.index.switchTab({
        url: "/pages/creation/creation"
      });
    };
    common_vendor.onMounted(() => {
      common_vendor.index.__f__("log", "at pages/generated-works/generated-works.vue:357", "生成作品页面加载完成");
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.p({
          type: "funnel",
          size: "16",
          color: "#007AFF"
        }),
        b: common_vendor.o(showFilterPopup),
        c: common_vendor.o(goBack),
        d: common_vendor.p({
          title: "生成作品",
          border: false,
          ["background-color"]: "#ffffff",
          color: "#333333",
          ["left-icon"]: "back"
        }),
        e: common_vendor.f(filterTabs, (tab, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(tab.label),
            b: tab.count
          }, tab.count ? {
            c: "c4c9a8e3-2-" + i0,
            d: common_vendor.p({
              text: tab.count,
              type: "primary",
              size: "small"
            })
          } : {}, {
            e: tab.key,
            f: currentFilter.value === tab.key ? 1 : "",
            g: common_vendor.o(($event) => switchFilter(tab.key))
          });
        }),
        f: common_vendor.f(filteredWorks.value, (work, k0, i0) => {
          return common_vendor.e({
            a: work.type === "image"
          }, work.type === "image" ? {
            b: work.thumbnail
          } : work.type === "video" ? {
            d: work.videoUrl,
            e: work.thumbnail
          } : {}, {
            c: work.type === "video",
            f: "c4c9a8e3-3-" + i0,
            g: common_vendor.p({
              type: work.type === "video" ? "videocam" : "image",
              size: "14",
              color: "#fff"
            }),
            h: work.type === "video"
          }, work.type === "video" ? {
            i: common_vendor.t(work.duration)
          } : {}, {
            j: common_vendor.t(work.title),
            k: common_vendor.t(work.style),
            l: common_vendor.t(formatDate(work.createTime)),
            m: "c4c9a8e3-4-" + i0,
            n: common_vendor.t(work.likes),
            o: "c4c9a8e3-5-" + i0,
            p: common_vendor.t(work.shares),
            q: work.id,
            r: common_vendor.o(($event) => viewWork(work))
          });
        }),
        g: common_vendor.p({
          type: "heart",
          size: "12",
          color: "#ff4757"
        }),
        h: common_vendor.p({
          type: "redo",
          size: "12",
          color: "#007AFF"
        }),
        i: filteredWorks.value.length === 0
      }, filteredWorks.value.length === 0 ? {
        j: common_vendor.p({
          type: "color",
          size: "60",
          color: "#ccc"
        }),
        k: common_vendor.o(goToCreation)
      } : {}, {
        l: common_vendor.p({
          type: "close",
          size: "20",
          color: "#666"
        }),
        m: common_vendor.o(closeFilterPopup),
        n: common_vendor.f(typeOptions, (type, k0, i0) => {
          return {
            a: common_vendor.t(type.label),
            b: type.key,
            c: filterOptions.type === type.key ? 1 : "",
            d: common_vendor.o(($event) => filterOptions.type = type.key)
          };
        }),
        o: common_vendor.f(styleOptions, (style, k0, i0) => {
          return {
            a: common_vendor.t(style.label),
            b: style.key,
            c: filterOptions.style === style.key ? 1 : "",
            d: common_vendor.o(($event) => filterOptions.style = style.key)
          };
        }),
        p: common_vendor.o(resetFilter),
        q: common_vendor.o(applyFilter),
        r: common_vendor.sr(filterPopup, "c4c9a8e3-7", {
          "k": "filterPopup"
        }),
        s: common_vendor.p({
          type: "bottom"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c4c9a8e3"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/generated-works/generated-works.js.map
