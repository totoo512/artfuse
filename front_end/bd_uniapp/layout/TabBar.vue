<template>
  <view class="tabbar-container">
    <view
      v-for="item in list"
      :key="item.pagePath"
      class="tabbar-item"
      @click="switchTab(item)"
    >
      <!-- 
        TODO: TabBar图标添加指南
        
        方式一：使用uni-icons（当前方式）
        - 优点：矢量图标，自适应大小和颜色
        - 缺点：图标样式有限
        - 图标库：https://uniapp.dcloud.net.cn/component/uniui/uni-icons.html
        
        方式二：使用自定义图片（推荐用于新增图标）
        - 将新图片放在 /static/ 目录下
        - 建议命名规范：tabbar-[功能名]-[状态].png
        - 例如：tabbar-home-active.png, tabbar-home-normal.png
        - 图片尺寸建议：48x48px 或 64x64px（高清屏适配）
        
        方式三：使用字体图标
        - 将字体文件放在 /static/ 目录下
        - 在 customicons.css 中定义新图标类
        - 参考现有的 customicons.ttf 和 customicons.css
      -->
      
      <!-- 当前使用uni-icons，如需使用自定义图片，请替换为下面的image标签 -->
      <uni-icons :type="item.icon" size="24" :color="currentPage === item.pagePath ? selectedColor : color" />
      
      <!-- 
        使用自定义图片的示例（取消注释并修改item数据结构）：
        <image 
          :src="currentPage === item.pagePath ? item.selectedIcon : item.normalIcon" 
          class="tabbar-icon"
        ></image>
      -->
      
      <text class="text" :style="{ color: currentPage === item.pagePath ? selectedColor : color }">{{ item.text }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import uniIcons from '@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue';

const color = ref("#7A7E83");
const selectedColor = ref("#3cc51f");

const currentPage = computed(() => {
    const pages = getCurrentPages();
    if (pages.length) {
        return '/' + pages[pages.length - 1].route;
    }
    return '';
});

// TODO: TabBar配置详细说明
// 
// 当前数据结构（使用uni-icons）：
// {
//   pagePath: "/pages/xxx/xxx",  // 页面路径，必须在pages.json中配置为tabBar页面
//   text: "显示文本",              // TabBar显示的文字
//   icon: "图标名称"               // uni-icons的图标名称
// }
//
// 如果要使用自定义图片，请修改为以下结构：
// {
//   pagePath: "/pages/xxx/xxx",
//   text: "显示文本",
//   normalIcon: "/static/tabbar-xxx-normal.png",    // 未选中状态的图片
//   selectedIcon: "/static/tabbar-xxx-active.png"   // 选中状态的图片
// }
//
// 添加新TabBar项目的步骤：
// 1. 在此数组中添加新的配置对象
// 2. 在pages.json的tabBar.list中添加对应配置
// 3. 确保pagePath指向的页面已创建
// 4. 如使用自定义图片，将图片文件放在/static/目录下

const list = reactive([
  {
    pagePath: "/pages/stylegen/stylegen",
    text: "风格化生成",
    icon: "color",
    // 如使用自定义图片，添加以下字段：
    // normalIcon: "/static/tabbar-stylegen-normal.png",
    // selectedIcon: "/static/tabbar-stylegen-active.png"
  },
  {
    pagePath: "/pages/exhibition/exhibition",
    text: "看展解析",
    icon: "eye",
    // 如使用自定义图片，添加以下字段：
    // normalIcon: "/static/tabbar-exhibition-normal.png",
    // selectedIcon: "/static/tabbar-exhibition-active.png"
  },
  {
    pagePath: "/pages/profile/profile",
    text: "我的",
    icon: "person",
    // 如使用自定义图片，添加以下字段：
    // normalIcon: "/static/tabbar-profile-normal.png",
    // selectedIcon: "/static/tabbar-profile-active.png"
  },
  // TODO: 添加新的TabBar项目示例
  // {
  //   pagePath: "/pages/newpage/newpage",
  //   text: "新功能",
  //   icon: "home",  // 或使用自定义图片
  //   // normalIcon: "/static/tabbar-newpage-normal.png",
  //   // selectedIcon: "/static/tabbar-newpage-active.png"
  // }
]);

const switchTab = (item) => {
  uni.switchTab({
    url: item.pagePath,
  });
};
</script>

<style scoped>
.tabbar-container {
  display: flex;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50px;
  background-color: #ffffff;
  border-top: 1px solid #e0e0e0;
  z-index: 999;
}
.tabbar-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.text {
  font-size: 12px;
}

/* TODO: 自定义图片图标样式 */
/* 如果使用自定义图片替代uni-icons，请取消注释并调整以下样式 */
/*
.tabbar-icon {
  width: 24px;
  height: 24px;
  margin-bottom: 2px;
}
*/

/* TODO: TabBar样式自定义选项 */
/* 
可自定义的样式属性：
- .tabbar-container height: 调整TabBar高度
- .tabbar-container background-color: 调整背景色
- .tabbar-container border-top: 调整顶部边框
- .text font-size: 调整文字大小
- .text margin-top: 调整图标与文字间距

响应式适配：
- 可根据设备类型调整图标大小和TabBar高度
- 建议在不同屏幕尺寸下测试显示效果
*/
</style>