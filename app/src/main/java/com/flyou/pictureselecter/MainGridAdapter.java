package com.flyou.pictureselecter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

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

    public MainGridAdapter(Activity context, List<String> mImageUrlList, int maxCount) {
        this.mContext = context;
        this.mImageUrlList = (ArrayList<String>) mImageUrlList;

    }

    @Override
    public int getCount() {
        if (mImageUrlList.size() == mMaxCount) {
            return mMaxCount;
        } else {
            return mImageUrlList.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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


        return convertView;
    }

    public class ViewHolder {
        private ImageView imageView;
        private Button delBtn;

    }
}
