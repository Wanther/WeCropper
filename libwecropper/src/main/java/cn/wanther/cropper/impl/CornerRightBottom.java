package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;

public class CornerRightBottom extends Corner {

	public CornerRightBottom(Engine engine) {
		super(engine);
	}

	@Override
	public int getX() {
		return getCropWin().getCropRect().right;
	}

	@Override
	public int getY() {
		return getCropWin().getCropRect().bottom;
	}

	@Override
	public int getMinX(Rect rd, Rect ri) {
		return getCropWin().getLeftBottom().getX() + getCropWin().getLeftBottom().getSize() + getCropWin().getMinWidth() + getSize();
	}

	@Override
	public int getMaxX(Rect rd, Rect ri) {
		return Math.min(rd.right, ri.right);
	}

	@Override
	public int getMinY(Rect rd, Rect ri) {
		return getCropWin().getRightTop().getY() + getCropWin().getRightTop().getSize() + getCropWin().getMinHeight() + getSize();
	}

	@Override
	public int getMaxY(Rect rd, Rect ri) {
		return Math.min(rd.bottom, ri.bottom);
	}
	
	@Override
	public void translate(int dx, int dy, boolean invalidate) {
		Rect rc = getCropWin().getCropRect();
		if(dx != 0){
			rc.right += dx;
		}
		if(dy != 0){
			rc.bottom += dy;
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(getX(), getY() + Math.round(getThickness() / 2f), getX(), getY() - getSize(), getPaint());
		canvas.drawLine(getX() + Math.round(getThickness() / 2f), getY(), getX() - getSize(), getY(), getPaint());
	}
	
}
