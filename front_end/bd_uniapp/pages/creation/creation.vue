<template>
  <view class="creation-container">
    <!-- 
      TODO: 导航栏配置说明
      
      当前导航栏已注释，因为：
      1. 抖音小程序会自动生成导航栏和回退按钮
      2. 其他平台可根据需要启用uni-nav-bar组件
      
      如需自定义导航栏，可取消注释并配置：
      - title: 页面标题
      - background-color: 背景色
      - color: 文字颜色
      - left-icon: 左侧图标（back/close等）
      - @clickLeft: 左侧按钮点击事件
    -->
    <!-- <uni-nav-bar 
      title="AIGC风格化生成" 
      :border="false" 
      background-color="#ffffff"
      color="#333333"
      left-icon="back"
      @clickLeft="goBack"
    /> -->
    
    <!-- 
      TODO: 素材上传区域功能说明
      
      包含两个主要上传功能：
      1. 人物照片上传 - 单张，用于风格化处理的主体
      2. 看展照片上传 - 最多9张，提供风格参考
      
      支持的上传方式：
      - 相机拍摄
      - 相册选择
      - 从看展相册选择（需要实现相册页面）
    -->
    <!-- 素材上传区域 -->
    <uni-section title="上传素材" type="line">
      <!-- 
        TODO: 人物照片上传功能详解
        
        功能说明：
        - 仅支持单张照片上传
        - 照片将作为风格化处理的主体人物
        - 支持相机拍摄和相册选择
        - 自动压缩图片以优化性能
        
        优化建议：
        - 添加图片裁剪功能
        - 支持人脸检测和定位
        - 添加图片质量检查
        - 支持多种图片格式
      -->
      <!-- 人物照片上传 -->
      <view class="upload-item">
        <view class="upload-title">人物照片</view>
        <view class="upload-area" @click="uploadPortrait">
          <view v-if="!portraitImage" class="upload-placeholder">
            <uni-icons type="camera" size="40" color="#999"></uni-icons>
            <text class="upload-text">点击上传人物照片</text>
          </view>
          <image v-else :src="portraitImage" class="uploaded-image" mode="aspectFit"></image>
        </view>
      </view>
      
      <!-- 
        TODO: 看展照片上传功能详解
        
        功能说明：
        - 支持最多9张照片上传
        - 照片用于提供艺术风格参考
        - 支持批量选择和单独删除
        - 显示当前上传数量和限制
        
        交互功能：
        - 点击图片可预览
        - 点击删除按钮可移除
        - 网格布局展示
        - 添加按钮用于继续上传
        
        优化建议：
        - 添加拖拽排序功能
        - 支持图片压缩选项
        - 添加批量删除功能
        - 支持从云端相册选择
      -->
      <!-- 看展照片上传 -->
      <view class="upload-item">
        <view class="upload-title">看展照片 ({{ exhibitionImages.length }}/9)</view>
        <view class="exhibition-grid">
          <view 
            v-for="(image, index) in exhibitionImages" 
            :key="index" 
            class="exhibition-item"
            @click="previewImage(image, index)"
          >
            <image :src="image" class="exhibition-image" mode="aspectFill"></image>
            <view class="delete-btn" @click.stop="removeExhibitionImage(index)">
              <uni-icons type="close" size="16" color="#fff"></uni-icons>
            </view>
          </view>
          
          <view 
            v-if="exhibitionImages.length < 9" 
            class="add-exhibition-btn" 
            @click="uploadExhibitionImages"
          >
            <uni-icons type="plus" size="30" color="#999"></uni-icons>
          </view>
        </view>
        
        <view class="album-section">
          <view class="album-tip">
            <uni-icons type="info" size="16" color="#007aff"></uni-icons>
            <text class="tip-text">可从您的看展相册中选择照片</text>
          </view>
          <button 
            type="button" 
            @click="openMyAlbum"
            class="album-btn"
          >
            从我的看展相册选择
          </button>
        </view>
      </view>
    </uni-section>
    
    <!-- 
      TODO: 生成按钮功能详解
      
      功能说明：
      - 检查必要条件：人物照片 + 至少一张看展照片
      - 禁用状态：条件不满足时按钮不可点击
      - 加载状态：生成过程中显示加载效果
      - 动态文本：根据状态显示不同文字
      
      生成流程：
      1. 验证上传素材完整性
      2. 显示加载弹窗和进度提示
      3. 模拟AI处理过程（多阶段提示）
      4. 返回生成结果
      
      优化建议：
      - 添加真实的API调用
      - 实现进度条显示
      - 支持生成参数配置
      - 添加生成失败重试机制
    -->
    <!-- 生成按钮 -->
    <view class="generate-section">
      <button 
        type="button" 
        :disabled="!canGenerate" 
        @click="generateImages"
        class="generate-btn"
        :class="{ 'loading': isGenerating }"
      >
        {{ isGenerating ? '生成中...' : '开始生成风格化图片' }}
      </button>
    </view>
    
    <!-- 
      TODO: 生成结果展示功能详解
      
      功能说明：
      - 网格布局展示生成的多张图片
      - 支持点击选择图片
      - 选中状态有视觉反馈
      - 仅在有结果时显示
      
      交互功能：
      - 图片选择：点击图片进行选择
      - 选中标记：显示选中状态图标
      - 边框高亮：选中图片有蓝色边框
      
      扩展功能：
      - 视频生成选项（基于选中图片）
      - 智能文案生成
      - 图片保存和分享
      
      优化建议：
      - 添加图片放大预览
      - 支持多选功能
      - 添加图片质量评分
      - 支持重新生成选项
    -->
    <!-- 生成结果展示 -->
    <uni-section v-if="generatedImages.length > 0" title="生成结果" type="line">
      <view class="result-grid">
        <view 
          v-for="(image, index) in generatedImages" 
          :key="index" 
          class="result-item"
          :class="{ 'selected': selectedImageIndex === index }"
          @click="selectImage(index)"
        >
          <image :src="image" class="result-image" mode="aspectFill"></image>
          <view v-if="selectedImageIndex === index" class="selected-mark">
            <uni-icons type="checkmarkempty" size="20" color="#fff"></uni-icons>
          </view>
        </view>
      </view>
      
      <!-- 
        TODO: 视频生成选项功能详解
        
        功能说明：
        - 基于选中的图片生成动态视频
        - 开关控制是否启用视频生成
        - 仅在选中图片后显示
        - 提供时间预估和提示信息
        
        交互流程：
        1. 用户选择图片后显示选项
        2. 切换开关触发确认弹窗
        3. 确认后加入生成队列
        4. 后台处理并通知用户
        
        技术实现：
        - 异步任务队列管理
        - 进度通知机制
        - 结果存储和检索
        
        优化建议：
        - 添加视频预览功能
        - 支持视频参数配置（时长、效果等）
        - 实现队列状态查询
        - 添加视频质量选择
      -->
      <!-- 视频生成选项 -->
      <view v-if="selectedImageIndex !== -1" class="video-option">
        <view class="option-item">
          <text class="option-label">生成动态视频</text>
          <switch 
            :checked="enableVideoGeneration" 
            @change="toggleVideoGeneration"
            color="#007aff"
          />
        </view>
        <view v-if="enableVideoGeneration" class="video-tip">
          <uni-icons type="info" size="14" color="#ff9500"></uni-icons>
          <text class="tip-text">视频生成需要5-10分钟，完成后将通知您</text>
        </view>
      </view>
    </uni-section>
    
    <!-- 
      TODO: 智能文案生成功能详解
      
      功能说明：
      - 基于生成的图片自动生成社交媒体文案
      - 提供多个文案选项供用户选择
      - 支持一键复制功能
      - 仅在选中图片后显示
      
      文案特点：
      - 结合艺术风格和个人特色
      - 包含相关话题标签
      - 适合社交平台分享
      - 情感化和个性化表达
      
      交互功能：
      - 点击文案可选中（可扩展）
      - 复制按钮快速复制到剪贴板
      - 复制成功提示反馈
      
      优化建议：
      - 基于AI实时生成文案
      - 支持文案风格选择
      - 添加文案编辑功能
      - 支持自定义标签
      - 集成情感分析
    -->
    <!-- 文案生成 -->
    <uni-section v-if="selectedImageIndex !== -1" title="智能文案" type="line">
      <view class="caption-list">
        <view 
          v-for="(caption, index) in generatedCaptions" 
          :key="index" 
          class="caption-item"
          @click="selectCaption(index)"
        >
          <view class="caption-content">{{ caption }}</view>
          <button 
            type="button" 
            @click.stop="copyCaption(caption)"
            class="copy-btn"
          >
            复制
          </button>
        </view>
      </view>
    </uni-section>
    
    <!-- 
      TODO: 操作按钮区域功能详解
      
      功能说明：
      - 仅在选中图片后显示
      - 提供保存和分享两个主要操作
      - 使用栅格布局平分空间
      - 不同按钮样式区分功能重要性
      
      保存图片功能：
      - 下载选中的生成图片
      - 保存到设备相册
      - 需要相册写入权限
      - 提供保存结果反馈
      
      分享功能：
      - 支持分享到社交平台
      - 当前配置为微信朋友圈
      - 可扩展支持更多平台
      - 提供分享结果反馈
      
      优化建议：
      - 添加更多分享平台选项
      - 支持自定义分享文案
      - 添加分享数据统计
      - 实现批量保存功能
      - 支持生成带水印的图片
      - 添加图片编辑功能
    -->
    <!-- 操作按钮 -->
    <view v-if="selectedImageIndex !== -1" class="action-section">
      <uni-row :gutter="10">
        <uni-col :span="12">
          <button 
            type="button" 
            @click="saveImage"
            class="action-btn action-btn-default"
          >
            保存图片
          </button>
        </uni-col>
        <uni-col :span="12">
          <button 
            type="button" 
            @click="shareToSocial"
            class="action-btn action-btn-primary"
          >
            分享到社交平台
          </button>
        </uni-col>
      </uni-row>
    </view>
    
    <!-- 加载弹窗 -->
    <uni-popup ref="loadingPopup" type="center">
      <view class="loading-content">
        <uni-load-more status="loading" :content-text="{ contentdown: loadingText }"></uni-load-more>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
