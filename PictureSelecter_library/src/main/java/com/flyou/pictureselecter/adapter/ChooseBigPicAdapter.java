package com.flyou.pictureselecter.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyou.pictureselecter.R;
import com.flyou.pictureselecter.bean.ImageBean;
import com.flyou.pictureselecter.util.FileUtils;
import com.flyou.pictureselecter.widget.subscaleview.ImageSource;
import com.flyou.pictureselecter.widget.subscaleview.SubsamplingScaleImageView;

import java.util.List;

public class ChooseBigPicAdapter extends PagerAdapter implements SubsamplingScaleImageView.OnImageEventListener {

    private List<ImageBean> images;
    private LayoutInflater inflater;
    private Context mContext;
    private SubsamplingScaleImageView bigImageView;

    private OnPhotoTapListener onPhotoTapListener;

    public ChooseBigPicAdapter(Context context, List<ImageBean> images) {
        this.images = images;
        inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.xdja_picture_selector_choose_big_pic_item, view, false);
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_pic);

        final ImageBean imageBean = images.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoTapListener.onPhotoTap();
            }
        });
        bigImageView = (SubsamplingScaleImageView) imageLayout.findViewById(R.id.img_big_pic);

        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.pb_loading);
        spinner.setVisibility(View.GONE);


            boolean isGif = FileUtils.getFileExtension(imageBean.getPath()).toLowerCase().equals("gif");
            if (isGif) {
                Glide.with(mContext)
                        .load(imageBean.getPath())
                        .asGif()
                        .placeholder(R.drawable.img_pic_big_default)
                        .error(R.drawable.img_pic_big_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
                bigImageView.setVisibility(View.VISIBLE);
                bigImageView.setImage(ImageSource.uri(imageBean.getPath()));
                bigImageView.setOnImageEventListener(this);
                bigImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPhotoTapListener.onPhotoTap();
                    }
                });
            }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoTapListener.onPhotoTap();
            }
        });


        ((ViewPager) view).addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        this.onPhotoTapListener = listener;
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onImageLoaded() {

    }

    @Override
    public void onPreviewLoadError(Exception e) {

    }

    @Override
    public void onImageLoadError(Exception e) {
        bigImageView.setImage(ImageSource.resource(R.drawable.img_pic_big_default));
    }

    @Override
    public void onTileLoadError(Exception e) {
        bigImageView.setImage(ImageSource.resource(R.drawable.img_pic_big_default));
    }

    @Override
    public void onPreviewReleased() {

    }

    public interface OnPhotoTapListener {
        void onPhotoTap();
    }
}
