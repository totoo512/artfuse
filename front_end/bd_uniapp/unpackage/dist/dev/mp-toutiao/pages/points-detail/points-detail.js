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
  __name: "points-detail",
  setup(__props) {
    const currentFilter = common_vendor.ref("all");
    const loading = common_vendor.ref(false);
    const hasMore = common_vendor.ref(true);
    const filterTabs = [
      { key: "all", label: "全部" },
      { key: "earn", label: "获得" },
      { key: "spend", label: "消费" }
    ];
    const userPoints = common_vendor.reactive({
      current: 1250,
      totalEarned: 3680,
      totalSpent: 2430
    });
    const records = common_vendor.reactive([
      {
        id: 1,
        type: "daily_signin",
        title: "每日签到",
        description: "连续签到第7天",
        amount: 20,
        createTime: "2024-01-20T09:00:00",
        orderId: null
      },
      {
        id: 2,
        type: "generation",
        title: "AIGC图片生成",
        description: "生成印象派风格人像",
        amount: -50,
        createTime: "2024-01-20T10:30:00",
        orderId: "GEN20240120001"
      },
      {
        id: 3,
        type: "share",
        title: "分享应用",
        description: "成功邀请好友注册",
        amount: 100,
        createTime: "2024-01-19T15:20:00",
        orderId: null
      },
      {
        id: 4,
        type: "generation",
        title: "AIGC视频生成",
        description: "生成抽象艺术视频",
        amount: -80,
        createTime: "2024-01-19T14:15:00",
        orderId: "GEN20240119002"
      },
      {
        id: 5,
        type: "task",
        title: "完成新手任务",
        description: "上传首张展品照片",
        amount: 50,
        createTime: "2024-01-18T16:45:00",
        orderId: null
      },
      {
        id: 6,
        type: "generation",
        title: "AIGC图片生成",
        description: "生成古典主义肖像",
        amount: -50,
        createTime: "2024-01-18T11:30:00",
        orderId: "GEN20240118003"
      },
      {
        id: 7,
        type: "daily_signin",
        title: "每日签到",
        description: "连续签到第6天",
        amount: 15,
        createTime: "2024-01-18T09:00:00",
        orderId: null
      },
      {
        id: 8,
        type: "recharge",
        title: "积分充值",
        description: "购买积分包（500积分）",
        amount: 500,
        createTime: "2024-01-17T20:15:00",
        orderId: "PAY20240117001"
      },
      {
        id: 9,
        type: "generation",
        title: "AIGC图片生成",
        description: "生成现代艺术拼贴",
        amount: -50,
        createTime: "2024-01-17T14:45:00",
        orderId: "GEN20240117004"
      },
      {
        id: 10,
        type: "exhibition",
        title: "AI看展解析",
        description: "解析展品《蒙娜丽莎》",
        amount: -20,
        createTime: "2024-01-17T13:20:00",
        orderId: "EXH20240117001"
      }
    ]);
    const filteredRecords = common_vendor.computed(() => {
      if (currentFilter.value === "earn") {
        return records.filter((record) => record.amount > 0);
      } else if (currentFilter.value === "spend") {
        return records.filter((record) => record.amount < 0);
      }
      return records;
    });
    const goBack = () => {
      common_vendor.index.navigateBack();
    };
    const switchFilter = (filterKey) => {
      currentFilter.value = filterKey;
    };
    const getRecordIcon = (type) => {
      const iconMap = {
        daily_signin: "calendar",
        generation: "color",
        share: "redo",
        task: "checkmarkempty",
        recharge: "wallet",
        exhibition: "eye"
      };
      return iconMap[type] || "help";
    };
    const formatTime = (timeStr) => {
      const date = new Date(timeStr);
      const now = /* @__PURE__ */ new Date();
      const diff = now - date;
      const minutes = Math.floor(diff / (1e3 * 60));
      const hours = Math.floor(diff / (1e3 * 60 * 60));
      const days = Math.floor(diff / (1e3 * 60 * 60 * 24));
      if (minutes < 1) {
        return "刚刚";
      } else if (minutes < 60) {
        return `${minutes}分钟前`;
      } else if (hours < 24) {
        return `${hours}小时前`;
      } else if (days < 7) {
        return `${days}天前`;
      } else {
        return `${date.getMonth() + 1}月${date.getDate()}日`;
      }
    };
    const loadMore = async () => {
      loading.value = true;
      setTimeout(() => {
        common_vendor.index.__f__("log", "at pages/points-detail/points-detail.vue:274", "加载更多积分记录");
        loading.value = false;
        hasMore.value = false;
      }, 1e3);
    };
    common_vendor.onMounted(() => {
      common_vendor.index.__f__("log", "at pages/points-detail/points-detail.vue:282", "积分明细页面加载完成");
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(goBack),
        b: common_vendor.p({
          title: "积分明细",
          border: false,
          ["background-color"]: "#ffffff",
          color: "#333333",
          ["left-icon"]: "back"
        }),
        c: common_vendor.t(userPoints.current),
        d: common_vendor.t(userPoints.totalEarned),
        e: common_vendor.t(userPoints.totalSpent),
        f: common_vendor.f(filterTabs, (tab, k0, i0) => {
          return {
            a: common_vendor.t(tab.label),
            b: tab.key,
            c: currentFilter.value === tab.key ? 1 : "",
            d: common_vendor.o(($event) => switchFilter(tab.key))
          };
        }),
        g: common_vendor.f(filteredRecords.value, (record, k0, i0) => {
          return common_vendor.e({
            a: "c7790286-1-" + i0,
            b: common_vendor.p({
              type: getRecordIcon(record.type),
              size: "20",
              color: record.amount > 0 ? "#52c41a" : "#ff4d4f"
            }),
            c: common_vendor.t(record.title),
            d: common_vendor.t(record.amount > 0 ? "+" : ""),
            e: common_vendor.t(record.amount),
            f: record.amount > 0 ? 1 : "",
            g: record.amount < 0 ? 1 : "",
            h: common_vendor.t(record.description),
            i: common_vendor.t(formatTime(record.createTime)),
            j: record.orderId
          }, record.orderId ? {
            k: common_vendor.t(record.orderId)
          } : {}, {
            l: record.id
          });
        }),
        h: filteredRecords.value.length === 0
      }, filteredRecords.value.length === 0 ? {
        i: common_vendor.p({
          type: "wallet",
          size: "60",
          color: "#ccc"
        })
      } : {}, {
        j: hasMore.value && filteredRecords.value.length > 0
      }, hasMore.value && filteredRecords.value.length > 0 ? {
        k: common_vendor.t(loading.value ? "加载中..." : "加载更多"),
        l: common_vendor.o(loadMore),
        m: loading.value
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c7790286"]]);
tt.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-toutiao/pages/points-detail/points-detail.js.map
