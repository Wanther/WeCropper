package cn.wanther.cropper.core;

import cn.wanther.cropper.impl.CropWin;

public class CropComponent extends Component {
	/**
	 * 16 radix for multi status
	 */
	public static final int STATUS_NORMAL = 0x1;
	public static final int STATUS_HIGHLIGHT = 0x2;
	
	private int mStatus = STATUS_NORMAL;

	public CropComponent(Engine engine) {
		super(engine);
	}
	
	public CropEngine getCropEngine(){
		return (CropEngine)getEngine();
	}
	
	public CropWin getCropWin(){
		return getCropEngine().getCropWin();
	}
	
	public void setStatus(int status){
		mStatus = status;
	}
	
	public int getStatus(){
		return mStatus;
	}
	
	public int getStatus(int mask){
		return mStatus & mask;
	}
	
	public boolean hasStatus(int status){
		return (getStatus() & status) == status;
	}

	@Override
	public void onActionDown(float x, float y) {
		super.onActionDown(x, y);
		getCropWin().highlight();
	}

	@Override
	public void onActionUp() {
		super.onActionUp();
		getCropWin().unhighlight();
	}
	
}
