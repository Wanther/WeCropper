package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import cn.wanther.cropper.core.CropComponent;
import cn.wanther.cropper.core.Engine;

public class CropImage extends CropComponent {
	
	private Drawable mDrawable;
	private Matrix mMatrix = new Matrix();
	
	private Rect mImageRect = new Rect();
	private RectF mTmpRect = new RectF();
	
	private int mRotateDegree = 0;
	private float mMaxScale;

	public CropImage(Engine engine) {
		super(engine);
		init();
	}
	
	protected void init(){
		setMaxScale(getCropEngine().getConfig().getImageMaxScale());
	}
	
	@Override
	public void draw(Canvas canvas){
		Drawable d = getDrawable();
		
		if(d != null){
			canvas.save();
			
			canvas.concat(mMatrix);
			d.draw(canvas);
			
			canvas.restore();
		}
	}
	
	public Drawable getDrawable(){
		return mDrawable;
	}
	
	public Matrix getMatrix(){
		return mMatrix;
	}
	
	public void setDrawable(Drawable d){
		/*
		Drawable old = getDrawable();
		if(old != null){
			if(old instanceof BitmapDrawable){
				Bitmap bitmap = ((BitmapDrawable)old).getBitmap();
				if(bitmap != null && !bitmap.isRecycled()){
					bitmap.recycle();
				}
			}
		}
		*/
		mDrawable = d;
		
		if(mDrawable != null){
			mDrawable.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			
			if(mDrawable instanceof BitmapDrawable){
				BitmapDrawable bitmapDrawable = (BitmapDrawable)mDrawable;
				if(Gravity.NO_GRAVITY != bitmapDrawable.getGravity()){
					bitmapDrawable.setGravity(Gravity.NO_GRAVITY);
				}
			}
		}
		
		//reset();
		
	}
	
	public void reset(){
		setRotateDegree(0);
		mMatrix.reset();
		scale(getMinScale() / getScale(), false);
	}
	
	public void translate(float dx, float dy, boolean invalidate){
		if(dx != 0 || dy != 0){
			mMatrix.postTranslate(dx, dy);
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}
	
	public void scale(float ds, float cx, float cy, boolean invalidate){
		if(ds > 0){
			mMatrix.postScale(ds, ds, cx, cy);
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}
	
	public void scale(float ds, boolean invalidate){
		scale(ds, 0, 0, invalidate);
	}
	
	public void rotate(int degree, float cx, float cy, boolean invalidate){
		degree %= 360;
		if(degree != 0){
			mMatrix.postRotate(degree, cx, cy);
		}
		if(invalidate){
			getEngine().refreshView();
		}
	}
	
	public void rotate(int degree, boolean invalidate){
		rotate(degree, 0, 0, invalidate);
	}
	
	public Rect getImageRect(){
		mImageRect.setEmpty();
		
		Drawable d = getDrawable();
		
		if(d != null){
			int dWidth = d.getIntrinsicWidth();
			int dHeight = d.getIntrinsicHeight();
			if(dWidth > 0 && dHeight > 0){
				mTmpRect.set(0, 0, dWidth, dHeight);
				mMatrix.mapRect(mTmpRect);
				mImageRect.set(Math.round(mTmpRect.left), Math.round(mTmpRect.top), Math.round(mTmpRect.right), Math.round(mTmpRect.bottom));
			}
		}
		
		return mImageRect;
	}
	
	public void setRotateDegree(int degree){
		mRotateDegree = degree % 360;
	}
	
	public int getRotateDegree(){
		return mRotateDegree;
	}
	
	public float getScale(){
		return (float)getImageRect().width() / getImageNowWidthOrigSize();
	}
	
	public void setMaxScale(float scale){
		mMaxScale = scale;
	}
	
	public float getMaxScale(){
		return mMaxScale;
	}
	
	public float getMinScale(){
		Rect rd = getCropEngine().getDisplayRect();
		
		if(rd.isEmpty()){
			return 0;
		}
		
		int width = getImageNowWidthOrigSize();
		int height = getImageNowHeightOrigSize();
		if(width <= 0 || height <= 0){
			return 0;
		}
		
		return Math.min((float)rd.width() / width, (float)rd.height() / height);
	}
	
	public int getImageNowWidthOrigSize(){
		Drawable d = getDrawable();
		if(d == null){
			return 0;
		}
		
		switch(mRotateDegree){
		case 0:
		case 180:
		case -180:
			return d.getIntrinsicWidth();
		case 90:
		case -90:
		case 270:
		case -270:
			return d.getIntrinsicHeight();
		}
		return 0;
	}
	
	public int getImageNowHeightOrigSize(){
		Drawable d = getDrawable();
		if(d == null){
			return 0;
		}
		
		switch(mRotateDegree){
		case 0:
		case 180:
		case -180:
			return d.getIntrinsicHeight();
		case 90:
		case -90:
		case 270:
		case -270:
			return d.getIntrinsicWidth();
		}
		return 0;
	}
	
}
