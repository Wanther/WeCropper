package cn.wanther.cropper.impl.aspectratio;

import cn.wanther.cropper.core.Engine;
import cn.wanther.cropper.impl.BorderBottom;

/**
 * Created by wanghe on 2015/7/19.
 */
public class ARBorderBottom extends BorderBottom {
    public ARBorderBottom(Engine engine) {
        super(engine);
    }

    @Override
    public boolean isTouched(float x, float y) {
        return false;
    }
}