// TODO: 组件导入说明
// 
// Vue 3 Composition API:
// - ref: 创建响应式基本数据类型
// - reactive: 创建响应式对象
// - computed: 创建计算属性
// - onMounted: 组件挂载后的生命周期钩子
// 
// UniApp 生命周期:
// - onLoad: 页面加载时触发，可接收页面参数
// 
// UI 组件库:
// - uni-nav-bar: 自定义导航栏组件
// - uni-section: 分段器组件，用于内容分组
// - uni-icons: 图标组件
// - uni-row/uni-col: 栅格布局组件
// - uni-popup: 弹窗组件
// - uni-load-more: 加载更多组件
import { ref, reactive, computed, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import uniNavBar from '@/uni_modules/uni-nav-bar/components/uni-nav-bar/uni-nav-bar.vue'
import uniSection from '@/uni_modules/uni-section/components/uni-section/uni-section.vue'
import uniIcons from '@/uni_modules/uni-icons/components/uni-icons/uni-icons.vue'
import uniRow from '@/uni_modules/uni-row/components/uni-row/uni-row.vue'
import uniCol from '@/uni_modules/uni-row/components/uni-col/uni-col.vue'
import uniPopup from '@/uni_modules/uni-popup/components/uni-popup/uni-popup.vue'
import uniLoadMore from '@/uni_modules/uni-load-more/components/uni-load-more/uni-load-more.vue'

// TODO: 响应式数据结构说明
// 
// 数据分类：
// 1. 用户上传数据：portraitImage, exhibitionImages
// 2. 生成结果数据：generatedImages, generatedCaptions
// 3. 交互状态数据：selectedImageIndex, isGenerating, enableVideoGeneration
// 4. UI控制数据：loadingText, loadingPopup
// 5. 页面参数数据：selectedStyle
// 
// 数据类型说明：
// - ref(): 用于基本数据类型（string, number, boolean等）
// - reactive(): 用于对象和数组
// - computed(): 用于依赖其他数据的计算属性
// TODO: 核心数据定义详解
// 
// 用户上传数据：
const portraitImage = ref('')              // 人物照片路径，用于风格化处理的主体
const exhibitionImages = ref([])           // 看展照片数组，最多9张，提供风格参考

// 生成结果数据：
const generatedImages = ref([])            // AI生成的风格化图片数组
const selectedImageIndex = ref(-1)         // 当前选中的图片索引，-1表示未选中

// 交互状态数据：
const isGenerating = ref(false)            // 是否正在生成图片
const enableVideoGeneration = ref(false)   // 是否启用视频生成功能

// UI控制数据：
const loadingText = ref('')               // 加载弹窗显示的文本
const loadingPopup = ref(null)            // 加载弹窗组件引用

// 页面参数数据：
const selectedStyle = ref(null)           // 从上一页传递的风格参数

// TODO: 智能文案数据说明
// 
// 功能说明：
// - 预设的社交媒体文案模板
// - 包含艺术相关话题标签
// - 适合不同社交平台分享
// - 可根据生成图片动态调整
// 
// 扩展方向：
// - 接入AI文案生成API
// - 根据图片内容智能生成
// - 支持多语言文案
// - 个性化文案推荐
const generatedCaptions = ref([
  '今天在艺术的海洋里遇见了另一个自己 ✨ #艺术风格化 #AIGC',
  '当我遇上经典艺术，原来我也可以这么美 🎨 #风格化生成 #艺术创作',
  '穿越时空，与大师作品融为一体的奇妙体验 🌟 #AI艺术 #创意无限'
])

// TODO: 计算属性说明
// 
// canGenerate: 判断是否可以开始生成
// 条件：
// 1. 必须上传人物照片
// 2. 必须至少上传一张看展照片
// 
// 用途：
// - 控制生成按钮的启用状态
// - 防止无效的生成请求
// - 提供用户操作引导
const canGenerate = computed(() => {
  return portraitImage.value && exhibitionImages.value.length > 0
})

// TODO: 页面生命周期管理
// 
// onLoad 生命周期说明：
// - 页面加载时触发，只执行一次
// - 接收页面跳转时传递的参数
// - 适合进行页面初始化操作
// 
// 参数处理：
// - options.style: 从风格选择页传递的风格参数
// - 使用JSON编码传递复杂对象数据
// - 需要解码和异常处理确保数据完整性
// 
// 扩展功能：
// - 可添加页面访问统计
// - 可进行用户行为分析
// - 可预加载必要资源
// - 可恢复用户上次操作状态
onLoad((options) => {
  if (options.style) {
    try {
      selectedStyle.value = JSON.parse(decodeURIComponent(options.style))
      uni.showToast({
        title: `已选择${selectedStyle.value.name}风格`,
        icon: 'success'
      })
    } catch (e) {
      console.error('解析风格参数失败:', e)
    }
  }
})

// TODO: 页面导航方法说明
// 
// goBack(): 返回上一页
// - 使用uni.navigateBack()实现
// - 适用于页面栈导航
// - 保持页面状态和用户操作历史
// - 抖音小程序会自动生成返回按钮，此方法用于自定义导航
// 
// 注意事项：
// - 确保页面栈中有上一页可返回
// - 可添加返回前的数据保存逻辑
// - 支持传递返回参数给上一页
const goBack = () => {
  uni.navigateBack()
}

// TODO: 人物照片上传功能详解
// 
// 功能说明：
// - 上传单张人物照片作为风格化处理的主体
// - 支持相册选择和相机拍摄两种方式
// - 自动压缩图片以优化性能和存储
// 
// 技术实现：
// - 使用uni.chooseImage() API
// - count: 1 限制只能选择一张照片
// - sizeType: ['compressed'] 自动压缩图片
// - sourceType: ['camera', 'album'] 支持相机和相册
// 
// 扩展功能：
// - 可添加图片裁剪功能
// - 可进行人脸检测验证
// - 可添加图片质量检查
// - 可支持多种图片格式
// - 可添加图片预处理（滤镜、调色等）
const uploadPortrait = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['camera', 'album'],
    success: (res) => {
      portraitImage.value = res.tempFilePaths[0]
      uni.showToast({
        title: '上传成功',
        icon: 'success'
      })
    }
  })
}

