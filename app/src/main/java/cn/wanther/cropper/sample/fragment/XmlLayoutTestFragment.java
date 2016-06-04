package cn.wanther.cropper.sample.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.wanther.cropper.ImageCropView;
import cn.wanther.cropper.sample.R;

/**
 * Created by wanghe on 2015/7/16.
 */
public class XmlLayoutTestFragment extends NoneRatioTestFragment {

    private View mLoadingProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_layout_02, container, false);

        mLoadingProgress = v.findViewById(R.id.loading);
        mCropView = (ImageCropView)v.findViewById(R.id.cropper);

        return v;
    }

    @Override
    protected void onImageLoadSuccess(Bitmap bitmap) {
        mLoadingProgress.setVisibility(View.GONE);
        mCropView.setBitmap(bitmap);
    }
}
