"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Math) {
  (uniIcons + uniLoadMore + uniPopup)();
}
const uniIcons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const uniPopup = () => "../../uni_modules/uni-popup/components/uni-popup/uni-popup.js";
const uniLoadMore = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _sfc_main = {
  __name: "exhibition",
  setup(__props) {
    const currentView = common_vendor.ref("sessionList");
    const currentSession = common_vendor.ref(null);
    const sessionList = common_vendor.ref([
      // 示例数据
      {
        id: "1",
        title: "印象派大师展",
        description: "探索印象派艺术的光影世界",
        createTime: Date.now() - 864e5,
        analysisCount: 5,
        analysisList: [
          {
            id: "a1",
            photo: "/static/c1.png",
            createTime: Date.now() - 36e5,
            result: {
              basicInfo: {
                title: "日出·印象",
                artist: "克劳德·莫奈",
                year: "1872年"
              },
              style: "印象派风格，注重光线和色彩的变化，笔触松散而富有表现力。",
              background: "这幅画描绘了勒阿弗尔港口的日出景象，是印象派运动的开山之作。",
              details: "画面中的太阳反射在水面上形成橙色的光带，船只的轮廓在晨雾中若隐若现。",
              story: "莫奈在1872年的一个早晨，在勒阿弗尔的港口画下了这幅作品，后来成为了印象派的代表作。"
            }
          }
        ],
        context: {
          exhibition: "印象派大师展",
          venue: "国家美术馆",
          period: "2024年1月-3月"
        }
      }
    ]);
    const linkUrl = common_vendor.ref("");
    const uploadedPhotos = common_vendor.ref([]);
    const loadingText = common_vendor.ref("");
    const loadingPopup = common_vendor.ref(null);
    const showCreateSession = () => {
      currentView.value = "createSession";
      linkUrl.value = "";
      uploadedPhotos.value = [];
    };
    const backToList = () => {
      currentView.value = "sessionList";
      currentSession.value = null;
    };
    const enterSession = (session) => {
      currentSession.value = session;
      currentView.value = "sessionDetail";
    };
    const editSession = (session) => {
      common_vendor.index.showModal({
        title: "编辑会话",
        editable: true,
        placeholderText: session.title,
        success: (res) => {
          if (res.confirm && res.content) {
            session.title = res.content;
            common_vendor.index.showToast({
              title: "修改成功",
              icon: "success"
            });
          }
        }
      });
    };
    const deleteSession = (sessionId) => {
      common_vendor.index.showModal({
        title: "确认删除",
        content: "删除后无法恢复，确定要删除这个会话吗？",
        success: (res) => {
          if (res.confirm) {
            const index = sessionList.value.findIndex((s) => s.id === sessionId);
            if (index > -1) {
              sessionList.value.splice(index, 1);
              common_vendor.index.showToast({
                title: "删除成功",
                icon: "success"
              });
            }
          }
        }
      });
    };
    const importFromLink = async () => {
      var _a, _b, _c;
      if (!linkUrl.value.trim())
        return;
      loadingText.value = "正在解析链接内容...";
      (_a = loadingPopup.value) == null ? void 0 : _a.open();
      try {
        await new Promise((resolve) => setTimeout(resolve, 2e3));
        const newSession = {
          id: Date.now().toString(),
          title: "从链接导入的展览",
          description: "通过链接解析获得的展览信息",
          createTime: Date.now(),
          analysisCount: 0,
          analysisList: [],
          context: {
            source: "link",
            url: linkUrl.value
          }
        };
        sessionList.value.unshift(newSession);
        (_b = loadingPopup.value) == null ? void 0 : _b.close();
        common_vendor.index.showToast({
          title: "导入成功",
          icon: "success"
        });
        enterSession(newSession);
      } catch (error) {
        (_c = loadingPopup.value) == null ? void 0 : _c.close();
        common_vendor.index.showToast({
          title: "导入失败",
          icon: "none"
        });
      }
    };
    const takePhoto = (type) => {
      const typeLabels = {
        title: "展览标题",
        intro: "展览前言",
        artist: "艺术家简介",
        guide: "导览手册"
      };
      common_vendor.index.chooseImage({
        count: 1,
        sizeType: ["compressed"],
        sourceType: ["camera", "album"],
        success: (res) => {
          uploadedPhotos.value.push({
            url: res.tempFilePaths[0],
            type,
            label: typeLabels[type]
          });
          common_vendor.index.showToast({
            title: `${typeLabels[type]}上传成功`,
            icon: "success"
          });
        }
      });
    };
    const removePhoto = (index) => {
      uploadedPhotos.value.splice(index, 1);
    };
    const createFromPhotos = async () => {
      var _a, _b, _c;
      if (uploadedPhotos.value.length === 0)
        return;
      loadingText.value = "AI正在解析展览信息...";
      (_a = loadingPopup.value) == null ? void 0 : _a.open();
      try {
        await new Promise((resolve) => setTimeout(resolve, 3e3));
        const newSession = {
          id: Date.now().toString(),
          title: "从照片创建的展览",
          description: "AI解析照片内容生成的展览会话",
          createTime: Date.now(),
          analysisCount: 0,
          analysisList: [],
          context: {
            source: "photos",
            photos: [...uploadedPhotos.value]
          }
        };
        sessionList.value.unshift(newSession);
        (_b = loadingPopup.value) == null ? void 0 : _b.close();
        common_vendor.index.showToast({
          title: "会话创建成功",
          icon: "success"
        });
        enterSession(newSession);
      } catch (error) {
        (_c = loadingPopup.value) == null ? void 0 : _c.close();
        common_vendor.index.showToast({
          title: "创建失败",
          icon: "none"
        });
      }
    };
    const takeAnalysisPhoto = () => {
      common_vendor.index.chooseImage({
        count: 1,
        sizeType: ["compressed"],
        sourceType: ["camera", "album"],
        success: async (res) => {
          var _a, _b, _c;
          const photoUrl = res.tempFilePaths[0];
          loadingText.value = "AI正在解析展品...";
          (_a = loadingPopup.value) == null ? void 0 : _a.open();
          try {
            await new Promise((resolve) => setTimeout(resolve, 4e3));
            const analysisResult = {
              id: Date.now().toString(),
              photo: photoUrl,
              createTime: Date.now(),
              result: {
                basicInfo: {
                  title: "星夜",
                  artist: "文森特·梵高",
                  year: "1889年"
                },
                style: "后印象派风格，运用旋转的笔触和强烈的色彩对比，表现出强烈的情感和动感。",
                background: "梵高在圣雷米精神病院期间创作，描绘了他从病房窗户看到的夜空景象。",
                details: "画面中的星星和月亮散发着光芒，天空中的云彩呈现螺旋状，柏树像火焰一样向上伸展。",
                story: "这是梵高最著名的作品之一，创作时他正处于精神状态不稳定的时期，但艺术创作力却达到了巅峰。"
              }
            };
            if (!currentSession.value.analysisList) {
              currentSession.value.analysisList = [];
            }
            currentSession.value.analysisList.unshift(analysisResult);
            currentSession.value.analysisCount = currentSession.value.analysisList.length;
            saveToPersonalAlbum(analysisResult);
            (_b = loadingPopup.value) == null ? void 0 : _b.close();
            common_vendor.index.showToast({
              title: "解析完成",
              icon: "success"
            });
          } catch (error) {
            (_c = loadingPopup.value) == null ? void 0 : _c.close();
            common_vendor.index.showToast({
              title: "解析失败",
              icon: "none"
            });
          }
        }
      });
    };
    const saveToPersonalAlbum = (analysisResult) => {
      const albumKey = "exhibition_album";
      let album = common_vendor.index.getStorageSync(albumKey) || [];
      const albumItem = {
        sessionId: currentSession.value.id,
        sessionTitle: currentSession.value.title,
        photo: analysisResult.photo,
        analysis: analysisResult.result,
        createTime: analysisResult.createTime
      };
      album.unshift(albumItem);
      common_vendor.index.setStorageSync(albumKey, album);
    };
    const formatTime = (timestamp) => {
      const date = new Date(timestamp);
      const now = /* @__PURE__ */ new Date();
      const diff = now - date;
      if (diff < 6e4) {
        return "刚刚";
      } else if (diff < 36e5) {
        return `${Math.floor(diff / 6e4)}分钟前`;
      } else if (diff < 864e5) {
        return `${Math.floor(diff / 36e5)}小时前`;
      } else {
        return `${Math.floor(diff / 864e5)}天前`;
      }
    };
    common_vendor.onMounted(() => {
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d, _e, _f;
      return common_vendor.e({
        a: currentView.value === "sessionList"
      }, currentView.value === "sessionList" ? common_vendor.e({
        b: common_vendor.p({
          type: "plus",
          size: "20",
          color: "#fff"
        }),
        c: common_vendor.o(showCreateSession),
        d: common_vendor.f(sessionList.value, (session, k0, i0) => {
          return {
            a: common_vendor.t(session.title),
            b: common_vendor.t(formatTime(session.createTime)),
            c: common_vendor.t(session.analysisCount),
            d: common_vendor.t(session.description),
            e: "faae53d7-1-" + i0,
            f: common_vendor.o(($event) => editSession(session)),
            g: "faae53d7-2-" + i0,
            h: common_vendor.o(($event) => deleteSession(session.id)),
            i: session.id,
            j: common_vendor.o(($event) => enterSession(session))
          };
        }),
        e: common_vendor.p({
          type: "compose",
          size: "16",
          color: "#666"
        }),
        f: common_vendor.p({
          type: "trash",
          size: "16",
          color: "#ff4757"
        }),
        g: sessionList.value.length === 0
      }, sessionList.value.length === 0 ? {
        h: common_vendor.p({
          type: "image",
          size: "60",
          color: "#ccc"
        })
      } : {}) : {}, {
        i: currentView.value === "createSession"
      }, currentView.value === "createSession" ? {
        j: common_vendor.p({
          type: "back",
          size: "20",
          color: "#333"
        }),
        k: common_vendor.o(backToList),
        l: common_vendor.p({
          type: "link",
          size: "24",
          color: "#007aff"
        }),
        m: linkUrl.value,
        n: common_vendor.o(($event) => linkUrl.value = $event.detail.value),
        o: common_vendor.o(importFromLink),
        p: !linkUrl.value.trim(),
        q: common_vendor.p({
          type: "camera",
          size: "24",
          color: "#007aff"
        }),
        r: common_vendor.f(uploadedPhotos.value, (photo, index, i0) => {
          return {
            a: photo.url,
            b: common_vendor.t(photo.label),
            c: "faae53d7-7-" + i0,
            d: common_vendor.o(($event) => removePhoto(index)),
            e: index
          };
        }),
        s: common_vendor.p({
          type: "close",
          size: "14",
          color: "#fff"
        }),
        t: common_vendor.p({
          type: "image",
          size: "16",
          color: "#007aff"
        }),
        v: common_vendor.o(($event) => takePhoto("title")),
        w: common_vendor.p({
          type: "image",
          size: "16",
          color: "#007aff"
        }),
        x: common_vendor.o(($event) => takePhoto("intro")),
        y: common_vendor.p({
          type: "image",
          size: "16",
          color: "#007aff"
        }),
        z: common_vendor.o(($event) => takePhoto("artist")),
        A: common_vendor.p({
          type: "image",
          size: "16",
          color: "#007aff"
        }),
        B: common_vendor.o(($event) => takePhoto("guide")),
        C: common_vendor.o(createFromPhotos),
        D: uploadedPhotos.value.length === 0
      } : {}, {
        E: currentView.value === "sessionDetail"
      }, currentView.value === "sessionDetail" ? common_vendor.e({
        F: common_vendor.p({
          type: "back",
          size: "20",
          color: "#333"
        }),
        G: common_vendor.o(backToList),
        H: common_vendor.t(((_a = currentSession.value) == null ? void 0 : _a.title) || "展览解析"),
        I: common_vendor.f(((_b = currentSession.value) == null ? void 0 : _b.analysisList) || [], (analysis, k0, i0) => {
          return common_vendor.e({
            a: analysis.photo,
            b: common_vendor.t(formatTime(analysis.createTime)),
            c: "faae53d7-13-" + i0,
            d: analysis.result.basicInfo
          }, analysis.result.basicInfo ? {
            e: common_vendor.t(analysis.result.basicInfo.title),
            f: common_vendor.t(analysis.result.basicInfo.artist),
            g: common_vendor.t(analysis.result.basicInfo.year)
          } : {}, {
            h: analysis.result.style
          }, analysis.result.style ? {
            i: common_vendor.t(analysis.result.style)
          } : {}, {
            j: analysis.result.background
          }, analysis.result.background ? {
            k: common_vendor.t(analysis.result.background)
          } : {}, {
            l: analysis.result.details
          }, analysis.result.details ? {
            m: common_vendor.t(analysis.result.details)
          } : {}, {
            n: analysis.result.story
          }, analysis.result.story ? {
            o: common_vendor.t(analysis.result.story)
          } : {}, {
            p: analysis.id
          });
        }),
        J: common_vendor.p({
          type: "robot",
          size: "20",
          color: "#007aff"
        }),
        K: !((_d = (_c = currentSession.value) == null ? void 0 : _c.analysisList) == null ? void 0 : _d.length)
      }, !((_f = (_e = currentSession.value) == null ? void 0 : _e.analysisList) == null ? void 0 : _f.length) ? {
        L: common_vendor.p({
          type: "camera",
          size: "60",
          color: "#ccc"
        })
      } : {}, {
        M: common_vendor.p({
          type: "camera-filled",
          size: "30",
          color: "#fff"
        }),
        N: common_vendor.o(takeAnalysisPhoto)
      }) : {}, {
        O: common_vendor.p({
          status: "loading",
          ["content-text"]: {
            contentdown: loadingText.value
          }
        }),
        P: common_vendor.sr(loadingPopup, "faae53d7-16", {
          "k": "loadingPopup"
        }),
        Q: common_vendor.p({
          type: "center"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-faae53d7"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/exhibition/exhibition.js.map
