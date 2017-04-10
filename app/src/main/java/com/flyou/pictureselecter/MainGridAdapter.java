package com.flyou.pictureselecter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fzl on 2017/4/10.
 * VersionCode: 1
 * Desc:
 */

public class MainGridAdapter extends BaseAdapter {
    private Activity mContext;
    private List<String> mImageUrlList;
    private int mMaxCount;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_ADD = 2;
    private int mAddPicture=R.drawable.add_img_btn_selector;

    public MainGridAdapter(Activity context, List<String> mImageUrlList, int maxCount) {
        this.mContext = context;
        this.mImageUrlList = (ArrayList<String>) mImageUrlList;
        this.mMaxCount = maxCount;

        if (mImageUrlList == null) {
            return;
        }
        if (mImageUrlList.size() < mMaxCount) {
            mImageUrlList.add(null);
        }
    }

    @Override
    public int getCount() {
        return mImageUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageUrlList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mImageUrlList.size() < mMaxCount) {
            if (mImageUrlList.size() != position + 1) {
                return TYPE_IMAGE;
            } else {
                return TYPE_ADD;
            }
        } else {
            return TYPE_IMAGE;
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.xdja_feed_back_grid_image_item, null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_feed_back_item);
            holder.delBtn = (Button) convertView.findViewById(R.id.delBtn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUrlList.remove(position);
                notifyDataSetChanged();
            }
        });
        switch (getItemViewType(position)) {
            case TYPE_ADD:
                holder.delBtn.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(mAddPicture)
                        .asBitmap()
                        .placeholder(R.drawable.img_pic_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.img_pic_default)
                        .into(holder.imageView);

                break;
            case TYPE_IMAGE:
                holder.delBtn.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(mImageUrlList.get(position))
                        .asBitmap()
                        .placeholder(R.drawable.img_pic_default)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.img_pic_default)
                        .into(holder.imageView);

                break;
        }


        return convertView;
    }

    public class ViewHolder {
        private ImageView imageView;
        private Button delBtn;

    }
}
