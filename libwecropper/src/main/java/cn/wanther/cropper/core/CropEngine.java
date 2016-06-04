package cn.wanther.cropper.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import cn.wanther.cropper.impl.Background;
import cn.wanther.cropper.impl.CropImage;
import cn.wanther.cropper.impl.CropOverlay;
import cn.wanther.cropper.impl.CropWin;


public class CropEngine extends Engine {
	private CropImage mImage;
	private CropWin mCropWin;

	private Configuration mConfig;

	private PointF mLastTouchPoint = new PointF();
	protected AdjustResult mTmpResult = new AdjustResult();

	public CropEngine(){}
	
	public void init(Context context, Configuration config){
		if(config == null){
			config = Configuration.createDefault(context);
		}
		mConfig = config;

		createComponents();
	}
	
	protected void createComponents(){
		mImage = new CropImage(this);
		
		CropOverlay overlay = new CropOverlay(this);
		mCropWin = CropWin.create(this);
		
		overlay.add(new Background(this));
		overlay.add(mCropWin);
		
		add(mImage);
		add(overlay);
	}
	
	@Override
	public void onActionDown(float x, float y) {
		mLastTouchPoint.set(x, y);
		super.onActionDown(x, y);
	}

	@Override
	public void onActionUp() {
		super.onActionUp();
		// useless..
		mLastTouchPoint.set(-1, -1);
	}

	@Override
	public void onActionMove(float x, float y) {
		// make x,y to dx,dy
		super.onActionMove(x - mLastTouchPoint.x, y - mLastTouchPoint.y);
		mLastTouchPoint.set(x, y);
	}
	
	public void initFirstDisplay(){
		getImage().reset();
		
		Rect rd = getDisplayRect();
		Rect ri = getImage().getImageRect();
		if(rd.isEmpty() || ri.isEmpty()){
			return;
		}
		
		getCropWin().reset();
		
		mTmpResult.reset();
		calculateTranslate();
		
		Matrix dm = mTmpResult.getMatrix();
		getImage().getMatrix().postConcat(dm);
		getCropWin().apply(dm);
		
		refreshView();
	}
	
	protected void onDisplayRectChanged(int left, int top, int right, int bottom){
		initFirstDisplay();
	}
	
	public void adjustScale(){
		mTmpResult.reset();
		calculateScale();
		
		Matrix dm = mTmpResult.getMatrix(getCropWin().getCropRect().centerX(), getCropWin().getCropRect().centerX());
		getImage().getMatrix().postConcat(dm);
		getCropWin().apply(dm);
		
		mTmpResult.reset();
		calculateTranslate();
		
		// use mTmpResult.ds to create an Animation
		// during animation , invoke calculateTranslate !
		dm = mTmpResult.getMatrix();
		getImage().getMatrix().postConcat(dm);
		getCropWin().apply(dm);
		
		//start animation if needed
		refreshView();
	}
	
	public void adjustTranslate(){
		mTmpResult.reset();
		calculateTranslate();
		
		translate(mTmpResult.dx, mTmpResult.dy, true);
	}
	
	protected void rotate(int degree){
		//rotate
		Rect rc = getCropWin().getCropRect();
		mTmpResult.reset();
		mTmpResult.set(degree);
		Matrix dm = mTmpResult.getMatrix(rc.centerX(), rc.centerY());

		getImage().getMatrix().postConcat(dm);
		//TODO: move this into #CropImage
		getImage().setRotateDegree(getImage().getRotateDegree() + degree);
		getCropWin().apply(dm);
		
		//adjust scale after rotate
		rc = getCropWin().getCropRect();
		mTmpResult.reset();
		calculateScale();
		dm = mTmpResult.getMatrix(rc.centerX(), rc.centerY());
		
		getImage().getMatrix().postConcat(dm);
		getCropWin().apply(dm);
		
		//adjust translate after scale
		rc = getCropWin().getCropRect();
		mTmpResult.reset();
		calculateTranslate();
		dm = mTmpResult.getMatrix();
		
		getImage().getMatrix().postConcat(dm);
		getCropWin().apply(dm);
		
		refreshView();
	}
	
