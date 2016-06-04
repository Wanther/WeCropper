package cn.wanther.cropper.core;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/*
 * TODO: 有些规则或者操作性的配置可以用回调的方式
 */
public class Configuration {

	public static final float DEFAULT_PADDING_WIDTH_RATIO = 0.1f;
	public static final float DEFAULT_PADDING_HEIGHT_RATIO = 0.05f;
	public static final int DEFAULT_BORDER_THICKNESS_DP = 1;
	public static final int DEFAULT_BORDER_TOUCH_EXTENTION_DP = 20;
	public static final int DEFAULT_CORNER_THICKNESS_DP = 2;
	public static final int DEFAULT_CORDER_SIZE_DP = 16;
	public static final int DEFAULT_CORNER_TOUCH_EXTENSION_DP = 20;
	public static final float DEFAULT_IMAGE_MAX_SCALE = 2;
	public static final int DEFAULT_CROP_WIDTH_MIN_DP = 20;
	public static final int DEFAULT_CROP_HEIGHT_MIN_DP = 20;
	public static final float DEFAULT_SCALE_CHANGE_MAX = 0.75f;
	public static final float DEFAULT_SCALE_CHANGE_MIN = 0.45f;
	public static final int DEFAULT_BORDER_COLOR = Color.WHITE;
	public static final int DEFAULT_BORDER_COLOR_HIGHLIGHT = Color.GREEN;
	public static final int DEFAULT_CORNER_COLOR = DEFAULT_BORDER_COLOR;
	public static final int DEFAULT_CORNER_COLOR_HIGHLIGHT = DEFAULT_BORDER_COLOR_HIGHLIGHT;
	public static final int DEFAULT_OVERLAY_COLOR = 0xCC000000;
	public static final int DEFAULT_GRID_COLOR = DEFAULT_BORDER_COLOR;
	public static final int DEFAULT_GRID_THICKNESS_DP = 1;
	public static final float DEFAULT_ASPECT_RARIO = 1.618f;

