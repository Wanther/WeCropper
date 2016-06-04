package cn.wanther.cropper.impl;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import cn.wanther.cropper.core.Configuration;
import cn.wanther.cropper.core.CropComponent;
import cn.wanther.cropper.core.Engine;

public abstract class Corner extends CropComponent {

	private Point mPosition = new Point();
	private Border mPrev;
	private Border mNext;
	private int mSize;
	private int mTouchExtension;
	private int mThickness;
	private int mColor;
	private int mColorHighlight;
	private Paint mPaint;
	
	public Corner(Engine engine){
		super(engine);
		init();
	}
	
	protected void init(){
		Configuration config = getCropEngine().getConfig();
		
		setThickness(config.getCornerThickness());
		setSize(config.getCornerSize());
		setTouchExtension(config.getCornerTouchExtension());
		setColor(config.getCornerColor());
		setColorHighlight(config.getCornerColorHighlight());
	}
	
	@Override
	public boolean isTouched(float x, float y) {
		return x <= getX() + getTouchExtension()
				&& x >= getX() - getTouchExtension()
				&& y <= getY() + getTouchExtension()
				&& y >= getY() - getTouchExtension();
	}
	
	@Override
	public void onActionMove(float dx, float dy) {
		Rect ri = getCropEngine().getImage().getImageRect();
		Rect rd = getEngine().getDisplayRect();
		
		int fx = Math.round(getX() + dx);
		int fy = Math.round(getY() + dy);
		
		int minX = getMinX(rd, ri);
		int maxX = getMaxX(rd, ri); 
		int minY = getMinY(rd, ri);
		int maxY = getMaxY(rd, ri);
		
		if(fx < minX){
			dx = minX - getX();
		}
		if(fx > maxX){
			dx = maxX - getX();
		}
		if(fy < minY){
			dy = minY - getY();
		}
		if(fy > maxY){
			dy = maxY - getY();
		}
		
		translate(Math.round(dx), Math.round(dy), true);
	}

	@Override
	public void onActionUp() {
		super.onActionUp();
		getCropEngine().adjustScale();
	}

	public Point getPosition(){
		return mPosition;
	}
	
	public void setPrev(Border prev){
		mPrev = prev;
	}
	
	public Border getPrev(){
		return mPrev;
	}
	
	public void setNext(Border next){
		mNext = next;
	}
	
	public Border getNext(){
		return mNext;
	}
	
	public void setTouchExtension(int touchExtension){
		mTouchExtension = touchExtension;
	}
	
	public int getTouchExtension(){
		return mTouchExtension;
	}
	
	public void setSize(int size){
		mSize = size;
	}
	
	public int getSize(){
		return mSize;
	}
	
	public void setThickness(int thickness){
		mThickness = thickness;
	}
	
	public int getThickness(){
		return mThickness;
	}
	
	public void setColor(int color){
		mColor = color;
	}
	
	public int getColor(){
		return mColor;
	}
	
	public void setColorHighlight(int color){
		mColorHighlight = color;
	}
	
	public Paint getPaint(){
		if(mPaint == null){
			mPaint = new Paint();
			mPaint.setStrokeWidth(getThickness());
		}
		
		if(hasStatus(STATUS_NORMAL)){
			mPaint.setColor(mColor);
		}
		if(hasStatus(STATUS_HIGHLIGHT)){
			mPaint.setColor(mColorHighlight);
		}
		
		return mPaint;
	}
	
	public Border link(Border next){
		setNext(next);
		next.setPrev(this);
		return next;
	}
	
	public abstract void translate(int dx, int dy, boolean invalidate);
	
	public abstract int getX();
	public abstract int getY();
	public abstract int getMinX(Rect rd, Rect ri);
	public abstract int getMaxX(Rect rd, Rect ri);
	public abstract int getMinY(Rect rd, Rect ri);
	public abstract int getMaxY(Rect rd, Rect ri);
	
}
