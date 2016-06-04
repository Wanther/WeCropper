package cn.wanther.cropper.core;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public abstract class Engine {
	private WeakReference<View> mView;
	private Rect mDisplayRect = new Rect();
	
	private boolean mEnabled = true;
	private Component mTouchingComponent;
	private List<Component> mComponentList;
	
	public Engine(){}
	
	public void add(Component c){
		if(c != null){
			getComponentList().add(c);
			c.onAdd();
		}
	}
	
	/**
	 * must invoke on main thread
	 */
	public void refreshView(){
		View v = getView();
		if(v != null){
			v.invalidate();
		}
	}

	public void destroy(){
		disable();
		List<Component> cmpList = getComponentList();
		for(Component c : cmpList){
			c.onDestroy();
		}
		
		onDestroy();
	}
	
	protected void onDestroy(){}
	
	public void draw(Canvas canvas){
		Rect rd = getDisplayRect();
		if(rd.isEmpty()){
			return;
		}
		
		canvas.clipRect(rd);
		
		List<Component> cmpList = getComponentList();
		for(Component c : cmpList){
			c.draw(canvas);
		}
	}
	
	public void onViewRectChanged(int left, int top, int right, int bottom){
		setDisplayRect(left + getViewPaddingLeft(), top + getViewPaddingTop(), right - getViewPaddingRight(), bottom - getViewPaddingBottom());
		Rect rd = getDisplayRect();
		onDisplayRectChanged(rd.left, rd.top, rd.right, rd.bottom);
	}
	
	protected void onDisplayRectChanged(int left, int top, int right, int bottom){
		
	}
	
	public void onActionDown(float x, float y) {
		// TODO: avoid multi touch
		if(isEnabled() && mTouchingComponent == null){
			List<Component> cmpList = getComponentList();
			for(Component c : cmpList){
				mTouchingComponent = c.getToucedComponent(x, y);
				if(mTouchingComponent != null){
					mTouchingComponent.onActionDown(x, y);
					break;
				}
			}
		}
	}
	
	public void onActionUp() {
		if(mTouchingComponent != null){
			mTouchingComponent.onActionUp();
			mTouchingComponent = null;
		}
	}

	public void onActionMove(float deltaX, float deltaY) {
		if(isEnabled() && mTouchingComponent != null){
			mTouchingComponent.onActionMove(deltaX, deltaY);
		}
	}
	
	/**
	 * affect : touchevent, rotate method, crop method
	 * @return
	 */
	public boolean isEnabled(){
		return mEnabled;
	}
	
	public void enable(){
		mEnabled = true;
	}
	
	public void disable(){
		mEnabled = false;
	}

	//gettes and settes
	public View getView(){
		if(mView == null){
			return null;
		}
		return mView.get();
	}
	
	public void setView(View v){
		mView = new WeakReference<View>(v);
	}
	
	public List<Component> getComponentList(){
		if(mComponentList == null){
			mComponentList = new LinkedList<Component>();
		}
		return mComponentList;
	}
	
	public Rect getDisplayRect(){
		return mDisplayRect;
	}
	
	private void setDisplayRect(int left, int top, int right, int bottom){
		View v = getView();
		if(v == null){
			mDisplayRect.setEmpty();
		}else{
			mDisplayRect.set(left, top, right, bottom);
		}
	}
	
	public int getViewPaddingLeft(){
		View v = getView();
		if(v != null){
			return v.getPaddingLeft();
		}
		return 0;
	}
	
	public int getViewPaddingTop(){
		View v = getView();
		if(v != null){
			return v.getPaddingTop();
		}
		return 0;
	}
	
	public int getViewPaddingRight(){
		View v = getView();
		if(v != null){
			return v.getPaddingRight();
		}
		return 0;
	}
	
	public int getViewPaddingBottom(){
		View v = getView();
		if(v != null){
			return v.getPaddingBottom();
		}
		return 0;
	}

}