	public static Configuration createDefault(Context context){

		// DP 转化 像素

		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

		int borderThickness = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_THICKNESS_DP, displayMetrics));
		int borderTouchExtension = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_TOUCH_EXTENTION_DP, displayMetrics));
		int cornerSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORDER_SIZE_DP, displayMetrics));
		int cornerThickness = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_THICKNESS_DP, displayMetrics));
		int cornerTouchExtension = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_TOUCH_EXTENSION_DP, displayMetrics));
		int cropWidthMin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CROP_WIDTH_MIN_DP, displayMetrics));
		int cropHeightMin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CROP_HEIGHT_MIN_DP, displayMetrics));
		int gridThickness = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GRID_THICKNESS_DP, displayMetrics));

		Configuration config = new Configuration();

		config.setPaddingWidthRatio(DEFAULT_PADDING_WIDTH_RATIO);
		config.setPaddingHeightRatio(DEFAULT_PADDING_HEIGHT_RATIO);
		config.setBorderThickness(borderThickness);
		config.setBorderTouchExtension(borderTouchExtension);
		config.setBorderColor(DEFAULT_BORDER_COLOR);
		config.setBorderColorHighlight(DEFAULT_BORDER_COLOR_HIGHLIGHT);
		config.setCornerThickness(cornerThickness);
		config.setCornerSize(cornerSize);
		config.setCornerTouchExtension(cornerTouchExtension);
		config.setCornerColor(DEFAULT_CORNER_COLOR);
		config.setCornerColorHighlight(DEFAULT_CORNER_COLOR_HIGHLIGHT);
		config.setImageMaxScale(DEFAULT_IMAGE_MAX_SCALE);
		config.setCropWidthMin(cropWidthMin);
		config.setCropHeightMin(cropHeightMin);
		config.setScaleChangeMax(DEFAULT_SCALE_CHANGE_MAX);
		config.setScaleChangeMin(DEFAULT_SCALE_CHANGE_MIN);
		config.setOverlayColor(DEFAULT_OVERLAY_COLOR);
		config.setAspectRatio(DEFAULT_ASPECT_RARIO);
		config.setGridColor(DEFAULT_GRID_COLOR);
		config.setGridThickness(gridThickness);
		config.setGridCols(0);
		config.setGridRows(0);

		return config;
	}

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

	private Configuration(){}

	public float getPaddingWidthRatio() {
		return paddingWidthRatio;
	}

	protected void setPaddingWidthRatio(float paddingWidthRatio) {
		this.paddingWidthRatio = paddingWidthRatio;
	}

	public float getPaddingHeightRatio() {
		return paddingHeightRatio;
	}

	protected void setPaddingHeightRatio(float paddingHeightRatio) {
		this.paddingHeightRatio = paddingHeightRatio;
	}

	public int getBorderThickness() {
		return borderThickness;
	}

	protected void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
	}

	public int getBorderTouchExtension() {
		return borderTouchExtension;
	}

	protected void setBorderTouchExtension(int borderTouchExtension) {
		this.borderTouchExtension = borderTouchExtension;
	}

	public int getCornerThickness() {
		return cornerThickness;
	}

	protected void setCornerThickness(int cornerThickness) {
		this.cornerThickness = cornerThickness;
	}

	public int getCornerTouchExtension() {
		return cornerTouchExtension;
	}

	protected void setCornerTouchExtension(int cornerTouchExtension) {
		this.cornerTouchExtension = cornerTouchExtension;
	}

	public int getCornerSize() {
		return cornerSize;
	}

	protected void setCornerSize(int cornerSize) {
		this.cornerSize = cornerSize;
	}

	public float getImageMaxScale() {
		return imageMaxScale;
	}

	protected void setImageMaxScale(float imageMaxScale) {
		this.imageMaxScale = imageMaxScale;
	}

	public int getCropWidthMin() {
		return cropWidthMin;
	}

	protected void setCropWidthMin(int cropWidthMin) {
		this.cropWidthMin = cropWidthMin;
	}

	public int getCropHeightMin() {
		return cropHeightMin;
	}

	protected void setCropHeightMin(int cropHeightMin) {
		this.cropHeightMin = cropHeightMin;
	}

	public float getScaleChangeMax() {
		return scaleChangeMax;
	}

	protected void setScaleChangeMax(float scaleChangeMax) {
		this.scaleChangeMax = scaleChangeMax;
	}

	public float getScaleChangeMin() {
		return scaleChangeMin;
	}

	protected void setScaleChangeMin(float scaleChangeMin) {
		this.scaleChangeMin = scaleChangeMin;
	}

	public int getBorderColor() {
		return borderColor;
	}

	protected void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public int getBorderColorHighlight() {
		return borderColorHighlight;
	}

	protected void setBorderColorHighlight(int borderColorHighlight) {
		this.borderColorHighlight = borderColorHighlight;
	}

	public int getCornerColor() {
		return cornerColor;
	}

	protected void setCornerColor(int cornerColor) {
		this.cornerColor = cornerColor;
	}

	public int getCornerColorHighlight() {
		return cornerColorHighlight;
	}

	protected void setCornerColorHighlight(int cornerColorHighlight) {
		this.cornerColorHighlight = cornerColorHighlight;
	}

	public int getOverlayColor() {
		return overlayColor;
	}

	protected void setOverlayColor(int color) {
		this.overlayColor = color;
	}

	public int getGridColor() {
		return gridColor;
	}

	protected void setGridColor(int gridColor) {
		this.gridColor = gridColor;
	}

	public int getGridThickness() {
		return gridThickness;
	}

	protected void setGridThickness(int gridThickness) {
		this.gridThickness = gridThickness;
	}

	public int getGridRows() {
		return gridRows;
	}

	protected void setGridRows(int gridRows) {
		this.gridRows = gridRows;
	}

	public int getGridCols() {
		return gridCols;
	}

	protected void setGridCols(int gridCols) {
		this.gridCols = gridCols;
	}

	protected void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public boolean isKeepAspectRatio() {
		return keepAspectRatio && aspectRatio > 0;
	}

	protected void setKeepAspectRatio(boolean keepAspectRatio) {
		this.keepAspectRatio = keepAspectRatio;
	}

	public boolean isShowGrid() {
		return gridRows > 0 || gridCols > 0;
	}

	public static class Builder{
		private Configuration configing;

		private Context context;

		public Builder(Context context){
			this.context = context;
			configing = Configuration.createDefault(context);
		}

		public Builder setPaddingWidthRatio(float paddingWidthRatio) {
			configing.setPaddingWidthRatio(paddingWidthRatio);
			return this;
		}

		public Builder setPaddingHeightRatio(float paddingHeightRatio) {
			configing.setPaddingHeightRatio(paddingHeightRatio);
			return this;
		}

		public Builder setBorderThickness(int borderThickness) {
			configing.setBorderThickness(borderThickness);
			return this;
		}

		public Builder setBorderTouchExtension(int borderTouchExtension) {
			configing.setBorderTouchExtension(borderTouchExtension);
			return this;
		}

		public Builder setCornerThickness(int cornerThickness) {
			configing.setCornerThickness(cornerThickness);
			return this;
		}

		public Builder setCornerTouchExtension(int cornerTouchExtension) {
			configing.setCornerTouchExtension(cornerTouchExtension);
			return this;
		}

		public Builder setCornerSize(int cornerSize) {
			configing.setCornerSize(cornerSize);
			return this;
		}

		public Builder setImageMaxScale(float imageMaxScale) {
			configing.setImageMaxScale(imageMaxScale);
			return this;
		}

		public Builder setCropWidthMin(int cropWidthMin) {
			configing.setCropWidthMin(cropWidthMin);
			return this;
		}

		public Builder setCropHeightMin(int cropHeightMin) {
			configing.setCropHeightMin(cropHeightMin);
			return this;
		}

		public Builder setScaleChangeMax(float scaleChangeMax) {
			configing.setScaleChangeMax(scaleChangeMax);
			return this;
		}

		public Builder setScaleChangeMin(float scaleChangeMin) {
			configing.setScaleChangeMin(scaleChangeMin);
			return this;
		}

		public Builder setBorderColor(int borderColor) {
			configing.setBorderColor(borderColor);
			return this;
		}

		public Builder setBorderColorHighlight(int borderColorHighlight) {
			configing.setBorderColorHighlight(borderColorHighlight);
			return this;
		}

		public Builder setCornerColor(int cornerColor) {
			configing.setCornerColor(cornerColor);
			return this;
		}

		public Builder setCornerColorHighlight(int cornerColorHighlight) {
			configing.setCornerColorHighlight(cornerColorHighlight);
			return this;
		}

		public Builder setOverlayColor(int overlayColor) {
			configing.setOverlayColor(overlayColor);
			return this;
		}

		public Builder setGridColor(int gridColor) {
			configing.setGridColor(gridColor);
			return this;
		}

		public Builder setGridThickness(int gridThickness) {
			configing.setGridThickness(gridThickness);
			return this;
		}

		public Builder setGridRowsAndCols(int rows, int cols) {
			configing.setGridRows(rows);
			configing.setGridCols(cols);
			return this;
		}

		public Builder setAspectRaio(float aspectRaio) {
			configing.setAspectRatio(aspectRaio);
			return this;
		}

		public Builder setKeepAspectRatio(boolean keep) {
			configing.setKeepAspectRatio(keep);
			return this;
		}

		public Configuration build(){

			context = null;

			return configing;
		}
	}

}