// TODO: 看展照片上传功能详解
// 
// 功能说明：
// - 上传多张看展照片提供艺术风格参考
// - 最多支持9张照片，提供丰富的风格信息
// - 支持批量选择和单张添加
// 
// 技术实现：
// - 动态计算剩余可上传数量
// - 使用展开运算符合并新照片到现有数组
// - 完善的错误处理和用户提示
// 
// 业务逻辑：
// - 限制最大上传数量防止性能问题
// - 照片数量影响AI生成效果
// - 更多照片提供更丰富的风格参考
// 
// 扩展功能：
// - 可添加拖拽排序功能
// - 可进行艺术风格预分析
// - 可添加照片标签和分类
// - 可支持批量编辑和处理
const uploadExhibitionImages = () => {
  const remainingCount = 9 - exhibitionImages.value.length
  uni.chooseImage({
    count: remainingCount,
    sizeType: ['compressed'],
    sourceType: ['camera', 'album'],
    success: (res) => {
      exhibitionImages.value.push(...res.tempFilePaths)
      uni.showToast({
        title: '选择成功',
        icon: 'success'
      })
    }
  })
}

// TODO: 从看展相册选择功能详解
// 
// 功能说明：
// - 跳转到用户的个人看展相册页面
// - 允许用户从已保存的看展照片中选择
// - 避免重复上传，提高用户体验
// 
// 技术实现：
// - 使用uni.navigateTo()进行页面跳转
// - 可传递回调参数用于接收选择结果
// - 支持多选模式和数量限制
// 
// 业务价值：
// - 复用用户历史看展数据
// - 提供更便捷的照片选择方式
// - 增强用户粘性和应用价值
// 
// 扩展功能：
// - 可添加相册分类和标签筛选
// - 可支持云端相册同步
// - 可添加智能推荐功能
// - 可支持批量操作和管理
const openMyAlbum = () => {
  // 这里应该跳转到用户的看展相册页面
  uni.navigateTo({
    url: '/pages/album/album'
  })
}

