package cn.wanther.cropper.impl.aspectratio;

import cn.wanther.cropper.core.Engine;
import cn.wanther.cropper.impl.Border;
import cn.wanther.cropper.impl.Corner;
import cn.wanther.cropper.impl.CropWin;

/**
 * Created by wanghe on 2015/7/19.
 */
public class ARCropWin extends CropWin {

    public ARCropWin(Engine engine) {
        super(engine);
    }

    @Override
    protected Border onCreateBorderLeft() {
        return new ARBorderLeft(getEngine());
    }

    @Override
    protected Border onCreateBorderRight() {
        return new ARBorderRight(getEngine());
    }

    @Override
    protected Border onCreateBorderTop() {
        return new ARBorderTop(getEngine());
    }

    @Override
    protected Border onCreateBorderBottom() {
        return new ARBorderBottom(getEngine());
    }

    @Override
    protected Corner onCreateCornerLeftTop() {
        return new ARCornerLeftTop(getEngine());
    }

    @Override
    protected Corner onCreateCornerLeftBottom() {
        return new ARCornerLeftBottom(getEngine());
    }

    @Override
    protected Corner onCreateCornerRightTop() {
        return new ARCornerRightTop(getEngine());
    }

    @Override
    protected Corner onCreateCornerRightBottom() {
        return new ARCornerRightBottom(getEngine());
    }
}
