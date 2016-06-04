package cn.wanther.cropper.impl;

import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;

public class BorderRight extends Border {

	public BorderRight(Engine engine) {
		super(engine);
	}
	
	@Override
	public int getCoordinate() {
		return getCropWin().getCropRect().right;
	}
	
	@Override
	public boolean isTouched(float x, float y){
		return x >= getCoordinate() - getTouchExtension()
				&& x <= getCoordinate() + getTouchExtension()
				&& y >= getPrev().getY() + getPrev().getTouchExtension()
				&& y <= getNext().getY() - getNext().getTouchExtension();
	}

	@Override
	public void onActionMove(float dx, float dy) {
		Rect rd = getCropEngine().getDisplayRect();
		Rect ri = getCropEngine().getImage().getImageRect();
		
		int fx = Math.round(getCoordinate() + dx);
		int minX = getCropWin().getLeft().getCoordinate() + getNext().getSize() + getCropWin().getMinWidth() + getNext().getNext().getNext().getSize();
		int maxX = Math.min(rd.right, ri.right);
		
		if(fx < minX){
			dx = minX - getCoordinate();
		}
		
		if(fx > maxX){
			dx = maxX - getCoordinate();
		}
		
		translate(Math.round(dx), true);
	}

	@Override
	public void translate(int d, boolean invalidate) {
		if(d != 0){
			getCropWin().getCropRect().right += d;
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}

	

}