// TODO: 看展照片预览功能详解
// 
// 功能说明：
// - 提供全屏预览看展照片的功能
// - 支持左右滑动浏览所有照片
// - 增强用户查看体验
// 
// 技术实现：
// - 使用uni.previewImage() API
// - urls: 传入所有照片数组
// - current: 指定当前预览的照片索引
// 
// 用户体验：
// - 支持缩放、旋转等手势操作
// - 自动适配不同屏幕尺寸
// - 提供流畅的切换动画
// 
// 扩展功能：
// - 可添加照片信息显示
// - 可支持照片编辑功能
// - 可添加分享和保存选项
const previewImage = (imageUrl, index) => {
  uni.previewImage({
    urls: exhibitionImages.value,
    current: index
  })
}

// TODO: 看展照片删除功能详解
// 
// 功能说明：
// - 允许用户删除不需要的看展照片
// - 支持单张删除操作
// - 实时更新照片数组和UI显示
// 
// 技术实现：
// - 使用数组splice()方法删除指定索引的元素
// - 响应式数据自动更新UI
// - 简单高效的删除逻辑
// 
// 用户体验：
// - 提供确认删除机制防止误操作
// - 支持批量删除功能
// - 可添加撤销删除功能
// 
// 扩展功能：
// - 可添加删除确认弹窗
// - 可支持拖拽删除手势
// - 可添加回收站功能
const removeExhibitionImage = (index) => {
  exhibitionImages.value.splice(index, 1)
}

// TODO: AI图片生成核心功能详解
// 
// 功能说明：
// - 核心的AI风格化图片生成功能
// - 结合人物照片和看展照片进行风格迁移
// - 提供完整的生成流程和状态管理
// 
// 技术实现：
// - 多阶段加载提示增强用户体验
// - 完善的错误处理和状态恢复
// - 使用setTimeout模拟异步生成过程
// 
// 业务流程：
// 1. 验证上传条件（人物照片+看展照片）
// 2. 显示加载状态和进度提示
// 3. 模拟AI处理过程（实际应接入真实AI服务）
// 4. 处理生成结果并更新UI
// 5. 提供用户反馈和后续操作
// 
// 生成阶段说明：
// - 人物特征分析：提取人物的面部特征和姿态
// - 艺术风格应用：将看展照片的艺术风格迁移到人物照片
// - 图片质量优化：提升生成图片的清晰度和艺术效果
// 
// 扩展功能：
// - 接入真实的AI生成API（如Stable Diffusion、DALL-E等）
// - 支持多种艺术风格选择和参数配置
// - 添加生成质量等级选择
// - 支持批量生成和高分辨率输出
// - 添加生成历史记录和收藏功能
const generateImages = () => {
  if (!canGenerate.value) return
  
  isGenerating.value = true
  loadingText.value = 'AI正在为您生成风格化图片...'
  loadingPopup.value?.open()
  
  // 模拟生成过程
  setTimeout(() => {
    loadingText.value = '正在分析人物特征...'
    
    setTimeout(() => {
      loadingText.value = '正在应用艺术风格...'
      
      setTimeout(() => {
        loadingText.value = '正在优化图片质量...'
        
        setTimeout(() => {
          // 模拟生成4张图片
          generatedImages.value = [
            '/static/c1.png',
            '/static/c2.png', 
            '/static/c3.png',
            '/static/c4.png'
          ]
          
          isGenerating.value = false
          loadingPopup.value?.close()
          
          uni.showToast({
            title: '生成完成！',
            icon: 'success'
          })
        }, 1500)
      }, 1500)
    }, 1500)
  }, 1500)
}

