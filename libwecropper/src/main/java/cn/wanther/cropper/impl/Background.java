package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import cn.wanther.cropper.core.CropComponent;
import cn.wanther.cropper.core.Engine;

public class Background extends CropComponent {

	private Paint mPaint;
	private int mOverlayColor;

	public Background(Engine engine) {
		super(engine);
		init();
	}
	
	protected void init(){
		setOverlayColor(getCropEngine().getConfig().getOverlayColor());
	}
	
	@Override
	public void draw(Canvas canvas){
		Rect rd = getCropEngine().getDisplayRect();
		Rect ri = getCropEngine().getImage().getImageRect();
		
		canvas.drawRect(Math.max(rd.left, ri.left), Math.max(rd.top, ri.top), Math.min(rd.right, ri.right), Math.min(rd.bottom, ri.bottom), getPaint());
	}
	
	protected Paint getPaint(){
		if(mPaint == null){
			mPaint = new Paint();
			mPaint.setColor(mOverlayColor);
		}
		return mPaint;
	}
	
	public void setOverlayColor(int color){
		mOverlayColor = color;
	}

}
