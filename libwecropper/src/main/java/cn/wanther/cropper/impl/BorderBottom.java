package cn.wanther.cropper.impl;

import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;

public class BorderBottom extends Border {

	public BorderBottom(Engine engine) {
		super(engine);
	}
	
	@Override
	public int getCoordinate() {
		return getCropWin().getCropRect().bottom;
	}
	
	@Override
	public boolean isTouched(float x, float y){
		return y >= getCoordinate() - getTouchExtension()
				&& y <= getCoordinate() + getTouchExtension()
				&& x >= getNext().getX() + getNext().getTouchExtension()
				&& x <= getPrev().getX() - getPrev().getTouchExtension();
	}

	@Override
	public void onActionMove(float dx, float dy) {
		Rect rd = getCropEngine().getDisplayRect();
		Rect ri = getCropEngine().getImage().getImageRect();
		
		int fy = Math.round(getCoordinate() + dy);
		int minY = getCropWin().getTop().getCoordinate() + getNext().getSize() + getCropWin().getMinHeight() + getNext().getNext().getNext().getSize();
		int maxY = Math.min(rd.bottom, ri.bottom);
		
		
		if(fy < minY){
			dy = minY - getCoordinate();
		}
		
		if(fy > maxY){
			dy = maxY - getCoordinate();
		}
		
		translate(Math.round(dy), true);
	}

	@Override
	public void translate(int d, boolean invalidate) {
		if(d != 0){
			getCropWin().getCropRect().bottom += d;
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}

	

}
