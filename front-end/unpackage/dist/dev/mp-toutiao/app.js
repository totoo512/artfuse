"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/ai-explore/ai-explore.js";
  "./pages/upload/upload.js";
}
const _sfc_main = {
  __name: "App",
  setup(__props) {
    common_vendor.onLaunch(() => {
      common_vendor.index.__f__("warn", "at App.vue:6", "当前组件仅支持 uni_modules 目录结构 ，请升级 HBuilderX 到 3.1.0 版本以上！");
      common_vendor.index.__f__("log", "at App.vue:7", "App Launch");
    });
    common_vendor.onShow(() => {
      common_vendor.index.__f__("log", "at App.vue:12", "App Show");
    });
    common_vendor.onHide(() => {
      common_vendor.index.__f__("log", "at App.vue:17", "App Hide");
    });
    return () => {
    };
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-toutiao/app.js.map
