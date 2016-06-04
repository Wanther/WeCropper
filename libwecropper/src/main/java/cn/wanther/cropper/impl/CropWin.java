package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import cn.wanther.cropper.core.Configuration;
import cn.wanther.cropper.impl.Border;
import cn.wanther.cropper.impl.BorderBottom;
import cn.wanther.cropper.impl.BorderLeft;
import cn.wanther.cropper.impl.BorderRight;
import cn.wanther.cropper.impl.BorderTop;
import cn.wanther.cropper.impl.Corner;
import cn.wanther.cropper.impl.CornerLeftBottom;
import cn.wanther.cropper.impl.CornerLeftTop;
import cn.wanther.cropper.impl.CornerRightBottom;
import cn.wanther.cropper.impl.CornerRightTop;
import cn.wanther.cropper.impl.CropImage;

import cn.wanther.cropper.core.CropComponent;
import cn.wanther.cropper.core.CropEngine;
import cn.wanther.cropper.core.Engine;
import cn.wanther.cropper.impl.aspectratio.ARCropWin;

public class CropWin extends CropComponent {

	public static CropWin create(CropEngine engine) {
		if (engine.getConfig().isKeepAspectRatio()) {
			return new ARCropWin(engine);
		}
		return new CropWin(engine);
	}

	private Paint mPaint;
	private int mMinWidth;
	private int mMinHeight;
	private float mPaddingWidthRatio;
	private float mPaddingHeightRatio;

	// grid line properties
	private boolean mIsShowGrid;
	private int mGridRows;
	private int mGridCols;
	private int mGridLineThickness;
	private int mGridColor;
	private Paint mGridPaint;

	private Rect mRect;
	private RectF mTmpRectF = new RectF();

	private Border mLeft;
	private Border mTop;
	private Border mRight;
	private Border mBottom;

	private Corner mLeftTop;
	private Corner mRightTop;
	private Corner mRightBottom;
	private Corner mLeftBottom;

	public CropWin(Engine engine) {
		super(engine);
		init();
	}

	protected void init() {
		Configuration config = getCropEngine().getConfig();

		setMinWidth(config.getCropWidthMin());
		setMinHeight(config.getCropHeightMin());
		setPaddingWidthRatio(config.getPaddingWidthRatio());
		setPaddingHeightRatio(config.getPaddingHeightRatio());
		setShowGrid(config.isShowGrid());
		setGridRows(config.getGridRows());
		setGridCols(config.getGridCols());
		setGridColor(config.getGridColor());
		setGridLineThickness(config.getGridThickness());

		setupBorderAndCorner();
	}

	protected void setupBorderAndCorner() {
		mLeft = onCreateBorderLeft();
		mRight = onCreateBorderRight();
		mTop = onCreateBorderTop();
		mBottom = onCreateBorderBottom();

		mLeftTop = onCreateCornerLeftTop();
		mRightTop = onCreateCornerRightTop();
		mRightBottom = onCreateCornerRightBottom();
		mLeftBottom = onCreateCornerLeftBottom();

		mLeftTop.link(mTop).link(mRightTop).link(mRight).link(mRightBottom).link(mBottom).link(mLeftBottom).link(mLeft).link(mLeftTop);

		add(mLeft);
		add(mRight);
		add(mTop);
		add(mBottom);

		add(mLeftTop);
		add(mRightTop);
		add(mRightBottom);
		add(mLeftBottom);
	}

	protected Border onCreateBorderLeft(){
		return new BorderLeft(getEngine());
	}

	protected Border onCreateBorderRight(){
		return new BorderRight(getEngine());
	}

	protected Border onCreateBorderTop(){
		return new BorderTop(getEngine());
	}

	protected Border onCreateBorderBottom(){
		return new BorderBottom(getEngine());
	}

	protected Corner onCreateCornerLeftTop(){
		return new CornerLeftTop(getEngine());
	}

	protected Corner onCreateCornerLeftBottom(){
		return new CornerLeftBottom(getEngine());
	}

	protected Corner onCreateCornerRightTop(){
		return new CornerRightTop(getEngine());
	}

	protected Corner onCreateCornerRightBottom(){
		return new CornerRightBottom(getEngine());
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(getCropRect(), getPaint());

		drawGridLine(canvas);

		super.draw(canvas);
	}

