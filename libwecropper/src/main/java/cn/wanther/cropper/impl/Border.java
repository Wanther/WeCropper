package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import cn.wanther.cropper.core.Configuration;
import cn.wanther.cropper.core.CropComponent;
import cn.wanther.cropper.core.Engine;

public abstract class Border extends CropComponent {

	private Corner mPrev;
	private Corner mNext;
	private int mThickness;
	private int mTouchExtension;
	private int mColor;
	private int mColorHighlight;
	private Paint mPaint;
	
	public Border(Engine engine) {
		super(engine);
		init();
	}
	
	protected void init(){
		Configuration config = getCropEngine().getConfig();
		
		setThickness(config.getBorderThickness());
		setTouchExtension(config.getBorderTouchExtension());
		setColor(config.getBorderColor());
		setColorHighlight(config.getBorderColorHighlight());
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(getPrev().getX(), getPrev().getY(), getNext().getX(), getNext().getY(), getPaint());
	}
	
	@Override
	public void onActionUp() {
		super.onActionUp();
		getCropEngine().adjustScale();
	}
	
	public void setPrev(Corner prev){
		mPrev = prev;
	}
	
	public void setNext(Corner next){
		mNext = next;
	}
	
	public Corner getPrev(){
		return mPrev;
	}
	
	public Corner getNext(){
		return mNext;
	}
	
	public void setThickness(int thickness){
		mThickness = thickness;
	}
	
	public void setTouchExtension(int touchExtension){
		mTouchExtension = touchExtension;
	}
	
	public void setColor(int color){
		mColor = color;
	}
	
	public void setColorHighlight(int color){
		mColorHighlight = color;
	}
	
	//TODO: paint management
	public Paint getPaint(){
		if(mPaint == null){
			mPaint = new Paint();
			mPaint.setStrokeWidth(mThickness);
		}
		if(hasStatus(STATUS_NORMAL)){
			mPaint.setColor(mColor);
		}
		if(hasStatus(STATUS_HIGHLIGHT)){
			mPaint.setColor(mColorHighlight);
		}
		return mPaint;
	}
	
	public Corner link(Corner next){
		setNext(next);
		next.setPrev(this);
		
		return next;
	}
	
	public int getTouchExtension(){
		return mTouchExtension;
	}
	
	public abstract int getCoordinate();
	public abstract void translate(int d, boolean invalidate);

}