// TODO: 生成图片选择功能详解
// 
// 功能说明：
// - 允许用户选择满意的生成结果
// - 支持单选模式，点击切换选中状态
// - 为后续保存和分享操作提供目标
// 
// 技术实现：
// - 使用三元运算符实现选中状态切换
// - selectedImageIndex为-1表示未选中
// - 选中同一张图片时取消选中
// 
// 交互逻辑：
// - 点击已选中图片：取消选中
// - 点击未选中图片：选中该图片
// - 只能同时选中一张图片
// 
// 扩展功能：
// - 可支持多选模式
// - 可添加图片对比功能
// - 可支持图片评分和收藏
const selectImage = (index) => {
  selectedImageIndex.value = index
  uni.showToast({
    title: '已选择图片',
    icon: 'success'
  })
}

// TODO: 视频生成开关功能详解
// 
// 功能说明：
// - 控制是否启用视频生成功能
// - 基于选中图片生成动态视频内容
// - 提供异步任务队列管理
// 
// 技术实现：
// - 监听switch组件的change事件
// - 获取e.detail.value获取开关状态
// - 提供确认机制防止误操作
// 
// 业务流程：
// 1. 用户切换开关触发事件
// 2. 显示确认弹窗说明生成时间
// 3. 用户确认后提交到生成队列
// 4. 后台异步处理视频生成
// 5. 完成后通知用户查看结果
// 
// 视频生成特点：
// - 基于静态图片生成动态效果
// - 支持多种动画和转场效果
// - 适合社交媒体分享传播
// - 提供更丰富的视觉体验
// 
// 扩展功能：
// - 可添加视频时长选择
// - 可支持背景音乐配置
// - 可添加转场效果选择
// - 可支持视频质量设置
// - 可实现实时预览功能
const toggleVideoGeneration = (e) => {
  enableVideoGeneration.value = e.detail.value
  if (e.detail.value) {
    uni.showModal({
      title: '视频生成提示',
      content: '视频生成需要较长时间排队，预计5-10分钟，完成后将通知您，您可稍后在"我的"页面查看。',
      confirmText: '确定',
      cancelText: '取消',
      success: (res) => {
        if (res.confirm) {
          // TODO: 提交视频生成任务
          uni.showToast({
            title: '已加入生成队列',
            icon: 'success'
          })
        } else {
          enableVideoGeneration.value = false
        }
      }
    })
  }
}

// TODO: 智能文案选择功能详解
// 
// 功能说明：
// - 允许用户选择喜欢的文案模板
// - 为分享操作提供文案内容
// - 支持文案的个性化定制
// 
// 技术实现：
// - 接收文案索引参数
// - 可扩展为设置当前选中文案状态
// - 支持文案的进一步编辑和修改
// 
// 用户体验：
// - 提供多样化的文案选择
// - 减少用户创作文案的时间成本
// - 提高分享内容的质量和吸引力
// 
// 文案特色：
// - 结合艺术风格和个人特色
// - 包含热门话题标签
// - 适合不同社交平台的特点
// - 情感化和个性化表达
// 
// 扩展功能：
// - 可接入AI文案生成服务
// - 可根据图片内容智能推荐文案
// - 可支持文案的个性化编辑
// - 可添加文案模板库和收藏功能
// - 可支持多语言文案生成
const selectCaption = (index) => {
  // 可以添加选中效果
}

// TODO: 文案复制功能详解
// 
// 功能说明：
// - 将选中的文案复制到系统剪贴板
// - 方便用户在其他应用中粘贴使用
// - 提供便捷的文案分享方式
// 
// 技术实现：
// - 使用uni.setClipboardData() API
// - 提供成功反馈提升用户体验
// - 处理复制失败的异常情况
// 
// 用户场景：
// - 复制到社交媒体应用发布
// - 保存到备忘录供后续使用
// - 分享给朋友或群聊
// - 作为创作灵感的参考素材
// 
// 交互体验：
// - 点击复制按钮立即执行
// - 显示复制成功的Toast提示
// - 防止重复点击的节流处理
// 
// 扩展功能：
// - 可添加复制历史记录
// - 可支持富文本格式复制
// - 可添加一键分享到指定平台
// - 可支持批量复制多条文案
const copyCaption = (caption) => {
  uni.setClipboardData({
    data: caption,
    success: () => {
      uni.showToast({
        title: '文案已复制',
        icon: 'success'
      })
    }
  })
}

// TODO: 保存图片到相册功能详解
// 
// 功能说明：
// - 将生成的图片保存到用户设备相册
// - 支持高质量图片保存
// - 提供离线访问和分享能力
// 
// 技术实现：
// - 首先检查是否选中了图片
// - 使用uni.downloadFile()下载网络图片
// - 使用uni.saveImageToPhotosAlbum()保存到相册
// - 完善的成功和失败处理机制
// 
// 权限处理：
// - 首次使用需要申请相册写入权限
// - 权限被拒绝时提供引导说明
// - 支持权限状态检查和重新申请
// 
// 保存流程：
// 1. 验证是否选中图片
// 2. 获取选中图片的URL
// 3. 下载图片到本地临时目录
// 4. 将临时文件保存到系统相册
// 5. 提供用户反馈和状态提示
// 
// 扩展功能：
// - 可添加水印和签名
// - 可支持批量保存
// - 可选择保存质量和格式
// - 可添加保存位置选择
// - 可支持自定义文件命名
const saveImage = () => {
  if (selectedImageIndex.value === -1) return
  
  const imageUrl = generatedImages.value[selectedImageIndex.value]
  
  uni.downloadFile({
    url: imageUrl,
    success: (res) => {
      uni.saveImageToPhotosAlbum({
        filePath: res.tempFilePath,
        success: () => {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
        },
        fail: () => {
          uni.showToast({
            title: '保存失败',
            icon: 'none'
          })
        }
      })
    }
  })
}

