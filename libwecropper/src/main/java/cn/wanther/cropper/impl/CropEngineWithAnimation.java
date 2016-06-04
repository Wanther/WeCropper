package cn.wanther.cropper.impl;


import android.graphics.Matrix;
import android.graphics.Rect;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import cn.wanther.cropper.core.CropEngine;

public class CropEngineWithAnimation extends CropEngine {
	
	private Animator mCurrentAnimator;
	private Animator mTranslateAnimator;
	private TranslateAnimatorListener mTranslateAnimatorListener;
	private Animator mScaleAnimator;
	private ScaleAnimatorListener mScaleAnimatorListener;
	private Animator mRotateAnimator;
	private RotateAnimatorListener mRotateAnimatorListener;
	
	private Matrix mMatrixBeforeAnimation = new Matrix();
	private Rect mRectBeforeAnimation = new Rect();
	private Matrix mDeltaMatrix = new Matrix();

	@Override
	protected void rotate(int degree) {
		if(!isEnabled()){
			return;
		}
		
		degree = degree % 360;
		if(degree == 0){
			return;
		}
		
		//Rect rc = getCropWin().getCropRect();
		Rect rd = getDisplayRect();
		mCurrentAnimator = getRotateAniamtor(degree, rd.centerX(), rd.centerY());
		mCurrentAnimator.start();
	}

	@Override
	public void adjustScale() {
		if(!isEnabled()){
			return;
		}
		
		mTmpResult.reset();
		calculateScale();
		
		// TODO: if change is too small to see, how much ?
		if(mTmpResult.ds > 1.01f || mTmpResult.ds < 0.99f){
			Rect rc = getCropWin().getCropRect();
			mCurrentAnimator = getScaleAnimator(mTmpResult.ds, rc.centerX(), rc.centerY());
			
			mCurrentAnimator.start();
		}else{
			adjustTranslate();
		}
		
	}

	@Override
	public void adjustTranslate() {
		if(!isEnabled()){
			return;
		}
		
		mTmpResult.reset();
		calculateTranslate();

		if(mTmpResult.dx != 0 || mTmpResult.dy != 0){
			mCurrentAnimator = getTranslateAnimator(mTmpResult.dx, mTmpResult.dy);
			mCurrentAnimator.start();
		}
		
	}
	
	protected Animator getTranslateAnimator(int dx, int dy){
		if(mTranslateAnimator == null){
			mTranslateAnimator = ValueAnimator.ofInt(0).setDuration(300);
			
			mTranslateAnimatorListener = new TranslateAnimatorListener();
			
			mTranslateAnimator.addListener(mTranslateAnimatorListener);
			((ValueAnimator)mTranslateAnimator).addUpdateListener(mTranslateAnimatorListener);
		}
		
		mTranslateAnimatorListener.set(dx, dy);
		
		return mTranslateAnimator;
	}
	
	protected Animator getScaleAnimator(float ds, int cx, int cy){
		if(mScaleAnimator == null){
			mScaleAnimator = ValueAnimator.ofInt(0).setDuration(300);
			mScaleAnimatorListener = new ScaleAnimatorListener();
			
			mScaleAnimator.addListener(mScaleAnimatorListener);
			((ValueAnimator)mScaleAnimator).addUpdateListener(mScaleAnimatorListener);
		}
		
		mScaleAnimatorListener.set(ds, cx, cy);
		
		return mScaleAnimator;
	}
	
	protected Animator getRotateAniamtor(int dd, int cx, int cy){
		if(mRotateAnimator == null){
			mRotateAnimator = ValueAnimator.ofInt(0).setDuration(300);
			
			mRotateAnimatorListener = new RotateAnimatorListener();
			
			mRotateAnimator.addListener(mRotateAnimatorListener);
			((ValueAnimator)mRotateAnimator).addUpdateListener(mRotateAnimatorListener);
		}
		
		if(mRotateAnimatorListener == null){
			mRotateAnimatorListener = new RotateAnimatorListener();
		}
		mRotateAnimatorListener.set(dd, cx, cy);
		
		return mRotateAnimator;
	}
	
