package cn.wanther.cropper.sample.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import cn.wanther.cropper.ImageCropView;
import cn.wanther.cropper.core.Configuration;
import cn.wanther.cropper.sample.R;

/**
 * Created by wanghe on 2015/7/12.
 */
public class NoneRatioTestFragment extends Fragment implements View.OnClickListener {

    protected static Handler sHandler = new Handler();

    private ViewGroup mCropperContainer;
    private View mCropBtn;
    private View mRotateLeftBtn;
    private View mRotateRightBtn;
    protected ImageCropView mCropView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_layout_01, container, false);

        mCropperContainer = (ViewGroup)v.findViewById(R.id.crop_container);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCropBtn = view.findViewById(R.id.crop);
        mRotateLeftBtn = view.findViewById(R.id.rotate_left);
        mRotateRightBtn = view.findViewById(R.id.rotate_right);

        mCropBtn.setOnClickListener(this);
        mRotateLeftBtn.setOnClickListener(this);
        mRotateRightBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Thread(new ImageLoadTask()).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mCropView != null) {
            mCropView.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.crop:
                showCroppedImage();
                break;
            case R.id.rotate_left:
                rotateLeft();
                break;
            case R.id.rotate_right:
                rotateRight();
                break;
        }
    }

    protected void rotateLeft(){
        if(mCropView != null){
            mCropView.rotateLeft();
        }
    }

    protected void rotateRight(){
        if(mCropView != null){
            mCropView.rotateRight();
        }
    }

    protected void showCroppedImage(){
        if(mCropView != null){
            final Bitmap croppedBitmap = mCropView.getCroppedBitmap();
            if(croppedBitmap == null){
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                return;
            }

            ImageView croppedImageView = new ImageView(getActivity());
            croppedImageView.setImageBitmap(croppedBitmap);
            new AlertDialog.Builder(getActivity()).setView(croppedImageView)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(croppedBitmap != null){
                                croppedBitmap.recycle();
                            }
                        }
                    })
                    .create()
                    .show();
        }
    }

    private class ImageLoadTask implements Runnable{
        @Override
        public void run() {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int targetSize = Math.max(metrics.widthPixels, metrics.heightPixels);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), R.mipmap.crop_pic_01, options);

            int sampleSize = calculateSampleSize(targetSize, options.outWidth, options.outHeight);

            options.inJustDecodeBounds = false;
            options.inSampleSize = sampleSize;

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.crop_pic_01);
            sHandler.post(new ImageLoadCallback(bmp));
        }
    }

    private class ImageLoadCallback implements Runnable {
        private Bitmap mBitmap;

        public ImageLoadCallback(Bitmap bitmap){
            mBitmap = bitmap;
        }
        @Override
        public void run() {
            onImageLoadSuccess(mBitmap);
            mBitmap = null;
        }
    }

    private int calculateSampleSize(int targetSize, int width, int height) {
        return 1;
    }

    protected void onImageLoadSuccess(Bitmap bitmap) {
        Configuration config = new Configuration.Builder(getActivity())
                .build();

        mCropView = new ImageCropView(getActivity(), config);
        mCropView.setBitmap(bitmap);

        mCropperContainer.removeAllViews();
        mCropperContainer.addView(mCropView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }
}