// TODO: 社交平台分享功能详解
// 
// 功能说明：
// - 支持分享到多个社交媒体平台
// - 提供一键分享的便捷体验
// - 增加应用的社交传播能力
// 
// 支持平台：
// - 微信朋友圈、微信好友
// - 抖音、快手等短视频平台
// - 微博、QQ空间等社交网络
// - 小红书、Instagram等图片社交
// 
// 技术实现：
// - 使用uni.share() API进行平台分享
// - provider: 'weixin' 指定微信平台
// - scene: 'WXSceneTimeline' 指定朋友圈
// - type: 1 表示分享图片类型
// 
// 分享内容：
// - 生成的艺术风格化图片
// - 可结合选中的文案内容
// - 应用推广信息（可选）
// 
// 分享流程：
// 1. 验证是否选中了图片
// 2. 获取选中图片的URL
// 3. 调用平台分享接口
// 4. 处理分享结果和用户反馈
// 
// 扩展功能：
// - 可添加分享数据统计
// - 可支持自定义分享文案
// - 可添加分享奖励机制
// - 可支持分享模板定制
// - 可实现多平台同时分享
const shareToSocial = () => {
  if (selectedImageIndex.value === -1) return
  
  uni.share({
    provider: 'weixin',
    scene: 'WXSceneTimeline',
    type: 1,
    imageUrl: generatedImages.value[selectedImageIndex.value],
    success: () => {
      uni.showToast({
        title: '分享成功',
        icon: 'success'
      })
    },
    fail: () => {
      uni.showToast({
        title: '分享失败',
        icon: 'none'
      })
    }
  })
}

onMounted(() => {
  // 页面初始化
})
</script>

<style scoped>
/* TODO: 页面整体布局样式说明
 *
 * .creation-container: 创作页面主容器
 * - padding: 0 20rpx 左右边距，确保内容不贴边
 * - padding-bottom: 100rpx 底部预留空间，避免被底部TabBar遮挡
 * 
 * 设计理念：
 * - 简洁的容器设计，专注内容展示
 * - 底部预留空间确保用户可以完整查看所有内容
 * - 响应式设计适配不同屏幕尺寸
 */
.creation-container {
  padding: 0 20rpx;
  padding-bottom: 100rpx;
}

/* TODO: 上传模块样式说明
 *
 * .upload-item: 单个上传项的容器
 * - margin-bottom: 30rpx 模块间距，保持视觉分离
 * 
 * .upload-title: 上传区域标题
 * - font-size: 32rpx 适中的标题字体大小
 * - font-weight: bold 粗体突出重要性
 * - color: #333 深灰色确保良好可读性
 * 
 * .upload-area: 上传区域主体
 * - width: 100% 占满容器宽度
 * - height: 200rpx 固定高度提供足够的点击区域
 * - border: 2rpx dashed #ddd 虚线边框暗示可上传
 * - background-color: #fafafa 浅灰背景区分于页面背景
 * - 弹性布局居中对齐上传提示内容
 * 
 * 交互设计：
 * - 虚线边框符合用户对上传区域的认知
 * - 居中布局提供清晰的视觉焦点
 * - 足够的高度确保良好的点击体验
 */
.upload-item {
  margin-bottom: 30rpx;
}

.upload-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
  color: #333;
}

.upload-area {
  width: 100%;
  height: 200rpx;
  border: 2rpx dashed #ddd;
  border-radius: 10rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
}

/* TODO: 上传状态样式说明
 *
 * .upload-placeholder: 上传提示内容容器
 * - 垂直排列图标和文字
 * - 居中对齐提供清晰的视觉引导
 * 
 * .upload-text: 上传提示文字
 * - font-size: 24rpx 较小字体，作为辅助说明
 * - color: #999 浅灰色，降低视觉权重
 * 
 * .uploaded-image: 已上传图片样式
 * - 占满上传区域，替换上传提示
 * - 圆角设计保持视觉一致性
 * 
 * 状态切换：
 * - 未上传：显示虚线框和上传提示
 * - 已上传：显示图片预览，隐藏上传提示
 */
.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-text {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  border-radius: 8rpx;
}

/* TODO: 看展照片网格布局样式说明
 *
 * .exhibition-grid: 看展照片网格容器
 * - display: grid 使用CSS Grid布局
 * - grid-template-columns: repeat(3, 1fr) 三列等宽布局
 * - gap: 15rpx 网格间距，保持视觉平衡
 * - margin-bottom: 20rpx 与下方内容保持间距
 * 
 * 布局优势：
 * - 三列布局最大化利用屏幕空间
 * - 等宽设计保持视觉一致性
 * - Grid布局自动处理换行和对齐
 * - 响应式设计适配不同屏幕尺寸
 */
.exhibition-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15rpx;
  margin-bottom: 20rpx;
}

/* TODO: 看展照片项目样式说明
 *
 * .exhibition-item: 单个看展照片容器
 * - position: relative 为删除按钮提供定位基准
 * - aspect-ratio: 1 保持正方形比例
 * - border-radius: 8rpx 圆角设计增加美观性
 * - overflow: hidden 确保图片不超出容器边界
 * 
 * .exhibition-image: 看展照片图片
 * - 占满容器，保持图片完整显示
 * 
 * .delete-btn: 删除按钮
 * - position: absolute 绝对定位在右上角
 * - 半透明黑色背景确保在各种图片上都可见
 * - 圆形设计符合现代UI趋势
 * - 弹性布局居中对齐删除图标
 * 
 * 交互设计：
 * - 正方形布局保持视觉整齐
 * - 删除按钮位置固定，操作便捷
 * - 半透明设计不过分干扰图片内容
 */
