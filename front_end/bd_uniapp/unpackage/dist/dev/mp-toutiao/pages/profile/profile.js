"use strict";
const common_vendor = require("../../common/vendor.js");
if (!Array) {
  const _easycom_uni_badge2 = common_vendor.resolveComponent("uni-badge");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_list_item2 = common_vendor.resolveComponent("uni-list-item");
  const _easycom_uni_list2 = common_vendor.resolveComponent("uni-list");
  const _easycom_uni_section2 = common_vendor.resolveComponent("uni-section");
  const _easycom_uni_card2 = common_vendor.resolveComponent("uni-card");
  (_easycom_uni_badge2 + _easycom_uni_icons2 + _easycom_uni_list_item2 + _easycom_uni_list2 + _easycom_uni_section2 + _easycom_uni_card2)();
}
const _easycom_uni_badge = () => "../../uni_modules/uni-badge/components/uni-badge/uni-badge.js";
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_list_item = () => "../../uni_modules/uni-list/components/uni-list-item/uni-list-item.js";
const _easycom_uni_list = () => "../../uni_modules/uni-list/components/uni-list/uni-list.js";
const _easycom_uni_section = () => "../../uni_modules/uni-section/components/uni-section/uni-section.js";
const _easycom_uni_card = () => "../../uni_modules/uni-card/components/uni-card/uni-card.js";
if (!Math) {
  (_easycom_uni_badge + _easycom_uni_icons + _easycom_uni_list_item + _easycom_uni_list + _easycom_uni_section + _easycom_uni_card)();
}
const _sfc_main = {
  __name: "profile",
  setup(__props) {
    const userInfo = common_vendor.reactive({
      avatar: "/static/logo.png",
      nickname: "艺术爱好者",
      phone: "",
      aiPoints: 1280,
      worksCount: 15,
      exhibitionCount: 8,
      exhibitionPhotos: 45,
      isVip: false,
      vipLevel: "VIP",
      vipExpireTime: "2024-12-31",
      hasSignedToday: false
    });
    const editProfile = () => {
      common_vendor.index.showToast({
        title: "编辑个人资料",
        icon: "none"
      });
    };
    const goToExhibitionAlbum = () => {
      common_vendor.index.navigateTo({
        url: "/pages/exhibition-album/exhibition-album"
      });
    };
    const goToGeneratedWorks = () => {
      common_vendor.index.navigateTo({
        url: "/pages/generated-works/generated-works"
      });
    };
    const bindPhone = () => {
      common_vendor.index.showToast({
        title: "绑定手机号",
        icon: "none"
      });
    };
    const privacySettings = () => {
      common_vendor.index.showToast({
        title: "隐私设置",
        icon: "none"
      });
    };
    const dailySign = () => {
      if (userInfo.hasSignedToday)
        return;
      userInfo.hasSignedToday = true;
      userInfo.aiPoints += 10;
      common_vendor.index.showToast({
        title: "签到成功，获得10积分",
        icon: "success"
      });
    };
    const viewPointsRules = () => {
      common_vendor.index.showModal({
        title: "积分获取规则",
        content: "每日签到：10积分\n分享应用：20积分\n完成看展：30积分\n生成作品：消耗50积分",
        showCancel: false
      });
    };
    const viewPointsHistory = () => {
      common_vendor.index.navigateTo({
        url: "/pages/points-history/points-history"
      });
    };
    const shareApp = () => {
      common_vendor.index.share({
        provider: "weixin",
        scene: "WXSceneSession",
        title: "AIGC艺术风格化小程序",
        summary: "用AI创造你的艺术作品",
        imageUrl: "/static/logo.png",
        success: () => {
          userInfo.aiPoints += 20;
          common_vendor.index.showToast({
            title: "分享成功，获得20积分",
            icon: "success"
          });
        }
      });
    };
    const manageVip = () => {
      common_vendor.index.navigateTo({
        url: "/pages/vip-purchase/vip-purchase"
      });
    };
    const helpAndFeedback = () => {
      common_vendor.index.navigateTo({
        url: "/pages/help-feedback/help-feedback"
      });
    };
    const aboutUs = () => {
      common_vendor.index.navigateTo({
        url: "/pages/about/about"
      });
    };
    common_vendor.onMounted(() => {
      common_vendor.index.__f__("log", "at pages/profile/profile.vue:302", "个人中心页面加载完成");
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: userInfo.avatar,
        b: userInfo.isVip
      }, userInfo.isVip ? {
        c: common_vendor.p({
          text: userInfo.vipLevel,
          type: "error"
        })
      } : {}, {
        d: common_vendor.t(userInfo.nickname),
        e: common_vendor.t(userInfo.phone || "未绑定手机号"),
        f: common_vendor.t(userInfo.aiPoints),
        g: common_vendor.t(userInfo.worksCount),
        h: common_vendor.t(userInfo.exhibitionCount),
        i: common_vendor.p({
          type: "gear",
          size: "16",
          color: "#666"
        }),
        j: common_vendor.o(editProfile),
        k: common_vendor.p({
          text: userInfo.exhibitionPhotos,
          type: "primary"
        }),
        l: common_vendor.o(goToExhibitionAlbum),
        m: common_vendor.p({
          title: "看展相册",
          note: "按展览会话分类的展品照片",
          thumb: "/static/c7.png",
          ["show-arrow"]: true
        }),
        n: common_vendor.p({
          text: userInfo.worksCount,
          type: "success"
        }),
        o: common_vendor.o(goToGeneratedWorks),
        p: common_vendor.p({
          title: "生成作品",
          note: "AIGC图片和视频作品集",
          thumb: "/static/c5.png",
          ["show-arrow"]: true
        }),
        q: common_vendor.p({
          title: "我的展藏",
          type: "line"
        }),
        r: common_vendor.o(editProfile),
        s: common_vendor.p({
          title: "个人资料",
          note: "头像、昵称等基础信息",
          thumb: "/static/c3.png",
          ["show-arrow"]: true
        }),
        t: common_vendor.o(bindPhone),
        v: common_vendor.p({
          title: "手机号码",
          note: userInfo.phone || "点击绑定手机号",
          thumb: "/static/c4.png",
          ["show-arrow"]: true
        }),
        w: common_vendor.o(privacySettings),
        x: common_vendor.p({
          title: "隐私设置",
          note: "作品公开性、数据使用等",
          thumb: "/static/c1.png",
          ["show-arrow"]: true
        }),
        y: common_vendor.p({
          title: "账户信息",
          type: "line"
        }),
        z: common_vendor.t(userInfo.aiPoints),
        A: common_vendor.t(userInfo.hasSignedToday ? "已签到" : "每日签到"),
        B: common_vendor.o(dailySign),
        C: userInfo.hasSignedToday,
        D: common_vendor.p({
          type: "help",
          size: "20",
          color: "#007AFF"
        }),
        E: common_vendor.o(viewPointsRules),
        F: common_vendor.p({
          type: "list",
          size: "20",
          color: "#007AFF"
        }),
        G: common_vendor.o(viewPointsHistory),
        H: common_vendor.p({
          type: "redo",
          size: "20",
          color: "#007AFF"
        }),
        I: common_vendor.o(shareApp),
        J: common_vendor.p({
          ["is-shadow"]: false,
          border: true
        }),
        K: common_vendor.p({
          title: "AI积分系统",
          type: "line"
        }),
        L: common_vendor.t(userInfo.isVip ? "VIP会员" : "开通会员"),
        M: userInfo.isVip
      }, userInfo.isVip ? {
        N: common_vendor.t(userInfo.vipExpireTime)
      } : {}, {
        O: userInfo.isVip
      }, userInfo.isVip ? {
        P: common_vendor.p({
          text: userInfo.vipLevel,
          type: "error"
        })
      } : {}, {
        Q: common_vendor.p({
          type: "checkmarkempty",
          size: "16",
          color: userInfo.isVip ? "#FF6B35" : "#999"
        }),
        R: userInfo.isVip ? 1 : "",
        S: common_vendor.p({
          type: "checkmarkempty",
          size: "16",
          color: userInfo.isVip ? "#FF6B35" : "#999"
        }),
        T: userInfo.isVip ? 1 : "",
        U: common_vendor.p({
          type: "checkmarkempty",
          size: "16",
          color: userInfo.isVip ? "#FF6B35" : "#999"
        }),
        V: userInfo.isVip ? 1 : "",
        W: common_vendor.p({
          type: "checkmarkempty",
          size: "16",
          color: userInfo.isVip ? "#FF6B35" : "#999"
        }),
        X: userInfo.isVip ? 1 : "",
        Y: common_vendor.t(userInfo.isVip ? "续费会员" : "立即开通"),
        Z: common_vendor.o(manageVip),
        aa: userInfo.isVip ? 1 : "",
        ab: userInfo.isVip ? 1 : "",
        ac: common_vendor.p({
          ["is-shadow"]: false,
          border: true
        }),
        ad: common_vendor.p({
          title: "会员功能",
          type: "line"
        }),
        ae: common_vendor.o(helpAndFeedback),
        af: common_vendor.p({
          title: "帮助与反馈",
          note: "使用指南、问题反馈",
          thumb: "/static/c2.png",
          ["show-arrow"]: true
        }),
        ag: common_vendor.o(aboutUs),
        ah: common_vendor.p({
          title: "关于我们",
          note: "版本信息、用户协议",
          thumb: "/static/c9.png",
          ["show-arrow"]: true
        }),
        ai: common_vendor.p({
          title: "其他",
          type: "line"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-dd383ca2"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/profile/profile.js.map