	public void translate(int dx, int dy, boolean invalidate){
		getImage().translate(dx, dy, false);
		getCropWin().translate(dx, dy, invalidate);
	}
	
	public void rotateLeft(){
		rotate(-90);
	}
	
	public void rotateRight(){
		rotate(90);
	}
	
	public void scale(float ds, boolean invalidate){
		scale(ds, 0, 0, invalidate);
	}
	
	public void scale(float ds, int cx, int cy, boolean invalidate){
		mTmpResult.reset();
		if(ds != 1.0){
			mTmpResult.ds = ds;
		}
		Matrix dm = mTmpResult.getMatrix(cx, cy);
		getImage().getMatrix().postConcat(dm);
		getCropWin().apply(dm);
		
		if(invalidate){
			refreshView();
		}
	}
	
	/**
	 * default impl is : draw rect to created bitmap
	 * if underline is a bitmap, can use Bitmap.createBitmap(source, l, t, w, h) or other
	 * @return
	 */
	public Bitmap getCroppedBitmap(){
		
		Drawable d = getImage().getDrawable();
		if(d == null){
			return null;
		}
		Rect rrc = getCropWin().getCropRectOnOrigImage();

		if(!rrc.intersect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight()) || rrc.isEmpty()){
			return null;
		}
		
		//if underline is a bitmap , crop the bitmap
		/*
		if(d instanceof BitmapDrawable){
			mTmpResult.reset();
			mTmpResult.set(getImage().getRotateDegree());
			Matrix dm = mTmpResult.getMatrix(rrc.centerX(), rrc.centerY());
			return Bitmap.createBitmap(((BitmapDrawable)d).getBitmap(), rrc.left, rrc.top, rrc.width(), rrc.height(), dm, false);
		}
		*/
		
		int nowWidth = 0;
		int nowHeight = 0;
		
		switch(getImage().getRotateDegree()){
		case 0:
		case 180:
		case -180:
			nowWidth = rrc.width();
			nowHeight = rrc.height();
			break;
		case 90:
		case -90:
		case 270:
		case -270:
			nowWidth = rrc.height();
			nowHeight = rrc.width();
			break;
		}
		
		// half size than RGBA_8888
		Bitmap cropResultBitmap = Bitmap.createBitmap(nowWidth, nowHeight, Config.RGB_565);
		
		// deal with rotate
		Rect rc = getCropWin().getCropRect();
		Matrix m = new Matrix(getImage().getMatrix());
		m.postTranslate(-rc.left, -rc.top);
		m.postScale((float)nowWidth / rc.width(), (float)nowWidth / rc.width());
		
		Canvas canvas = new Canvas(cropResultBitmap);
		canvas.concat(m);
		d.draw(canvas);
		
