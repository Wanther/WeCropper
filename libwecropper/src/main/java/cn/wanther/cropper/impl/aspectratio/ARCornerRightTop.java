package cn.wanther.cropper.impl.aspectratio;

import android.graphics.Rect;

import cn.wanther.cropper.core.Engine;
import cn.wanther.cropper.impl.CornerRightTop;

/**
 * Created by wanghe on 2015/7/19.
 */
public class ARCornerRightTop extends CornerRightTop {
    public ARCornerRightTop(Engine engine) {
        super(engine);
    }

    @Override
    public boolean isTouched(float x, float y) {
        final int cropWidthHalf = (int)(getCropWin().getCropRect().width() / 2f);
        final int cropHeightHalf = (int)(getCropWin().getCropRect().height() / 2f);

        return x >= getX() - cropWidthHalf && x <= getX() + getTouchExtension()
                && y >= getY() - getTouchExtension() && y <= getY() + getTouchExtension()
                ||
                x >= getX() - getTouchExtension() && x <= getX() + getTouchExtension()
                        && y >= getY() - getTouchExtension() && y <= getY() + cropHeightHalf;
    }

    @Override
    public void onActionMove(float dx, float dy) {
        final Rect ri = getCropEngine().getImage().getImageRect();
        final Rect rd = getEngine().getDisplayRect();

        float dt = dx;
        if (Math.abs(dx) < Math.abs(dy)) {
            dt = -dy;
        }

        final int minDX = getMinX(rd, ri) - getX();
        final int maxDX = getMaxX(rd, ri) - getX();
        final int minDY = getMinY(rd, ri) - getY();
        final int maxDY = getMaxY(rd, ri) - getY();

        if (dt < minDX) {
            dt = minDX;
        }

        if (dt > maxDX) {
            dt = maxDX;
        }

        if (-dt < minDY) {
            dt = -minDY;
        }

        if (-dt > maxDY) {
            dt = -maxDY;
        }

        translate(Math.round(dt), Math.round(-dt), true);
    }
}
