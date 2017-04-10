package com.flyou.pictureselecter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyou.pictureselecter.R;
import com.flyou.pictureselecter.bean.ImageGroupBean;

import java.util.List;


public class ChooseGroupPicAdapter extends BaseAdapter {

    private List<ImageGroupBean> groupBeans;
    private LayoutInflater inflater;
    private Context mContext;

    public void setCurrentSelected(int currentSelected) {
        this.currentSelected = currentSelected;
    }

    public int getCurrentSelected() {
        return currentSelected;
    }

    private int currentSelected;



    public ChooseGroupPicAdapter(Context context, List<ImageGroupBean> groupBeans, boolean isVideoThumbnail) {
        this.groupBeans = groupBeans;
        inflater = LayoutInflater.from(context);
        this.mContext = context;

    }

    public void refresh() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (groupBeans != null) return groupBeans.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (groupBeans != null) return groupBeans.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.xdja_picture_selector_choose_group_pic_item, null);
            viewHolder.img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
            viewHolder.tv_fileName = (TextView) convertView.findViewById(R.id.tv_file_name);
            viewHolder.tv_fileCount = (TextView) convertView.findViewById(R.id.tv_file_count);
            viewHolder.img_selected = (ImageView) convertView.findViewById(R.id.img_selected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageGroupBean groupBean = groupBeans.get(position);

            Glide.with(mContext).load(groupBean.getTopImagePath())
                    .placeholder(R.drawable.img_pic_default)
                    .error(R.drawable.img_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.img_pic);

        viewHolder.tv_fileName.setText(groupBean.getFolderName());
        viewHolder.tv_fileCount.setText(groupBean.getImageCounts() + "");
        viewHolder.img_selected.setVisibility(View.GONE);
        if (position == currentSelected) {
            viewHolder.img_selected.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        public ImageView img_pic;
        public TextView tv_fileName;
        public TextView tv_fileCount;
        public ImageView img_selected;
    }
}
