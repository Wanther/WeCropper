package cn.wanther.cropper.impl;

import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;

public class BorderTop extends Border {

	public BorderTop(Engine engine) {
		super(engine);
	}
	
	@Override
	public int getCoordinate() {
		return getCropWin().getCropRect().top;
	}
	
	@Override
	public boolean isTouched(float x, float y){
		return y >= getCoordinate() - getTouchExtension()
				&& y <= getCoordinate() + getTouchExtension()
				&& x >= getPrev().getX() + getPrev().getTouchExtension()
				&& x <= getNext().getX() - getNext().getTouchExtension();
	}

	@Override
	public void onActionMove(float dx, float dy) {
		Rect rd = getCropEngine().getDisplayRect();
		Rect ri = getCropEngine().getImage().getImageRect();
		
		int fy = Math.round(getCoordinate() + dy);
		int minTop = Math.max(rd.top, ri.top);
		int maxTop = getCropWin().getBottom().getCoordinate() - getNext().getSize() - getCropWin().getMinHeight() - getNext().getNext().getNext().getSize();
		
		if(fy < minTop){
			dy = minTop - getCoordinate();
		}
		
		if(fy > maxTop){
			dy = maxTop - getCoordinate();
		}
		
		translate(Math.round(dy), true);
	}

	@Override
	public void translate(int d, boolean invalidate) {
		if(d != 0){
			getCropWin().getCropRect().top += d;
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}

	

}
