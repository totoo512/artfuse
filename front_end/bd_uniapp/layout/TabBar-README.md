# TabBar 图片添加详细指南

## 当前状态

当前 TabBar 使用的是 `uni-icons` 组件提供的矢量图标，同时在 `pages.json` 中配置了 static 目录下的图片资源作为备用。

## 图片资源位置

### 当前使用的图片
位置：`/static/` 目录
- `c3.png` - 个人中心（未选中）
- `c4.png` - 个人中心（选中）
- `c5.png` - 风格化生成（未选中）
- `c6.png` - 风格化生成（选中）
- `c7.png` - 看展解析（未选中）
- `c8.png` - 看展解析（选中）

### 新增图片建议位置
**推荐位置：** `/static/` 目录

**命名规范：**
```
tabbar-[功能名]-normal.png    // 未选中状态
tabbar-[功能名]-active.png    // 选中状态
```

**示例：**
```
/static/tabbar-home-normal.png
/static/tabbar-home-active.png
/static/tabbar-search-normal.png
/static/tabbar-search-active.png
```

## 图片规格要求

- **尺寸：** 48x48px 或 64x64px（推荐 64x64px 适配高清屏）
- **格式：** PNG（支持透明背景）
- **颜色：** 
  - 未选中状态：#7A7E83（灰色）
  - 选中状态：#3cc51f（绿色）

## 添加新 TabBar 项目的完整步骤

### 1. 准备图片资源
```bash
# 将图片放入 static 目录
/static/tabbar-newfeature-normal.png
/static/tabbar-newfeature-active.png
```

### 2. 创建对应页面
```bash
# 在 pages 目录下创建新页面
/pages/newfeature/newfeature.vue
```

### 3. 修改 pages.json
```json
{
  "pages": [
    // 添加新页面配置
    {
      "path": "pages/newfeature/newfeature",
      "style": {
        "navigationBarTitleText": "新功能"
      }
    }
  ],
  "tabBar": {
    "list": [
      // 添加新的 TabBar 项目
      {
        "pagePath": "pages/newfeature/newfeature",
        "iconPath": "static/tabbar-newfeature-normal.png",
        "selectedIconPath": "static/tabbar-newfeature-active.png",
        "text": "新功能"
      }
    ]
  }
}
```

### 4. 修改 TabBar.vue
在 `layout/TabBar.vue` 的 `list` 数组中添加新项目：

```javascript
const list = reactive([
  // 现有项目...
  {
    pagePath: "/pages/newfeature/newfeature",
    text: "新功能",
    icon: "home",  // uni-icons 图标名称
    // 如果使用自定义图片，添加以下字段：
    // normalIcon: "/static/tabbar-newfeature-normal.png",
    // selectedIcon: "/static/tabbar-newfeature-active.png"
  }
]);
```

## 图标实现方式对比

### 方式一：uni-icons（当前方式）
**优点：**
- 矢量图标，自适应大小和颜色
- 无需准备图片资源
- 文件体积小

**缺点：**
- 图标样式有限
- 无法完全自定义外观

**使用方法：**
```vue
<uni-icons :type="item.icon" size="24" :color="currentColor" />
```

### 方式二：自定义图片（推荐用于新增图标）
**优点：**
- 完全自定义设计
- 视觉效果更佳
- 品牌一致性更好

**缺点：**
- 需要准备多套图片
- 文件体积相对较大
- 需要适配不同状态

**使用方法：**
```vue
<image 
  :src="currentPage === item.pagePath ? item.selectedIcon : item.normalIcon" 
  class="tabbar-icon"
></image>
```

### 方式三：字体图标
**优点：**
- 矢量特性，无失真
- 可通过 CSS 控制颜色和大小
- 文件体积适中

**缺点：**
- 需要制作字体文件
- 配置相对复杂

**使用方法：**
参考现有的 `customicons.css` 和 `customicons.ttf`

## TODO 清单

### 立即可执行的任务
- [ ] 确定新增 TabBar 项目的功能需求
- [ ] 设计并制作对应的图标（48x48px 或 64x64px PNG）
- [ ] 将图片按命名规范放入 `/static/` 目录
- [ ] 创建对应的页面文件
- [ ] 在 `pages.json` 中添加页面和 TabBar 配置
- [ ] 在 `TabBar.vue` 中添加对应的配置项

### 优化建议
- [ ] 考虑统一使用自定义图片替代 uni-icons，提升视觉一致性
- [ ] 制作高清版本图片（@2x, @3x）适配不同设备
- [ ] 添加图标悬停或点击动画效果
- [ ] 考虑支持暗色模式的图标版本

### 长期规划
- [ ] 建立完整的图标设计规范
- [ ] 考虑使用 SVG 图标替代 PNG（更好的缩放性）
- [ ] 实现图标的懒加载优化
- [ ] 添加无障碍访问支持

## 注意事项

1. **页面路径一致性：** 确保 `TabBar.vue` 中的 `pagePath` 与 `pages.json` 中的路径完全一致
2. **图片路径：** 使用相对路径 `/static/xxx.png`，不要使用绝对路径
3. **图片优化：** 建议使用工具压缩图片以减少包体积
4. **测试覆盖：** 在不同设备和平台上测试 TabBar 显示效果
5. **备用方案：** 保留 uni-icons 作为备用方案，防止自定义图片加载失败

## 相关文件

- `layout/TabBar.vue` - TabBar 组件主文件
- `pages.json` - 页面和 TabBar 配置
- `static/` - 图片资源目录
- `static/customicons.css` - 自定义字体图标样式
- `static/customicons.ttf` - 自定义字体文件