.exhibition-item {
  position: relative;
  aspect-ratio: 1;
  border-radius: 8rpx;
  overflow: hidden;
}

.exhibition-image {
  width: 100%;
  height: 100%;
}

.delete-btn {
  position: absolute;
  top: 5rpx;
  right: 5rpx;
  width: 30rpx;
  height: 30rpx;
  background-color: rgba(0,0,0,0.6);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* TODO: 添加看展照片按钮样式说明
 *
 * .add-exhibition-btn: 添加更多看展照片的按钮
 * - aspect-ratio: 1 与照片项目保持相同比例
 * - border: 2rpx dashed #ddd 虚线边框暗示可添加
 * - background-color: #fafafa 浅灰背景区分于照片
 * - 弹性布局居中对齐添加图标
 * 
 * 设计理念：
 * - 与照片项目保持一致的尺寸和布局
 * - 虚线边框符合用户对添加操作的认知
 * - 浅色背景不干扰已有照片的视觉效果
 * - 居中的加号图标提供清晰的操作提示
 */
.add-exhibition-btn {
  aspect-ratio: 1;
  border: 2rpx dashed #ddd;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
}

/* TODO: 相册选择区域样式说明
 *
 * .album-section: 相册选择区域容器
 * - margin-top: 20rpx 与上方内容保持间距
 * 
 * .album-tip: 相册提示信息容器
 * - 水平排列图标和提示文字
 * - 为用户提供操作说明
 * 
 * .tip-text: 提示文字样式
 * - font-size: 24rpx 较小字体，作为辅助说明
 * - color: #666 中等灰色，适中的视觉权重
 * - margin-left: 8rpx 与图标保持适当间距
 * 
 * .album-btn: 相册选择按钮
 * - width: 100% 占满容器宽度
 * - padding: 20rpx 提供足够的点击区域
 * - background-color: #f8f8f8 浅灰背景区分于主要操作
 * - border: 1rpx solid #ddd 细边框增加层次感
 * - 圆角设计保持视觉一致性
 * 
 * 交互设计：
 * - 全宽按钮提供良好的点击体验
 * - 浅色背景表明这是次要操作
 * - 清晰的边框和圆角符合现代UI设计
 */
.album-section {
  margin-top: 20rpx;
}

.album-tip {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.tip-text {
  font-size: 24rpx;
  color: #666;
  margin-left: 8rpx;
}

.album-btn {
  width: 100%;
  padding: 20rpx;
  background-color: #f8f8f8;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #333;
}

/* TODO: 生成按钮区域样式说明
 *
 * .generate-section: 生成按钮区域容器
 * - margin: 40rpx 0 上下较大间距，突出重要性
 * 
 * .generate-btn: 主要生成按钮
 * - width: 100% 占满容器宽度
 * - height: 80rpx 较高的按钮提供良好的点击体验
 * - font-size: 32rpx 较大字体突出重要性
 * - background-color: #007aff 系统蓝色，表明主要操作
 * - color: white 白色文字确保良好对比度
 * - border: none 无边框的现代设计
 * - border-radius: 8rpx 圆角设计保持一致性
 * 
 * 按钮状态样式：
 * - :disabled 禁用状态：灰色背景和文字
 * - .loading 加载状态：灰色背景表明正在处理
 * 
 * 设计理念：
 * - 主要操作使用系统色彩突出重要性
 * - 较大的尺寸和字体确保易于操作
 * - 状态变化提供清晰的视觉反馈
 * - 全宽设计符合移动端操作习惯
 */
.generate-section {
  margin: 40rpx 0;
}

.generate-btn {
  width: 100%;
  height: 80rpx;
  font-size: 32rpx;
  background-color: #007aff;
  color: white;
  border: none;
  border-radius: 8rpx;
}

.generate-btn:disabled {
  background-color: #ccc;
  color: #999;
}

.generate-btn.loading {
  background-color: #ccc;
}

/* TODO: 生成结果展示样式说明
 *
 * .result-grid: 生成结果网格容器
 * - display: grid 使用CSS Grid布局
 * - grid-template-columns: repeat(2, 1fr) 两列等宽布局
 * - gap: 15rpx 网格间距，保持视觉平衡
 * - margin-bottom: 30rpx 与下方内容保持间距
 * 
 * 布局设计：
 * - 两列布局平衡了展示效果和图片大小
 * - 比看展照片的三列布局更大，突出生成结果
 * - Grid布局确保整齐的排列和自动换行
 * 
 * .result-item: 单个生成结果容器
 * - position: relative 为选中标记提供定位基准
 * - aspect-ratio: 1 保持正方形比例
 * - border-radius: 12rpx 较大圆角增加精致感
 * - overflow: hidden 确保图片不超出容器
 * - border: 3rpx solid transparent 预留选中边框空间
 * 
 * .result-item.selected: 选中状态样式
 * - border-color: #007aff 系统蓝色边框表明选中
 * 
 * .result-image: 生成结果图片
 * - 占满容器，完整展示生成效果
 * 
 * .selected-mark: 选中标记
 * - position: absolute 绝对定位在右上角
 * - 圆形蓝色背景，内含对勾图标
 * - 提供清晰的选中状态视觉反馈
 * 
 * 交互设计：
 * - 较大的圆角增加精致感，突出生成结果的重要性
 * - 双重选中反馈：边框颜色 + 选中标记
 * - 透明边框预留空间，避免选中时布局跳动
 */
.result-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15rpx;
  margin-bottom: 30rpx;
}

.result-item {
  position: relative;
  aspect-ratio: 1;
  border-radius: 12rpx;
  overflow: hidden;
  border: 3rpx solid transparent;
}

.result-item.selected {
  border-color: #007aff;
}

.result-image {
  width: 100%;
  height: 100%;
}

.selected-mark {
  position: absolute;
  top: 10rpx;
  right: 10rpx;
  width: 40rpx;
  height: 40rpx;
  background-color: #007aff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* TODO: 视频生成选项样式说明
 *
 * .video-option: 视频生成选项容器
 * - margin-bottom: 30rpx 与下方内容保持间距
 * 
 * .option-item: 选项项目容器
 * - display: flex 弹性布局
 * - justify-content: space-between 两端对齐
 * - align-items: center 垂直居中对齐
 * - padding: 20rpx 0 上下内边距提供点击区域
 * 
 * .option-label: 选项标签文字
 * - font-size: 28rpx 适中的字体大小
 * - color: #333 深灰色确保可读性
 * 
 * .video-tip: 视频生成提示信息
 * - 水平排列图标和提示文字
 * - background-color: #fff7e6 浅橙色背景突出提示
 * - padding: 15rpx 20rpx 内边距确保内容舒适
 * - border-radius: 8rpx 圆角设计保持一致性
 * - margin-top: 10rpx 与选项保持间距
 * 
 * 设计理念：
 * - 清晰的选项布局便于用户理解和操作
 * - 彩色提示背景吸引用户注意重要信息
 * - 弹性布局确保在不同内容长度下的良好表现
 */
.video-option {
  margin-bottom: 30rpx;
}

.option-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
}

.option-label {
  font-size: 28rpx;
  color: #333;
}

.video-tip {
  display: flex;
  align-items: center;
  padding: 15rpx 20rpx;
  background-color: #fff7e6;
  border-radius: 8rpx;
  margin-top: 10rpx;
}

/* TODO: 智能文案列表样式说明
 *
 * .caption-list: 文案列表容器
 * - margin-bottom: 30rpx 与下方内容保持间距
 * 
 * .caption-item: 单个文案项目容器
 * - display: flex 弹性布局
 * - justify-content: space-between 两端对齐
 * - align-items: center 垂直居中对齐
 * - padding: 20rpx 内边距确保内容舒适
 * - background-color: #f8f8f8 浅灰背景区分于页面背景
 * - border-radius: 8rpx 圆角设计增加美观性
 * - margin-bottom: 15rpx 项目间距保持分离
 * 
 * .caption-content: 文案内容文字
 * - flex: 1 占据剩余空间
 * - font-size: 26rpx 适中的阅读字体
 * - color: #333 深灰色确保可读性
 * - line-height: 1.4 行高确保良好的阅读体验
 * - margin-right: 20rpx 与复制按钮保持间距
 * 
 * .copy-btn: 复制按钮
 * - padding: 10rpx 20rpx 适中的内边距
 * - background-color: #f8f8f8 与项目背景一致
 * - border: 1rpx solid #ddd 细边框增加层次感
 * - border-radius: 6rpx 较小圆角符合按钮特征
 * - font-size: 24rpx 较小字体，作为辅助操作
 * 
 * 交互设计：
 * - 卡片式设计便于阅读和操作
 * - 复制按钮位置固定，操作便捷
 * - 良好的行高和字体大小确保阅读舒适
 */
.caption-list {
  margin-bottom: 30rpx;
}

.caption-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: #f8f8f8;
  border-radius: 8rpx;
  margin-bottom: 15rpx;
}

