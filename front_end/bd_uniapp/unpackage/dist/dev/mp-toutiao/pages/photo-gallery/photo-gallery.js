"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Array) {
  const _easycom_uni_nav_bar2 = common_vendor.resolveComponent("uni-nav-bar");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  (_easycom_uni_nav_bar2 + _easycom_uni_icons2)();
}
const _easycom_uni_nav_bar = () => "../../uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.js";
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  (_easycom_uni_nav_bar + _easycom_uni_icons)();
}
const _sfc_main = {
  __name: "photo-gallery",
  setup(__props) {
    const sessionId = common_vendor.ref("");
    const sessionTitle = common_vendor.ref("展览照片");
    const sessionDate = common_vendor.ref("");
    const selectMode = common_vendor.ref(false);
    const selectedPhotos = common_vendor.ref([]);
    const photos = common_vendor.reactive([]);
    const mockPhotos = {
      1: [
        { id: 1, thumbnail: "/static/c1.png", original: "/static/c1.png", title: "莫奈-睡莲" },
        { id: 2, thumbnail: "/static/c2.png", original: "/static/c2.png", title: "梵高-星空" },
        { id: 3, thumbnail: "/static/c3.png", original: "/static/c3.png", title: "雷诺阿-舞会" },
        { id: 4, thumbnail: "/static/c4.png", original: "/static/c4.png", title: "德加-芭蕾舞者" },
        { id: 5, thumbnail: "/static/c5.png", original: "/static/c5.png", title: "塞尚-苹果" },
        { id: 6, thumbnail: "/static/c6.png", original: "/static/c6.png", title: "毕沙罗-乡村" },
        { id: 7, thumbnail: "/static/c7.png", original: "/static/c7.png", title: "马奈-奥林匹亚" },
        { id: 8, thumbnail: "/static/c8.png", original: "/static/c8.png", title: "西斯莱-风景" },
        { id: 9, thumbnail: "/static/c9.png", original: "/static/c9.png", title: "莫奈-日出印象" },
        { id: 10, thumbnail: "/static/c1.png", original: "/static/c1.png", title: "梵高-向日葵" },
        { id: 11, thumbnail: "/static/c2.png", original: "/static/c2.png", title: "雷诺阿-船上的午餐" },
        { id: 12, thumbnail: "/static/c3.png", original: "/static/c3.png", title: "德加-舞蹈课" }
      ],
      2: [
        { id: 13, thumbnail: "/static/c4.png", original: "/static/c4.png", title: "毕加索-立体主义" },
        { id: 14, thumbnail: "/static/c5.png", original: "/static/c5.png", title: "康定斯基-抽象" },
        { id: 15, thumbnail: "/static/c6.png", original: "/static/c6.png", title: "蒙德里安-几何" },
        { id: 16, thumbnail: "/static/c7.png", original: "/static/c7.png", title: "达利-超现实" },
        { id: 17, thumbnail: "/static/c8.png", original: "/static/c8.png", title: "马蒂斯-野兽派" },
        { id: 18, thumbnail: "/static/c9.png", original: "/static/c9.png", title: "波洛克-滴画" },
        { id: 19, thumbnail: "/static/c1.png", original: "/static/c1.png", title: "罗斯科-色域" },
        { id: 20, thumbnail: "/static/c2.png", original: "/static/c2.png", title: "沃霍尔-波普" }
      ],
      3: [
        { id: 21, thumbnail: "/static/c3.png", original: "/static/c3.png", title: "清明上河图" },
        { id: 22, thumbnail: "/static/c4.png", original: "/static/c4.png", title: "富春山居图" },
        { id: 23, thumbnail: "/static/c5.png", original: "/static/c5.png", title: "千里江山图" },
        { id: 24, thumbnail: "/static/c6.png", original: "/static/c6.png", title: "韩熙载夜宴图" },
        { id: 25, thumbnail: "/static/c7.png", original: "/static/c7.png", title: "步辇图" },
        { id: 26, thumbnail: "/static/c8.png", original: "/static/c8.png", title: "洛神赋图" }
      ]
    };
    const goBack = () => {
      common_vendor.index.navigateBack();
    };
    const handlePhotoClick = (photo, index) => {
      if (selectMode.value) {
        const photoIndex = selectedPhotos.value.indexOf(photo.id);
        if (photoIndex > -1) {
          selectedPhotos.value.splice(photoIndex, 1);
        } else {
          selectedPhotos.value.push(photo.id);
        }
      } else {
        previewPhoto(photo, index);
      }
    };
    const previewPhoto = (photo, index) => {
      const urls = photos.map((p) => p.original);
      common_vendor.index.previewImage({
        current: index,
        urls
      });
    };
    const useAsStyleReference = () => {
      if (selectedPhotos.value.length === 0)
        return;
      const selectedPhotoData = photos.filter((p) => selectedPhotos.value.includes(p.id));
      common_vendor.index.navigateTo({
        url: `/pages/creation/creation?stylePhotos=${encodeURIComponent(JSON.stringify(selectedPhotoData))}`
      });
    };
    const sharePhotos = () => {
      if (selectedPhotos.value.length === 0)
        return;
      common_vendor.index.showActionSheet({
        itemList: ["分享到微信", "分享到朋友圈", "复制链接"],
        success: (res) => {
          switch (res.tapIndex) {
            case 0:
              shareToWeChat();
              break;
            case 1:
              shareToMoments();
              break;
            case 2:
              copyShareLink();
              break;
          }
        }
      });
    };
    const shareToWeChat = () => {
      common_vendor.index.showToast({
        title: "分享到微信",
        icon: "success"
      });
    };
    const shareToMoments = () => {
      common_vendor.index.showToast({
        title: "分享到朋友圈",
        icon: "success"
      });
    };
    const copyShareLink = () => {
      common_vendor.index.setClipboardData({
        data: `https://example.com/gallery/${sessionId.value}`,
        success: () => {
          common_vendor.index.showToast({
            title: "链接已复制",
            icon: "success"
          });
        }
      });
    };
    const deletePhotos = () => {
      if (selectedPhotos.value.length === 0)
        return;
      common_vendor.index.showModal({
        title: "确认删除",
        content: `确定要删除选中的 ${selectedPhotos.value.length} 张照片吗？`,
        success: (res) => {
          if (res.confirm) {
            for (let i = photos.length - 1; i >= 0; i--) {
              if (selectedPhotos.value.includes(photos[i].id)) {
                photos.splice(i, 1);
              }
            }
            selectedPhotos.value = [];
            selectMode.value = false;
            common_vendor.index.showToast({
              title: "删除成功",
              icon: "success"
            });
          }
        }
      });
    };
    const onImageLoad = () => {
    };
    const onImageError = () => {
      common_vendor.index.__f__("log", "at pages/photo-gallery/photo-gallery.vue:258", "图片加载失败");
    };
    common_vendor.onLoad((options) => {
      sessionId.value = options.sessionId || "1";
      sessionTitle.value = decodeURIComponent(options.title || "展览照片");
      const sessionPhotos = mockPhotos[sessionId.value] || [];
      photos.splice(0, photos.length, ...sessionPhotos);
      const dates = {
        "1": "2024年1月15日",
        "2": "2024年1月20日",
        "3": "2024年1月25日"
      };
      sessionDate.value = dates[sessionId.value] || "";
    });
    common_vendor.onMounted(() => {
      common_vendor.index.__f__("log", "at pages/photo-gallery/photo-gallery.vue:280", "照片画廊页面加载完成");
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(selectMode.value ? "取消" : "选择"),
        b: common_vendor.o(($event) => selectMode.value = !selectMode.value),
        c: common_vendor.o(goBack),
        d: common_vendor.p({
          title: sessionTitle.value,
          border: false,
          ["background-color"]: "#ffffff",
          color: "#333333",
          ["left-icon"]: "back"
        }),
        e: common_vendor.t(photos.length),
        f: common_vendor.t(sessionDate.value),
        g: common_vendor.f(photos, (photo, index, i0) => {
          return common_vendor.e({
            a: photo.thumbnail
          }, selectMode.value ? {
            b: "469522e3-1-" + i0,
            c: common_vendor.p({
              type: selectedPhotos.value.includes(photo.id) ? "checkmarkempty" : "circle",
              size: "24",
              color: selectedPhotos.value.includes(photo.id) ? "#007AFF" : "#fff"
            })
          } : {}, {
            d: common_vendor.t(photo.title),
            e: photo.id,
            f: common_vendor.o(($event) => handlePhotoClick(photo, index))
          });
        }),
        h: common_vendor.o(onImageLoad),
        i: common_vendor.o(onImageError),
        j: selectMode.value,
        k: selectMode.value && selectedPhotos.value.length > 0
      }, selectMode.value && selectedPhotos.value.length > 0 ? {
        l: common_vendor.t(selectedPhotos.value.length),
        m: common_vendor.p({
          type: "color",
          size: "18",
          color: "#007AFF"
        }),
        n: common_vendor.o(useAsStyleReference),
        o: common_vendor.p({
          type: "redo",
          size: "18",
          color: "#007AFF"
        }),
        p: common_vendor.o(sharePhotos),
        q: common_vendor.p({
          type: "trash",
          size: "18",
          color: "#ff4757"
        }),
        r: common_vendor.o(deletePhotos)
      } : {}, {
        s: photos.length === 0
      }, photos.length === 0 ? {
        t: common_vendor.p({
          type: "image",
          size: "60",
          color: "#ccc"
        })
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-469522e3"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/photo-gallery/photo-gallery.js.map
