# WeCropper

截图，支持

* 旋转（动画）

* 截图框位置自动调整（动画）

* 旋转后截图框自动定位之前选中的区域

* 截图框样式自定义

* 截图框保持长宽比

## 注意

布局中不要使用wrap_content，因为图片和框都是可缩放的，View自己也不知道自己多大合适

## 使用

```xml

...
xmlns:app="http://schemas.android.com/apk/res-auto"
...

<com.welearn.cropper.ImageCropperView
  app:borderColor="#00bde5"
  app:borderThickness="4dp"
  ...
  />

```

或

```java

Configuration config = new Configuration.Builder(context)
            .setBorderColor(Color.WHITW)
            ...
            .build();
ImageCropView cropView = new ImageCropView(context, config);

```

## xml属性参考

```xml

    <declare-styleable name="ImageCropView">
        <!-- 选框距离屏幕左右两边留多少空隙 值为屏幕宽的比例 -->
        <attr name="paddingWidthRatio" format="float" />
        <!-- 选框距离屏幕上下两边留多少空隙 值为屏幕高的比例 -->
        <attr name="paddingHeightRatio" format="float" />
        <!-- 选框边的粗细 -->
        <attr name="borderThickness" format="dimension" />
        <!-- 选框边的可触摸范围 -->
        <attr name="borderTouchExtention" format="dimension" />
        <!-- 选框角的粗细 -->
        <attr name="cornerThickness" format="dimension" />
        <!-- 选框角的触摸范围 -->
        <attr name="cornerTouchExtention" format="dimension" />
        <!-- 选框角的长度 -->
        <attr name="cornerSize" format="dimension" />
        <!-- 相对图片原始大小，放大的最大比例 -->
        <attr name="imageMaxScale" format="float" />
        <!-- 选框最小宽度（除去角的长度） -->
        <attr name="cropWidthMin" format="dimension" />
        <!-- 选框最小高度（除去角的长度） -->
        <attr name="cropHeightMin" format="dimension" />
        <!-- 选框到达屏幕的多大时进行缩小 -->
        <attr name="scaleChangeMax" format="float" />
        <!-- 选框到达屏幕的多大时进行放大 -->
        <attr name="scaleChangeMin" format="float" />
        <!-- 选框边的颜色 -->
        <attr name="borderColor" format="color|reference" />
        <!-- 选框边的颜色 - 高亮 -->
        <attr name="borderColorHighLight" format="color|reference" />
        <!-- 选框角的颜色 -->
        <attr name="cornerColor" format="color|reference" />
        <!-- 选框角的颜色 - 高亮 -->
        <attr name="cornerColorHighLight" format="color|reference" />
        <!-- 遮罩颜色 -->
        <attr name="overlayColor" format="color|reference" />
        <!-- 网格线颜色 -->
        <attr name="gridLineColor" format="color|reference" />
        <!-- 网格线粗细 -->
        <attr name="gridThickness" format="dimension" />
        <!-- 网格行数 -->
        <attr name="gridLineRowCol" format="string" />
        <!-- 纵横比 -->
        <attr name="aspectRatio" format="float" />
        <!-- 是否保持纵横比 -->
        <attr name="keepAspectRatio" format="boolean" />
    </declare-styleable>

```

## 代码配置属性参考

```java

    /**
	 * 选框距离屏幕左右两边留多少空隙 值为屏幕宽的比例
	 */
	private float paddingWidthRatio;
	/**
	 * 选框距离屏幕上下两边留多少空隙 值为屏幕高的比例
	 */
	private float paddingHeightRatio;
	/**
	 * 选框边的粗细
	 */
	private int borderThickness;
	/**
	 * 选框边的可触摸范围
	 */
	private int borderTouchExtension;
	/**
	 * 选框角的粗细
	 */
	private int cornerThickness;
	/**
	 * 选框角的触摸范围
	 */
	private int cornerTouchExtension;
	/**
	 * 选框角的长度
	 */
	private int cornerSize;
	/**
	 * 相对图片原始大小，放大的最大比例
	 */
	private float imageMaxScale;
	/**
	 * 选框最小宽度（除去角的长度）
	 */
	private int cropWidthMin;
	/**
	 * 选框最小高度（除去角的长度）
	 */
	private int cropHeightMin;
	/**
	 * 选框到达屏幕的多大时进行缩小
	 */
	private float scaleChangeMax;
	/**
	 * 选框到达屏幕的多大时进行放大
	 */
	private float scaleChangeMin;
	/**
	 * 选框边的颜色
	 */
	private int borderColor;
	/**
	 * 选框边的颜色 - 高亮
	 */
	private int borderColorHighlight;
	/**
	 * 选框角的颜色
	 */
	private int cornerColor;
	/**
	 * 选框角的颜色 - 高亮
	 */
	private int cornerColorHighlight;
	/**
	 * 遮罩颜色
	 */
	private int overlayColor;
	/**
	 * 网格线颜色
	 */
	private int gridColor;
	/**
	 * 网格线粗细
	 */
	private int gridThickness;
	/**
	 * 网格行数
	 */
	private int gridRows;
	/**
	 * 网格线列数
	 */
	private int gridCols;
	/**
	 * 截图框从横比例
	 */
	private float aspectRatio;
	/**
	 * 保持纵横比
	 */
	private boolean keepAspectRatio;

```

## 可优化

* TouchSlop

* 多点多手势触控

* 任意角度的旋转