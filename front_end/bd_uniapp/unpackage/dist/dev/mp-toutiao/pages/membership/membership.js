"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
if (!Array) {
  const _easycom_uni_nav_bar2 = common_vendor.resolveComponent("uni-nav-bar");
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  const _easycom_uni_section2 = common_vendor.resolveComponent("uni-section");
  const _component_uni_progress = common_vendor.resolveComponent("uni-progress");
  (_easycom_uni_nav_bar2 + _easycom_uni_icons2 + _easycom_uni_section2 + _component_uni_progress)();
}
const _easycom_uni_nav_bar = () => "../../uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.js";
const _easycom_uni_icons = () => "../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
const _easycom_uni_section = () => "../../uni_modules/uni-section/components/uni-section/uni-section.js";
if (!Math) {
  (_easycom_uni_nav_bar + _easycom_uni_icons + _easycom_uni_section)();
}
const _sfc_main = {
  __name: "membership",
  setup(__props) {
    const membershipInfo = common_vendor.reactive({
      nickname: "艺术爱好者",
      isVip: false,
      // 改为true可以测试VIP状态
      expireDate: "2024-12-31T23:59:59"
    });
    const usageStats = common_vendor.reactive({
      imageGeneration: 8,
      videoGeneration: 2,
      aiAnalysis: 15
    });
    const selectedPlan = common_vendor.ref(null);
    const memberBenefits = [
      {
        id: 1,
        title: "无限生成",
        description: "图片和视频无限次生成",
        icon: "loop",
        freeUser: false
      },
      {
        id: 2,
        title: "优先处理",
        description: "生成队列优先处理",
        icon: "forward",
        freeUser: false
      },
      {
        id: 3,
        title: "专属风格",
        description: "50+专属艺术风格模板",
        icon: "color",
        freeUser: false
      },
      {
        id: 4,
        title: "高清导出",
        description: "支持4K高清图片导出",
        icon: "image",
        freeUser: false
      },
      {
        id: 5,
        title: "无水印",
        description: "生成作品无水印",
        icon: "eye-slash",
        freeUser: false
      },
      {
        id: 6,
        title: "基础功能",
        description: "每日免费生成次数",
        icon: "gift",
        freeUser: true
      },
      {
        id: 7,
        title: "AI解析",
        description: "展品智能解析服务",
        icon: "eye",
        freeUser: true
      },
      {
        id: 8,
        title: "作品保存",
        description: "云端作品永久保存",
        icon: "cloud-upload",
        freeUser: true
      }
    ];
    const pricingPlans = [
      {
        id: 1,
        name: "月度会员",
        price: 29,
        originalPrice: null,
        unit: "月",
        recommended: false,
        features: [
          "无限图片生成",
          "无限视频生成",
          "优先处理队列",
          "专属风格模板",
          "4K高清导出"
        ]
      },
      {
        id: 2,
        name: "季度会员",
        price: 69,
        originalPrice: 87,
        unit: "季",
        recommended: true,
        features: [
          "包含月度会员所有权益",
          "额外赠送100积分",
          "专属客服支持",
          "新功能抢先体验"
        ]
      },
      {
        id: 3,
        name: "年度会员",
        price: 199,
        originalPrice: 348,
        unit: "年",
        recommended: false,
        features: [
          "包含季度会员所有权益",
          "额外赠送500积分",
          "专属定制风格",
          "线下活动优先邀请"
        ]
      }
    ];
    const getUsagePercent = (type) => {
      if (membershipInfo.isVip) {
        return 0;
      }
      const limits = {
        imageGeneration: 10,
        videoGeneration: 3,
        aiAnalysis: 20
      };
      const used = usageStats[type];
      const limit = limits[type];
      return Math.min(used / limit * 100, 100);
    };
    const goBack = () => {
      common_vendor.index.navigateBack();
    };
    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`;
    };
    const showPricingPlans = () => {
      common_vendor.index.pageScrollTo({
        selector: ".pricing-section",
        duration: 300
      });
    };
    const selectPlan = (planId) => {
      selectedPlan.value = planId;
    };
    const purchaseMembership = () => {
      if (!selectedPlan.value) {
        common_vendor.index.showToast({
          title: "请选择套餐",
          icon: "none"
        });
        return;
      }
      const plan = pricingPlans.find((p) => p.id === selectedPlan.value);
      common_vendor.index.showModal({
        title: "确认购买",
        content: `确认购买${plan.name}（¥${plan.price}）？`,
        success: (res) => {
          if (res.confirm) {
            common_vendor.index.showToast({
              title: "支付成功",
              icon: "success"
            });
            setTimeout(() => {
              membershipInfo.isVip = true;
              const expireDate = /* @__PURE__ */ new Date();
              if (plan.unit === "月") {
                expireDate.setMonth(expireDate.getMonth() + 1);
              } else if (plan.unit === "季") {
                expireDate.setMonth(expireDate.getMonth() + 3);
              } else if (plan.unit === "年") {
                expireDate.setFullYear(expireDate.getFullYear() + 1);
              }
              membershipInfo.expireDate = expireDate.toISOString();
            }, 1e3);
          }
        }
      });
    };
    const renewMembership = () => {
      common_vendor.index.showModal({
        title: "续费会员",
        content: "是否续费一年会员（¥199）？",
        success: (res) => {
          if (res.confirm) {
            common_vendor.index.showToast({
              title: "续费成功",
              icon: "success"
            });
            setTimeout(() => {
              const expireDate = new Date(membershipInfo.expireDate);
              expireDate.setFullYear(expireDate.getFullYear() + 1);
              membershipInfo.expireDate = expireDate.toISOString();
            }, 1e3);
          }
        }
      });
    };
    common_vendor.onMounted(() => {
      common_vendor.index.__f__("log", "at pages/membership/membership.vue:418", "会员中心页面加载完成");
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(goBack),
        b: common_vendor.p({
          title: "会员中心",
          border: false,
          ["background-color"]: "transparent",
          color: "#ffffff",
          ["left-icon"]: "back"
        }),
        c: common_assets._imports_0,
        d: membershipInfo.isVip
      }, membershipInfo.isVip ? {
        e: common_vendor.p({
          type: "vip-filled",
          size: "16",
          color: "#FFD700"
        })
      } : {}, {
        f: common_vendor.t(membershipInfo.nickname),
        g: common_vendor.t(membershipInfo.isVip ? "VIP会员" : "普通用户"),
        h: membershipInfo.isVip ? 1 : "",
        i: membershipInfo.isVip
      }, membershipInfo.isVip ? {
        j: common_vendor.t(formatDate(membershipInfo.expireDate))
      } : {}, {
        k: !membershipInfo.isVip
      }, !membershipInfo.isVip ? {
        l: common_vendor.o(showPricingPlans)
      } : {}, {
        m: common_vendor.f(memberBenefits, (benefit, k0, i0) => {
          return common_vendor.e({
            a: "94171f33-3-" + i0 + ",94171f33-2",
            b: common_vendor.p({
              type: benefit.icon,
              size: "24",
              color: membershipInfo.isVip || benefit.freeUser ? "#007AFF" : "#ccc"
            }),
            c: common_vendor.t(benefit.title),
            d: common_vendor.t(benefit.description),
            e: !benefit.freeUser && !membershipInfo.isVip
          }, !benefit.freeUser && !membershipInfo.isVip ? {
            f: "94171f33-4-" + i0 + ",94171f33-2",
            g: common_vendor.p({
              type: "vip-filled",
              size: "12",
              color: "#FFD700"
            })
          } : {}, {
            h: benefit.id,
            i: membershipInfo.isVip || benefit.freeUser ? 1 : ""
          });
        }),
        n: common_vendor.p({
          title: "会员权益",
          type: "line"
        }),
        o: common_vendor.t(usageStats.imageGeneration),
        p: common_vendor.t(membershipInfo.isVip ? "无限" : "10"),
        q: common_vendor.p({
          percent: getUsagePercent("imageGeneration"),
          ["stroke-width"]: "6",
          ["active-color"]: "#007AFF",
          ["background-color"]: "#f0f0f0"
        }),
        r: common_vendor.t(usageStats.videoGeneration),
        s: common_vendor.t(membershipInfo.isVip ? "无限" : "3"),
        t: common_vendor.p({
          percent: getUsagePercent("videoGeneration"),
          ["stroke-width"]: "6",
          ["active-color"]: "#52c41a",
          ["background-color"]: "#f0f0f0"
        }),
        v: common_vendor.t(usageStats.aiAnalysis),
        w: common_vendor.t(membershipInfo.isVip ? "无限" : "20"),
        x: common_vendor.p({
          percent: getUsagePercent("aiAnalysis"),
          ["stroke-width"]: "6",
          ["active-color"]: "#ff7875",
          ["background-color"]: "#f0f0f0"
        }),
        y: common_vendor.p({
          title: "本月使用情况",
          type: "line"
        }),
        z: !membershipInfo.isVip
      }, !membershipInfo.isVip ? {
        A: common_vendor.f(pricingPlans, (plan, k0, i0) => {
          return common_vendor.e({
            a: plan.recommended
          }, plan.recommended ? {} : {}, {
            b: common_vendor.t(plan.name),
            c: common_vendor.t(plan.price),
            d: common_vendor.t(plan.unit),
            e: plan.originalPrice
          }, plan.originalPrice ? {
            f: common_vendor.t(plan.originalPrice)
          } : {}, {
            g: common_vendor.f(plan.features, (feature, k1, i1) => {
              return {
                a: "94171f33-10-" + i0 + "-" + i1 + ",94171f33-9",
                b: common_vendor.t(feature),
                c: feature
              };
            }),
            h: plan.id,
            i: plan.recommended ? 1 : "",
            j: selectedPlan.value === plan.id ? 1 : "",
            k: common_vendor.o(($event) => selectPlan(plan.id))
          });
        }),
        B: common_vendor.p({
          type: "checkmarkempty",
          size: "14",
          color: "#52c41a"
        }),
        C: !selectedPlan.value,
        D: common_vendor.o(purchaseMembership),
        E: common_vendor.p({
          title: "选择套餐",
          type: "line"
        })
      } : {
        F: common_vendor.t(formatDate(membershipInfo.expireDate)),
        G: common_vendor.o(renewMembership),
        H: common_vendor.p({
          title: "续费服务",
          type: "line"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-94171f33"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/membership/membership.js.map
