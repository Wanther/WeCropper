package cn.wanther.cropper.impl;

import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;

public class BorderLeft extends Border {

	public BorderLeft(Engine engine) {
		super(engine);
	}
	
	@Override
	public int getCoordinate() {
		return getCropWin().getCropRect().left;
	}
	
	@Override
	public boolean isTouched(float x, float y){
		return x >= getCoordinate() - getTouchExtension()
				&& x <= getCoordinate() + getTouchExtension()
				&& y >= getNext().getY() + getNext().getTouchExtension()
				&& y <= getPrev().getY() - getNext().getTouchExtension();
	}

	@Override
	public void onActionMove(float dx, float dy) {
		Rect rd = getCropEngine().getDisplayRect();
		Rect ri = getCropEngine().getImage().getImageRect();
		
		int fx = Math.round(getCoordinate() + dx);
		int minLeft = Math.max(rd.left, ri.left);
		int maxLeft = getCropWin().getRight().getCoordinate() - getNext().getSize() - getCropWin().getMinWidth() - getNext().getNext().getNext().getSize();
		
		if(fx < minLeft){
			dx = minLeft - getCoordinate();
		}
		
		if(fx > maxLeft){
			dx = maxLeft - getCoordinate();
		}
		
		translate(Math.round(dx), true);
	}

	public void translate(int dx, boolean invalidate){
		if(dx != 0){
			getCropWin().getCropRect().left += dx;
		}
		
		if(invalidate){
			getEngine().refreshView();
		}
	}

}