		return cropResultBitmap;
	}
	
	/**
	 * rc&ri's scale rule
	 */
	protected void calculateScale(){
		Rect rd = getDisplayRect();
		Rect rc = getCropWin().getCropRect();
		
		float currentScale = getImage().getScale();
		int scaleChangeWidthMax = Math.round(getConfig().getScaleChangeMax() * rd.width());
		int scaleChangeWidthMin = Math.round(getConfig().getScaleChangeMin() * rd.width());
		int scaleChangeHeightMax = Math.round(getConfig().getScaleChangeMax() * rd.height());
		int scaleChangeHeightMin = Math.round(getConfig().getScaleChangeMin() * rd.height());
		
		float ds = 1.0f;
		
		if(rc.width() < scaleChangeWidthMin && rc.height() < scaleChangeHeightMin){
			//should zoom in
			if(rc.width() > rc.height()){
				ds = (scaleChangeWidthMax + scaleChangeWidthMin) / 2f / rc.width();
			}else{
				ds = (scaleChangeHeightMax + scaleChangeHeightMin) / 2f / rc.height();
			}
		}else if(rc.width() > scaleChangeWidthMax){
			// should zoom out, use width
			ds = (scaleChangeWidthMax + scaleChangeWidthMin) / 2f / rc.width();
		}else if(rc.height() > scaleChangeHeightMax){
			//should zoom out , use height
			ds = (scaleChangeHeightMax + scaleChangeHeightMin) / 2f / rc.height();
		}
		
		float minScale = getImage().getMinScale();
		
		// which is better , ds or ts ??
		if(currentScale * ds > getImage().getMaxScale()){
			ds = getImage().getMaxScale() / currentScale;
		}
		if(currentScale * ds < minScale){
			ds = minScale / currentScale;
		}
		//Log.d("AAAAAA", "cs=" + currentScale + ",ds=" + ds + ",minScale=" + minScale + ",maxScale=" + getImage().getMaxScale());
		mTmpResult.ds = ds;
	}
	
	/**
	 * rd,rc.ri's position rule
	 */
	protected void calculateTranslate(){

		Rect rc = getCropWin().getCropRect();
		Rect rd = getDisplayRect();
		Rect ri = getImage().getImageRect();
		
		int virtualPaddingWidth = Math.round(getCropWin().getPaddingWidthRatio() * rd.width());
		int virtualPaddingHeight = Math.round(getCropWin().getPaddingHeightRatio() * rd.height());
		
		int dx = 0, dy = 0;
		
		if(rc.left + dx < rd.left + virtualPaddingWidth){
			dx += rd.left - Math.max(rc.left + dx - virtualPaddingWidth, ri.left);
		}
		if(rc.right + dx > rd.right - virtualPaddingWidth){
			dx += rd.right - Math.min(rc.right + dx + virtualPaddingWidth, ri.right);
		}
		if(rc.top + dy < rd.top + virtualPaddingHeight){
			dy += rd.top - Math.max(rc.top + dy - virtualPaddingHeight, ri.top);
		}
		if(rc.bottom + dy > rd.bottom - virtualPaddingHeight){
			dy += rd.bottom - Math.min(rc.bottom + dy + virtualPaddingHeight, ri.bottom);
		}
		
		if(ri.width() < rd.width()){
			dx += Math.round((rd.width() - ri.width()) / 2f) + rd.left - (ri.left + dx);
		}else{
			if(ri.left + dx > rd.left){
				dx += rd.left - (ri.left + dx);
			}
			if(ri.right + dx < rd.right){
				dx += rd.right - (ri.right + dx);
			}
		}
		if(ri.height() < rd.height()){
			dy += Math.round((rd.height() - ri.height()) / 2f) + rd.top - (ri.top + dy);
		}else{
			if(ri.top + dy > rd.top){
				dy += rd.top - (ri.top + dy);
			}
			if(ri.bottom + dy < rd.bottom){
				dy += rd.bottom - (ri.bottom + dy);
			}
		}
		
		mTmpResult.set(dx, dy);
		
	}
	
	public void setDrawable(Drawable d){
		if(mImage != null){
			mImage.setDrawable(d);
			initFirstDisplay();
		}
	}
	
	public CropImage getImage(){
		return mImage;
	}
	
	public CropWin getCropWin(){
		return mCropWin;
	}
	
	public Configuration getConfig(){
		return mConfig;
	}
	
	public static class AdjustResult{
		public int dx;
		public int dy;
		public float ds;
		// delta raotate degree
		public int dd;
		private Matrix mMatrix = new Matrix();
		
		public AdjustResult(){}
		
		public void reset(){
			dx = dy = dd = 0;
			ds = 1.0f;
		}
		
		public void set(int dx, int dy){
			this.dx = dx;
			this.dy = dy;
		}
		
		public void set(float ds){
			this.ds = ds;
		}
		
		public void set(int dd){
			this.dd = dd;
		}
		
		public Matrix getMatrix(){
			return getMatrix(0, 0);
		}
		
		public Matrix getMatrix(int cx, int cy){
			mMatrix.reset();
			if(dd % 360 != 0){
				mMatrix.postRotate(dd, cx, cy);
			}
			if(ds != 1.0){
				mMatrix.postScale(ds, ds, cx, cy);
			}
			if(dx != 0 || dy != 0){
				mMatrix.postTranslate(dx, dy);
			}
			
			return mMatrix;
		}
		
		public String toString(){
			return "[dx=" + dx + ", dy=" + dy + ", ds=" + ds + "]";
		}
	}
	
}
