"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Array) {
  const _easycom_uni_nav_bar2 = common_vendor.resolveComponent("uni-nav-bar");
  const _easycom_uni_search_bar2 = common_vendor.resolveComponent("uni-search-bar");
  const _easycom_uni_badge2 = common_vendor.resolveComponent("uni-badge");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  (_easycom_uni_nav_bar2 + _easycom_uni_search_bar2 + _easycom_uni_badge2 + _easycom_uni_icons2)();
}
const _easycom_uni_nav_bar = () => "../../uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.js";
const _easycom_uni_search_bar = () => "../../uni_modules/uni-search-bar/components/uni-search-bar/uni-search-bar.js";
const _easycom_uni_badge = () => "../../uni_modules/uni-badge/components/uni-badge/uni-badge.js";
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  (_easycom_uni_nav_bar + _easycom_uni_search_bar + _easycom_uni_badge + _easycom_uni_icons)();
}
const _sfc_main = {
  __name: "exhibition-album",
  setup(__props) {
    const searchKeyword = common_vendor.ref("");
    const exhibitionSessions = common_vendor.reactive([
      {
        id: 1,
        title: "印象派大师展",
        date: "2024-01-15",
        location: "中国美术馆",
        photoCount: 12,
        photos: [
          { id: 1, thumbnail: "/static/c1.png", original: "/static/c1.png", title: "莫奈-睡莲" },
          { id: 2, thumbnail: "/static/c2.png", original: "/static/c2.png", title: "梵高-星空" },
          { id: 3, thumbnail: "/static/c3.png", original: "/static/c3.png", title: "雷诺阿-舞会" },
          { id: 4, thumbnail: "/static/c4.png", original: "/static/c4.png", title: "德加-芭蕾舞者" },
          { id: 5, thumbnail: "/static/c5.png", original: "/static/c5.png", title: "塞尚-苹果" },
          { id: 6, thumbnail: "/static/c6.png", original: "/static/c6.png", title: "毕沙罗-乡村" },
          { id: 7, thumbnail: "/static/c7.png", original: "/static/c7.png", title: "马奈-奥林匹亚" },
          { id: 8, thumbnail: "/static/c8.png", original: "/static/c8.png", title: "西斯莱-风景" }
        ]
      },
      {
        id: 2,
        title: "现代艺术回顾展",
        date: "2024-01-20",
        location: "上海当代艺术博物馆",
        photoCount: 8,
        photos: [
          { id: 9, thumbnail: "/static/c9.png", original: "/static/c9.png", title: "毕加索-立体主义" },
          { id: 10, thumbnail: "/static/c1.png", original: "/static/c1.png", title: "康定斯基-抽象" },
          { id: 11, thumbnail: "/static/c2.png", original: "/static/c2.png", title: "蒙德里安-几何" },
          { id: 12, thumbnail: "/static/c3.png", original: "/static/c3.png", title: "达利-超现实" }
        ]
      },
      {
        id: 3,
        title: "中国古代书画展",
        date: "2024-01-25",
        location: "故宫博物院",
        photoCount: 15,
        photos: [
          { id: 13, thumbnail: "/static/c4.png", original: "/static/c4.png", title: "清明上河图" },
          { id: 14, thumbnail: "/static/c5.png", original: "/static/c5.png", title: "富春山居图" },
          { id: 15, thumbnail: "/static/c6.png", original: "/static/c6.png", title: "千里江山图" },
          { id: 16, thumbnail: "/static/c7.png", original: "/static/c7.png", title: "韩熙载夜宴图" },
          { id: 17, thumbnail: "/static/c8.png", original: "/static/c8.png", title: "步辇图" },
          { id: 18, thumbnail: "/static/c9.png", original: "/static/c9.png", title: "洛神赋图" }
        ]
      }
    ]);
    const filteredSessions = common_vendor.computed(() => {
      if (!searchKeyword.value) {
        return exhibitionSessions;
      }
      return exhibitionSessions.filter(
        (session) => session.title.includes(searchKeyword.value) || session.location.includes(searchKeyword.value)
      );
    });
    const goBack = () => {
      common_vendor.index.navigateBack();
    };
    const searchExhibitions = () => {
      common_vendor.index.__f__("log", "at pages/exhibition-album/exhibition-album.vue:148", "搜索展览:", searchKeyword.value);
    };
    const onSearchInput = (value) => {
      searchKeyword.value = value;
    };
    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      return `${date.getMonth() + 1}月${date.getDate()}日`;
    };
    const viewSessionPhotos = (session) => {
      common_vendor.index.navigateTo({
        url: `/pages/photo-gallery/photo-gallery?sessionId=${session.id}&title=${encodeURIComponent(session.title)}`
      });
    };
    const goToExhibition = () => {
      common_vendor.index.switchTab({
        url: "/pages/exhibition/exhibition"
      });
    };
    common_vendor.onMounted(() => {
      common_vendor.index.__f__("log", "at pages/exhibition-album/exhibition-album.vue:174", "看展相册页面加载完成");
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(goBack),
        b: common_vendor.p({
          title: "看展相册",
          border: false,
          ["background-color"]: "#ffffff",
          color: "#333333",
          ["left-icon"]: "back"
        }),
        c: common_vendor.o(searchExhibitions),
        d: common_vendor.o(onSearchInput),
        e: common_vendor.o(($event) => searchKeyword.value = $event),
        f: common_vendor.p({
          placeholder: "搜索展览或展品",
          cancelButton: "none",
          modelValue: searchKeyword.value
        }),
        g: common_vendor.f(filteredSessions.value, (session, k0, i0) => {
          return {
            a: common_vendor.t(session.title),
            b: common_vendor.t(formatDate(session.date)),
            c: common_vendor.t(session.location),
            d: "c1dac981-2-" + i0,
            e: common_vendor.p({
              text: session.photoCount,
              type: "primary"
            }),
            f: common_vendor.f(session.photos.slice(0, 6), (photo, index, i1) => {
              return common_vendor.e({
                a: photo.thumbnail,
                b: index === 5 && session.photos.length > 6
              }, index === 5 && session.photos.length > 6 ? {
                c: common_vendor.t(session.photos.length - 5)
              } : {}, {
                d: index,
                e: index === 5 && session.photos.length > 6 ? 1 : ""
              });
            }),
            g: session.id,
            h: common_vendor.o(($event) => viewSessionPhotos(session))
          };
        }),
        h: filteredSessions.value.length === 0
      }, filteredSessions.value.length === 0 ? {
        i: common_vendor.p({
          type: "image",
          size: "60",
          color: "#ccc"
        }),
        j: common_vendor.o(goToExhibition)
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c1dac981"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/exhibition-album/exhibition-album.js.map
