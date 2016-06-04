package cn.wanther.cropper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import cn.wanther.cropper.core.Configuration;
import cn.wanther.cropper.core.CropEngine;
import cn.wanther.cropper.impl.CropEngineWithAnimation;

public class ImageCropView extends View {
	
	private CropEngine mEngine;

	public ImageCropView(Context context) {
		this(context, Configuration.createDefault(context));
	}

	public ImageCropView(Context context, Configuration config){
		super(context);
		initWithConfig(context, config);
	}

	public ImageCropView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithConfig(context, buildConfigFromAttrs(context, attrs));
	}

	public ImageCropView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initWithConfig(context, buildConfigFromAttrs(context, attrs));
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ImageCropView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initWithConfig(context, buildConfigFromAttrs(context, attrs));
	}

	protected Configuration buildConfigFromAttrs(Context context, AttributeSet attrs){
		Configuration.Builder builder = new Configuration.Builder(context);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageCropView);

		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int borderThickness = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_BORDER_THICKNESS_DP, displayMetrics));
		int borderTouchExtension = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_BORDER_TOUCH_EXTENTION_DP, displayMetrics));
		int cornerSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_CORDER_SIZE_DP, displayMetrics));
		int cornerThickness = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_CORNER_THICKNESS_DP, displayMetrics));
		int cornerTouchExtension = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_CORNER_TOUCH_EXTENSION_DP, displayMetrics));
		int cropWidthMin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_CROP_WIDTH_MIN_DP, displayMetrics));
		int cropHeightMin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_CROP_HEIGHT_MIN_DP, displayMetrics));
		int gridThickness = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Configuration.DEFAULT_GRID_THICKNESS_DP, displayMetrics));

		builder.setPaddingWidthRatio(a.getFloat(R.styleable.ImageCropView_paddingWidthRatio, Configuration.DEFAULT_PADDING_WIDTH_RATIO));
		builder.setPaddingHeightRatio(a.getFloat(R.styleable.ImageCropView_paddingHeightRatio, Configuration.DEFAULT_PADDING_HEIGHT_RATIO));
		builder.setBorderThickness(a.getDimensionPixelSize(R.styleable.ImageCropView_borderThickness, borderThickness));
		builder.setBorderTouchExtension(a.getDimensionPixelSize(R.styleable.ImageCropView_borderTouchExtention, borderTouchExtension));
		builder.setCornerThickness(a.getDimensionPixelSize(R.styleable.ImageCropView_cornerThickness, cornerThickness));
		builder.setCornerTouchExtension(a.getDimensionPixelSize(R.styleable.ImageCropView_cornerTouchExtention, cornerTouchExtension));
		builder.setCornerSize(a.getDimensionPixelSize(R.styleable.ImageCropView_cornerSize, cornerSize));
		builder.setImageMaxScale(a.getFloat(R.styleable.ImageCropView_imageMaxScale, Configuration.DEFAULT_IMAGE_MAX_SCALE));
		builder.setCropWidthMin(a.getDimensionPixelSize(R.styleable.ImageCropView_cropWidthMin, cropWidthMin));
		builder.setCropHeightMin(a.getDimensionPixelSize(R.styleable.ImageCropView_cropHeightMin, cropHeightMin));
		builder.setScaleChangeMax(a.getFloat(R.styleable.ImageCropView_scaleChangeMax, Configuration.DEFAULT_SCALE_CHANGE_MAX));
		builder.setScaleChangeMin(a.getFloat(R.styleable.ImageCropView_scaleChangeMin, Configuration.DEFAULT_SCALE_CHANGE_MIN));
		builder.setBorderColor(a.getColor(R.styleable.ImageCropView_borderColor, Configuration.DEFAULT_BORDER_COLOR));
		builder.setBorderColorHighlight(a.getColor(R.styleable.ImageCropView_borderColorHighLight, Configuration.DEFAULT_BORDER_COLOR_HIGHLIGHT));
		builder.setCornerColor(a.getColor(R.styleable.ImageCropView_cornerColor, Configuration.DEFAULT_CORNER_COLOR));
		builder.setCornerColorHighlight(a.getColor(R.styleable.ImageCropView_cornerColorHighLight, Configuration.DEFAULT_CORNER_COLOR_HIGHLIGHT));
		builder.setOverlayColor(a.getColor(R.styleable.ImageCropView_overlayColor, Configuration.DEFAULT_OVERLAY_COLOR));
		builder.setAspectRaio(a.getFloat(R.styleable.ImageCropView_aspectRatio, 0));
		builder.setKeepAspectRatio(a.getBoolean(R.styleable.ImageCropView_keepAspectRatio, false));
		builder.setGridThickness(a.getDimensionPixelSize(R.styleable.ImageCropView_gridThickness, gridThickness));
		builder.setGridColor(a.getColor(R.styleable.ImageCropView_gridLineColor, Configuration.DEFAULT_GRID_COLOR));
		String gridLineRC = a.getString(R.styleable.ImageCropView_gridLineRowCol);
		if(!TextUtils.isEmpty(gridLineRC)) {
			String[] rowAndCol = gridLineRC.split("[,]");
			builder.setGridRowsAndCols(Integer.parseInt(rowAndCol[0]), Integer.parseInt(rowAndCol[1]));
		}

		a.recycle();

		return builder.build();
	}

	private void initWithConfig(Context context, Configuration config) {
		//mEngine = new CropEngine();
		mEngine = new CropEngineWithAnimation();
		mEngine.setView(this);
		mEngine.init(context, config);
	}

	public void destroy(){
		if(mEngine != null){
			mEngine.destroy();
			mEngine = null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(mEngine != null){
			mEngine.draw(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mEngine == null){
			return false;
		}
		
		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			mEngine.onActionDown(event.getX(0), event.getY(0));
			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_POINTER_UP:
			mEngine.onActionUp();
			return true;
		case MotionEvent.ACTION_MOVE:
			mEngine.onActionMove(event.getX(0), event.getY(0));
			return true;
			default:
				return false;
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if(mEngine != null){
			mEngine.onViewRectChanged(0, 0, w, h);
		}
	}
	
	
	public void setBitmap(Bitmap bitmap){
		setDrawable(new BitmapDrawable(getResources(), bitmap));
	}
	
	public void setDrawable(Drawable d){
		if(mEngine != null){
			mEngine.setDrawable(d);
		}
		
	}
	
	public void rotateLeft(){
		if(mEngine != null){
			mEngine.rotateLeft();
		}
	}
	
	public void rotateRight(){
		if(mEngine != null){
			mEngine.rotateRight();
		}
	}
	
	public Bitmap getCroppedBitmap(){
		if(mEngine != null){
			return mEngine.getCroppedBitmap();
		}
		return null;
	}
	
	public boolean isEngineEnabled(){
		return mEngine != null && mEngine.isEnabled();
	}
	
	public Rect getCropWinRect(){
		if(mEngine == null){
			return null;
		}
		return mEngine.getCropWin().getCropRect();
	}

}
