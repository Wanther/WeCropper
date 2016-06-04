package cn.wanther.cropper.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;

public class Component {
	private boolean mVisible = true;
	private List<Component> mChildren;
	private Engine mEngine;
	
	public Component(Engine engine){
		mEngine = engine;
	}
	
	public void add(Component c){
		List<Component> children = getChildren();
		if(!children.contains(c)){
			children.add(c);
			c.onAdd();
		}
	}
	
	public void remove(Component c){
		List<Component> children = getChildren();
		Iterator<Component> it = children.iterator();
		while(it.hasNext()){
			Component child = it.next();
			if(child.equals(c)){
				//sequence : before or after
				it.remove();
				child.onRemove();
				break;
			}
		}
	}
	
	public List<Component> getChildren(){
		if(mChildren == null){
			mChildren = new LinkedList<Component>();
		}
		
		return mChildren;
	}
	
	public int getChildenCount(){
		if(mChildren == null){
			return 0;
		}
		
		return mChildren.size();
	}
	
	public void draw(Canvas canvas){
		if(!mVisible){
			return;
		}
		List<Component> children = getChildren();
		for(Component child : children){
			child.draw(canvas);
		}
	}
	
	protected void onAdd(){
		List<Component> children = getChildren();
		for(Component child : children){
			child.onAdd();
		}
	}
	protected void onRemove(){
		List<Component> children = getChildren();
		for(Component child : children){
			child.onRemove();
		}
	}
	
	protected void onDestroy(){
		List<Component> children = getChildren();
		for(Component child : children){
			child.onDestroy();
		}
	}
	
	public void onActionDown(float x, float y){}
	
	public void onActionUp(){}
	
	public void onActionMove(float x, float y){}
	
	public boolean isTouched(float x, float y){
		return false;
	}
	
	public Component getToucedComponent(float x, float y){
		
		List<Component> children = getChildren();
		for(Component child : children){
			if(child.isTouched(x, y)){
				return child;
			}
			
			Component touchedComponent = child.getToucedComponent(x, y);
			if(touchedComponent != null){
				return touchedComponent;
			}
		}
		
		return null;
	}
	
	public Engine getEngine(){
		return mEngine;
	}
	
	protected void setEngine(Engine engine){
		mEngine = engine;
	}
	
	public void setVisible(boolean visible){
		mVisible = visible;
	}
}