	@Override
	public void onActionMove(float dx, float dy) {
		Rect rc = getCropRect();
		Rect ri = getCropEngine().getImage().getImageRect();
		if (rc.left + dx < ri.left) {
			dx = ri.left - rc.left;
		}
		if (rc.right + dx > ri.right) {
			dx = ri.right - rc.right;
		}
		if (rc.top + dy < ri.top) {
			dy = ri.top - rc.top;
		}
		if (rc.bottom + dy > ri.bottom) {
			dy = ri.bottom - rc.bottom;
		}

		translate(Math.round(dx), Math.round(dy), true);
	}

	@Override
	public void onActionUp() {
		super.onActionUp();
		getCropEngine().adjustTranslate();
	}

	@Override
	public boolean isTouched(float x, float y) {
		Rect rc = getCropRect();
		if (x >= rc.left + getLeft().getTouchExtension() 
				&& x <= rc.right - getRight().getTouchExtension() 
				&& y >= rc.top + getTop().getTouchExtension() 
				&& y <= rc.bottom - getBottom().getTouchExtension()) {
			return true;
		}
		return super.isTouched(x, y);
	}

	protected void drawGridLine(Canvas canvas){
		// draw grid
		if (!mIsShowGrid){
			return;
		}

		final Rect r = getCropRect();
		final int w = r.width();
		final int h = r.height();

		if (mGridCols > 1) {
			int gridWidth = w / mGridCols;
			if (gridWidth > 0) {
				for (int i = 0; i < mGridCols - 1; i++) {
					int l = r.left + gridWidth * (i + 1);
					int t = r.top;
					canvas.drawLine(l, t, l, t + h, getGridPaint());
				}
			}
		}

		if (mGridRows > 1) {
			int gridHeight = h / mGridRows;
			if (gridHeight > 0) {
				for (int i = 0; i < mGridRows - 1; i++) {
					int l = r.left;
					int t = r.top + gridHeight * (i + 1);
					canvas.drawLine(l, t, l + w, t,  getGridPaint());
				}
			}
		}
	}

	public void translate(int dx, int dy, boolean invalidate) {
		if (dx != 0 || dy != 0) {
			getCropRect().offset(dx, dy);
		}
		if (invalidate) {
			getEngine().refreshView();
		}
	}

	public void apply(Matrix dm) {
		Rect rc = getCropRect();
		mTmpRectF.set(rc);
		dm.mapRect(mTmpRectF);
		rc.set(Math.round(mTmpRectF.left), Math.round(mTmpRectF.top), Math.round(mTmpRectF.right), Math.round(mTmpRectF.bottom));
	}

	public Rect getCropRect() {
		if (mRect == null) {
			mRect = new Rect();
		}
		return mRect;
	}

	/**
	 * current crop area is rotated and/or scaled, calculate the real rect on
	 * original image (rotate 0, scale 1.0f, left 0, top 0)
	 * 
	 * @return
	 */
	public Rect getCropRectOnOrigImage() {
		Rect realCropRect = new Rect();

		CropImage image = getCropEngine().getImage();
		if (image == null) {
			return realCropRect;
		}

		Rect rc = getCropRect();
		Rect ri = image.getImageRect();

		switch (image.getRotateDegree()) {
		case 0:
			realCropRect.set(rc.left - ri.left, rc.top - ri.top, rc.right - ri.left, rc.bottom - ri.top);
			break;
		case 90:
		case -270:
			realCropRect.set(rc.top - ri.top, ri.right - rc.right, rc.bottom - ri.top, ri.right - rc.left);
			break;
		case 180:
		case -180:
			realCropRect.set(ri.right - rc.right, ri.bottom - rc.bottom, ri.right - rc.left, ri.bottom - rc.top);
			break;
		case -90:
		case 270:
			realCropRect.set(ri.bottom - rc.bottom, rc.left - ri.left, ri.bottom - rc.top, rc.right - ri.left);
			break;
		default:
		}

		float scale = image.getScale();
		realCropRect.set(Math.round(realCropRect.left / scale), Math.round(realCropRect.top / scale),
				Math.round(realCropRect.right / scale), Math.round(realCropRect.bottom / scale));

		return realCropRect;
	}

	public void highlight() {
		setBorderAndCornerStatus(STATUS_HIGHLIGHT);
		getEngine().refreshView();
	}

