package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;

public class CornerLeftTop extends Corner {

	public CornerLeftTop(Engine engine) {
		super(engine);
	}

	@Override
	public int getX() {
		return getCropWin().getCropRect().left;
	}

	@Override
	public int getY() {
		return getCropWin().getCropRect().top;
	}

	@Override
	public int getMinX(Rect rd, Rect ri) {
		return Math.max(rd.left, ri.left);
	}

	@Override
	public int getMaxX(Rect rd, Rect ri) {
		return getCropWin().getRightTop().getX() - getSize() - getCropWin().getMinWidth() - getCropWin().getRightTop().getSize();
	}

	@Override
	public int getMinY(Rect rd, Rect ri) {
		return Math.max(rd.top, ri.top);
	}

	@Override
	public int getMaxY(Rect rd, Rect ri) {
		return getCropWin().getLeftBottom().getY() - getSize() - getCropWin().getMinHeight() - getCropWin().getLeftBottom().getSize();
	}
	
	@Override
	public void translate(int dx, int dy, boolean invalidate) {
		Rect rc = getCropWin().getCropRect();
		if(dx != 0){
			rc.left += dx;
		}
		if(dy != 0){
			rc.top += dy;
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(getX(), getY() - Math.round(getThickness() / 2f), getX(), getY() + getSize(), getPaint());
		canvas.drawLine(getX() - Math.round(getThickness() / 2f), getY(), getX() + getSize(), getY(), getPaint());
	}
	
}