	protected void rememberStatus(){
		mRectBeforeAnimation.set(getCropWin().getCropRect());
		mMatrixBeforeAnimation.set(getImage().getMatrix());
	}
	
	public abstract class CropAnimatorListener extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener{

		@Override
		public void onAnimationEnd(Animator animation) {
			getCropWin().unhighlight();
			enable();
		}

		@Override
		public void onAnimationStart(Animator animation) {
			disable();
			getCropWin().highlight();
		}
	}
	
	public class TranslateAnimatorListener extends CropAnimatorListener{
		private int mDeltaX;
		private int mDeltaY;
		
		public void set(int dx, int dy){
			mDeltaX = dx;
			mDeltaY = dy;
			rememberStatus();
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			float progress = (float)animation.getCurrentPlayTime() / animation.getDuration();
			int dx = Math.round(progress * mDeltaX);
			int dy = Math.round(progress * mDeltaY);
			
			//TODO: avoid overhead
			
			
			if(progress == 0 || (dx == 0 && dy == 0)){
				return;
			}
			
			mDeltaMatrix.reset();
			mDeltaMatrix.setTranslate(dx, dy);
			
			getImage().getMatrix().set(mMatrixBeforeAnimation);
			getImage().getMatrix().postConcat(mDeltaMatrix);
			getCropWin().getCropRect().set(mRectBeforeAnimation);
			getCropWin().apply(mDeltaMatrix);
			
			refreshView();
		}
		
	}
	
	public class ScaleAnimatorListener extends CropAnimatorListener{
		
		private float mDeltaS;
		private int mCenterX;
		private int mCenterY;
		
		public void set(float ds, int cx, int cy){
			mDeltaS = ds;
			mCenterX = cx;
			mCenterY = cy;
			
			rememberStatus();
		}
		
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			float progress = (float)animation.getCurrentPlayTime() / animation.getDuration();
			float ds = progress * (mDeltaS - 1) + 1;
			
			//avoid overhead
			if(ds < 1 && ds < mDeltaS || ds > 1 && ds > mDeltaS){
				ds = mDeltaS;
			}
			
			if(ds == 1){
				return;
			}
			
			mDeltaMatrix.reset();
			mDeltaMatrix.setScale(ds, ds, mCenterX, mCenterY);
			
			getImage().getMatrix().set(mMatrixBeforeAnimation);
			getImage().getMatrix().postConcat(mDeltaMatrix);
			getCropWin().getCropRect().set(mRectBeforeAnimation);
			getCropWin().apply(mDeltaMatrix);
			
			/*mTmpResult.reset();
			calculateTranslate();
			
			if(mTmpResult.dx != 0 || mTmpResult.dy != 0){
				Matrix dm = mTmpResult.getMatrix();
				getImage().getMatrix().postConcat(dm);
				getCropWin().apply(dm);
			}*/
			
			refreshView();
			
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			adjustTranslate();
		}
		
	}
	
	public class RotateAnimatorListener extends CropAnimatorListener{
		/**
		 * multiple 90 times
		 */
		private int mDeltaD;
		private int mCenterX;
		private int mCenterY;
		
		public void set(int dd, int cx, int cy){
			mDeltaD = dd % 360;
			mCenterX = cx;
			mCenterY = cy;
			
			rememberStatus();
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			float progress = (float)animation.getCurrentPlayTime() / animation.getDuration();
			int dd = Math.round(progress * mDeltaD);
			//avoid overhead
			if(Math.abs(dd) > Math.abs(mDeltaD)){
				dd = mDeltaD;
			}
			
			if(dd == 0){
				return;
			}
			
			mDeltaMatrix.reset();
			mDeltaMatrix.setRotate(dd, mCenterX, mCenterY);
			
			getImage().getMatrix().set(mMatrixBeforeAnimation);
			getImage().getMatrix().postConcat(mDeltaMatrix);
			getCropWin().getCropRect().set(mRectBeforeAnimation);
			getCropWin().apply(mDeltaMatrix);
			
			refreshView();
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			getImage().setRotateDegree(getImage().getRotateDegree() + mDeltaD);
			
			super.onAnimationEnd(animation);
			
			adjustScale();
		}
		
		
	}

}
