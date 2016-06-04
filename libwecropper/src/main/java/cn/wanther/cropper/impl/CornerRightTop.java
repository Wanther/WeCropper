package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;

public class CornerRightTop extends Corner {

	public CornerRightTop(Engine engine) {
		super(engine);
	}

	@Override
	public int getX() {
		return getCropWin().getCropRect().right;
	}

	@Override
	public int getY() {
		return getCropWin().getCropRect().top;
	}

	@Override
	public int getMinX(Rect rd, Rect ri) {
		return getCropWin().getLeftTop().getX() + getCropWin().getLeftTop().getSize() + getCropWin().getMinWidth() + getSize();
	}

	@Override
	public int getMaxX(Rect rd, Rect ri) {
		return Math.min(rd.right, ri.right);
	}

	@Override
	public int getMinY(Rect rd, Rect ri) {
		return Math.max(rd.top, ri.top);
	}

	@Override
	public int getMaxY(Rect rd, Rect ri) {
		return getCropWin().getRightBottom().getY() - getSize() - getCropWin().getMinHeight() - getCropWin().getRightBottom().getSize();
	}
	
	@Override
	public void translate(int dx, int dy, boolean invalidate) {
		Rect rc = getCropWin().getCropRect();
		if(dx != 0){
			rc.right += dx;
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
		canvas.drawLine(getX() + Math.round(getThickness() / 2f), getY(), getX() - getSize(), getY(), getPaint());
	}
	
}
