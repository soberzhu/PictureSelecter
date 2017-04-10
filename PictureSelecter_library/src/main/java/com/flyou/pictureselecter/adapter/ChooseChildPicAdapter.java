package com.flyou.pictureselecter.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyou.pictureselecter.R;
import com.flyou.pictureselecter.bean.ImageBean;
import com.flyou.pictureselecter.config.PictureSelectorConfig;
import com.flyou.pictureselecter.manager.ChooseChangeManager;
import com.flyou.pictureselecter.util.FileUtils;
import com.flyou.pictureselecter.util.PictureToastUtil;

import java.util.List;


public class ChooseChildPicAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ImageBean> pics;
    private OnSelectedChanged onSelectedChanged;
    private PictureSelectorConfig config;


    public interface OnSelectedChanged {
        void onChanged();
    }

    public ChooseChildPicAdapter(Context context, String folderName, OnSelectedChanged changed) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.pics = ChooseChangeManager.getInstance().getmGroupMap().get(folderName);
        this.onSelectedChanged = changed;

        this. config=PictureSelectorConfig.getInstance();
    }

    public void refresh(String folderName) {
        pics = ChooseChangeManager.getInstance().getmGroupMap().get(folderName);
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (pics != null) return pics.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (pics != null) return pics.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.xdja_picture_selector_choose_pic_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_pic);

            viewHolder.ll_click_area = (LinearLayout) convertView.findViewById(R.id.ll_click_area);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb_pic);

            StateListDrawable drawable = new StateListDrawable();
            Drawable normal = mContext.getResources().getDrawable(config.getCheckDrawableNormal());
            Drawable press = mContext.getResources().getDrawable(config.getCheckDrawablePressed());
            drawable.addState(new int[]{android.R.attr.state_checked},press);
            drawable.addState(new int[]{},normal);
            viewHolder.cb.setBackgroundDrawable(drawable);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ImageBean imageBean = pics.get(position);

            viewHolder.ll_click_area.setVisibility(View.VISIBLE);
            viewHolder.cb.setVisibility(View.VISIBLE);



            Glide.with(mContext)
                    .load(imageBean.getPath())
                    .asBitmap()
                    .placeholder(R.drawable.img_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.img_pic_default)
                    .into(viewHolder.img);

        final CheckBox temp = viewHolder.cb;
        viewHolder.ll_click_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp.setChecked(!temp.isChecked());
            }
        });
        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (ChooseChangeManager.getInstance().getChoosedCount() >= PictureSelectorConfig.getInstance().getMaxSelectCount()
                        && isChecked
                        && !imageBean.isSelected()) {
                    PictureToastUtil.showCountOverMaxSelect(mContext);
                    temp.setChecked(false);
                    return;
                } else {

                    if (isChecked) {
                        if (FileUtils.getFileSize(imageBean.getPath()) >= PictureSelectorConfig.getInstance().getMaxFileLength()) {
                            PictureToastUtil.showLengthOver(mContext);
                            temp.setChecked(false);
                            return;
                        }
                    }
                    // 记录图片选中顺序。
                    if (isChecked) {
                        if (!ChooseChangeManager.getmSelectedImgs().contains(imageBean)) {
                            ChooseChangeManager.getmSelectedImgs().add(imageBean);
                        }
                    } else {
                        ChooseChangeManager.getmSelectedImgs().remove(imageBean);
                    }
                    imageBean.setSelected(isChecked);
                }
                onSelectedChanged.onChanged();
            }
        });

        viewHolder.cb.setChecked(imageBean.isSelected());
        return convertView;
    }

    public static class ViewHolder {
        public ImageView img;
        public LinearLayout ll_click_area;
        public CheckBox cb;

    }
}