.caption-content {
  flex: 1;
  font-size: 26rpx;
  color: #333;
  line-height: 1.4;
  margin-right: 20rpx;
}

.copy-btn {
  padding: 10rpx 20rpx;
  background-color: #f8f8f8;
  border: 1rpx solid #ddd;
  border-radius: 6rpx;
  font-size: 24rpx;
  color: #333;
}

/* TODO: 操作按钮区域样式说明
 *
 * .action-section: 操作按钮区域容器
 * - margin-top: 30rpx 与上方内容保持间距
 * 
 * .action-btn: 操作按钮基础样式
 * - width: 100% 占满容器宽度
 * - height: 70rpx 适中的按钮高度
 * - border: none 无边框的现代设计
 * - border-radius: 8rpx 圆角设计保持一致性
 * - font-size: 28rpx 适中的字体大小
 * 
 * .action-btn-default: 默认按钮样式
 * - background-color: #f8f8f8 浅灰背景表明次要操作
 * - color: #333 深灰文字确保可读性
 * - border: 1rpx solid #ddd 细边框增加层次感
 * 
 * .action-btn-primary: 主要按钮样式
 * - background-color: #007aff 系统蓝色突出重要性
 * - color: white 白色文字确保良好对比度
 * 
 * .loading-content: 加载弹窗内容样式
 * - padding: 40rpx 较大内边距确保内容舒适
 * - background-color: #fff 白色背景突出弹窗
 * - border-radius: 12rpx 较大圆角增加精致感
 * - text-align: center 居中对齐加载内容
 * 
 * 设计理念：
 * - 按钮样式区分操作重要性
 * - 全宽设计符合移动端操作习惯
 * - 加载弹窗提供清晰的状态反馈
 */
.action-section {
  margin-top: 30rpx;
}

.action-btn {
  width: 100%;
  height: 70rpx;
  border: none;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.action-btn-default {
  background-color: #f8f8f8;
  color: #333;
  border: 1rpx solid #ddd;
}

.action-btn-primary {
  background-color: #007aff;
  color: white;
}

.loading-content {
  padding: 40rpx;
  background-color: #fff;
  border-radius: 12rpx;
  text-align: center;
}
</style>