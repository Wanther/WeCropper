package cn.wanther.cropper.impl;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.wanther.cropper.core.CropComponent;
import cn.wanther.cropper.core.Engine;

public class CropOverlay extends CropComponent {
	
	private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG  
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG  
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
	
	public CropOverlay(Engine engine) {
		super(engine);
	}
	
	@Override
	public void draw(Canvas canvas){
		Rect rd = getEngine().getDisplayRect();
		canvas.saveLayerAlpha(rd.left, rd.top, rd.right, rd.bottom, 255, LAYER_FLAGS);
		
		super.draw(canvas);
		
		canvas.restore();
	}

}