	public void unhighlight() {
		setBorderAndCornerStatus(STATUS_NORMAL);
		getEngine().refreshView();
	}

	protected void setBorderAndCornerStatus(int status) {
		mLeft.setStatus(status);
		mTop.setStatus(status);
		mRight.setStatus(status);
		mBottom.setStatus(status);

		mLeftTop.setStatus(status);
		mRightTop.setStatus(status);
		mRightBottom.setStatus(status);
		mLeftBottom.setStatus(status);
	}

	protected Paint getPaint() {
		if (mPaint == null) {
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		}
		return mPaint;
	}

	protected Paint getGridPaint() {
		if (mGridPaint == null) {
			mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mGridPaint.setColor(mGridColor);
			mGridPaint.setStyle(Paint.Style.STROKE);
			mGridPaint.setStrokeWidth(mGridLineThickness);
		}
		return mGridPaint;
	}

	public void reset() {
		Rect ri = getCropEngine().getImage().getImageRect();
		Rect rc = getCropRect();

		rc.set(ri);
		if (!rc.isEmpty()) {
			rc.inset(Math.round(ri.width() * getPaddingWidthRatio()), Math.round(ri.height() * getPaddingHeightRatio()));
			float aspectRatio = getCropEngine().getConfig().getAspectRatio();
			if(aspectRatio != 0) {
				int rcWidth = rc.width();
				int rcHeight = rc.height();
				int needRcWidth = Math.round(rc.height() * aspectRatio);
				rc.inset(Math.round((rcWidth - needRcWidth) / 2f), 0);

				float scale = 1f * rcWidth / needRcWidth;

				if (scale < 1) {
					rc.inset(Math.round((rc.width() - rcWidth) / 2), Math.round((rcHeight - rc.height() * scale) / 2));
				}
			}
		}
	}

	public void setMinWidth(int minWidth) {
		mMinWidth = minWidth;
	}

	public int getMinWidth() {
		return mMinWidth;
	}

	public void setMinHeight(int minHeight) {
		mMinHeight = minHeight;
	}

	public int getMinHeight() {
		return mMinHeight;
	}

	public Border getLeft() {
		return mLeft;
	}

	public void setLeft(Border mLeft) {
		this.mLeft = mLeft;
	}

	public Border getTop() {
		return mTop;
	}

	public void setTop(Border mTop) {
		this.mTop = mTop;
	}

	public Border getRight() {
		return mRight;
	}

	public void setRight(Border mRight) {
		this.mRight = mRight;
	}

	public Border getBottom() {
		return mBottom;
	}

	public void setBottom(Border mBottom) {
		this.mBottom = mBottom;
	}

	public Corner getLeftTop() {
		return mLeftTop;
	}

	public void setLeftTop(Corner mLeftTop) {
		this.mLeftTop = mLeftTop;
	}

	public Corner getRightTop() {
		return mRightTop;
	}

	public void setRightTop(Corner mRightTop) {
		this.mRightTop = mRightTop;
	}

	public Corner getRightBottom() {
		return mRightBottom;
	}

	public void setRightBottom(Corner mRightBottom) {
		this.mRightBottom = mRightBottom;
	}

	public Corner getLeftBottom() {
		return mLeftBottom;
	}

	public void setLeftBottom(Corner mLeftBottom) {
		this.mLeftBottom = mLeftBottom;
	}

	public float getPaddingWidthRatio() {
		return mPaddingWidthRatio;
	}

	public void setPaddingWidthRatio(float paddingWidthRatio) {
		this.mPaddingWidthRatio = paddingWidthRatio;
	}

	public float getPaddingHeightRatio() {
		return mPaddingHeightRatio;
	}

	public void setPaddingHeightRatio(float paddingHeightRation) {
		this.mPaddingHeightRatio = paddingHeightRation;
	}

	public void setShowGrid(boolean showGrid) {
		mIsShowGrid = showGrid;
	}

	public void setGridLineThickness(int thickness){
		mGridLineThickness = thickness;
	}

	public void setGridColor(int color) {
		mGridColor = color;
	}

	public void setGridRows(int rows) {
		mGridRows = rows;
	}

	public void setGridCols(int cols) {
		mGridCols = cols;
	}

}
