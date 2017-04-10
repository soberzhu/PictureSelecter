package com.flyou.pictureselecter.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyou.pictureselecter.PictureSelecter;
import com.flyou.pictureselecter.R;
import com.flyou.pictureselecter.adapter.ChooseBigPicAdapter;
import com.flyou.pictureselecter.bean.ImageBean;
import com.flyou.pictureselecter.config.PictureSelectorConfig;
import com.flyou.pictureselecter.manager.ChooseChangeManager;
import com.flyou.pictureselecter.util.FileUtils;
import com.flyou.pictureselecter.util.PictureToastUtil;

import java.util.ArrayList;
import java.util.List;


public class ChooseBigPictureActivity extends Activity
        implements ViewPager.OnPageChangeListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, ChooseBigPicAdapter.OnPhotoTapListener {


    private ViewPager vPager;

    private View titleBar;

    private ImageButton back;

    private TextView title;

    private TextView operate;


    private View cbPicFoot;


    private CheckBox cbSelected;

    private List<ImageBean> images;
    private int index;
    private String folderName;
    private PictureSelectorConfig config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = PictureSelectorConfig.getInstance();
        //不显示系统的标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.xdja_picture_selector_activity_choose_big_pic);


        initView();
        index = getIntent().getIntExtra("index", 0);

        folderName = getIntent().getStringExtra("foldername");


        images = ChooseChangeManager.getInstance().getmGroupMap().get(folderName);
        title.setText((index + 1) + "/" + images.size());


        showBottomView(index);

        cbSelected.setOnCheckedChangeListener(this);

        ChooseBigPicAdapter adapter = new ChooseBigPicAdapter(this, images);
        adapter.setOnPhotoTapListener(this);
        vPager.setAdapter(adapter);
        vPager.setCurrentItem(index);
        vPager.setOnPageChangeListener(this);
        back.setOnClickListener(this);
        operate.setOnClickListener(this);
    }

    private void initView() {
        vPager = (ViewPager) findViewById(R.id.vPager);
        titleBar = findViewById(R.id.title_bar);
        back = (ImageButton) findViewById(R.id.imgbtn_back);
        title = (TextView) findViewById(R.id.tv_title);
        operate = (TextView) findViewById(R.id.tv_operate);
        vPager = (ViewPager) findViewById(R.id.vPager);
        cbPicFoot = findViewById(R.id.cb_pic_foot);

        cbSelected = (CheckBox) findViewById(R.id.cb_select);
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = getResources().getDrawable(config.getCheckDrawableNormal());
        Drawable press = getResources().getDrawable(config.getCheckDrawablePressed());
        drawable.addState(new int[]{android.R.attr.state_checked}, press);
        drawable.addState(new int[]{}, normal);

        cbSelected.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        int selectedCount = ChooseChangeManager.getInstance().getChoosedCount();
        if (selectedCount != 0) {

            operate.setText("完成(" + selectedCount + "/" + PictureSelectorConfig.getInstance().getMaxSelectCount() + ")");


        } else {

            operate.setText("完成");

        }
    }

    public void onChanged() {
        int selectedCount = ChooseChangeManager.getInstance().getChoosedCount();
        if (selectedCount == 0) {
            operate.setText("完成");
        } else {
            operate.setText("完成(" + selectedCount + "/" + PictureSelectorConfig.getInstance().getMaxSelectCount() + ")");

        }
    }

    public void showBottomView(int position) {
        if (titleBar.getVisibility() != View.VISIBLE) {
            cbPicFoot.setVisibility(View.GONE);
        } else {
            cbPicFoot.setVisibility(View.VISIBLE);
        }


        if (images.get(position).isSelected()) {
            cbSelected.setChecked(true);
        } else {
            cbSelected.setChecked(false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        title.setText((position + 1) + "/" + images.size());
        index = position;
        showBottomView(index);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_operate) {

            ArrayList<ImageBean> tempImgs = ChooseChangeManager.getInstance().getChoosedImg();
            ArrayList<String> imagePathList = new ArrayList<>();
            if (tempImgs != null && tempImgs.size() > 0) {

                for (ImageBean tempImg : tempImgs) {
                    imagePathList.add(tempImg.getPath());
                }
                Intent mIntent = new Intent();
                mIntent.putExtra(PictureSelecter.INTENT_PICTURES_RESULT, imagePathList);
                setResult(RESULT_OK, mIntent);
                finish();
            } else {
                ImageBean imageBean = ChooseChangeManager.getInstance().getmGroupMap().get(folderName).get(vPager.getCurrentItem());
                if (FileUtils.isFileExist(imageBean.getPath())) {
                    if (tempImgs == null) {
                        tempImgs = new ArrayList<>();
                    }
                    imagePathList.add(imageBean.getPath());
                    Intent mIntent = new Intent();
                    mIntent.putExtra(PictureSelecter.INTENT_PICTURES_RESULT, imagePathList);
                    setResult(RESULT_OK, mIntent);
                    finish();
                } else {
                    PictureToastUtil.toast(ChooseBigPictureActivity.this, getString(R.string.pic_invalid));

                }

            }

        } else if (i == R.id.imgbtn_back) {
            finish();

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int i = compoundButton.getId();
        if (i == R.id.cb_select) {
            if (ChooseChangeManager.getInstance().getChoosedCount() >= PictureSelectorConfig.getInstance().getMaxSelectCount()
                    && isChecked
                    && !images.get(index).isSelected()) {
                PictureToastUtil.showCountOverMaxSelect(getApplicationContext());

                cbSelected.setChecked(false);

                return;
            } else {
                if (FileUtils.getFileSize(images.get(index).getPath()) >= PictureSelectorConfig.getInstance().getMaxFileLength()) {
                    PictureToastUtil.showLengthOver(getApplicationContext());
                    cbSelected.setChecked(false);
                    return;
                }

                // 记录图片选中顺序。
                if (isChecked) {
                    ChooseChangeManager.getmSelectedImgs().add(images.get(index));
                } else {
                    ChooseChangeManager.getmSelectedImgs().remove(images.get(index));
                }
                images.get(index).setSelected(isChecked);

            }
            ChooseBigPictureActivity.this.onChanged();

        }
    }


    @Override
    public void onPhotoTap() {
        if (titleBar.getVisibility() == View.VISIBLE) {
            titleBar.setVisibility(View.GONE);
            cbPicFoot.setVisibility(View.GONE);
        } else {
            titleBar.setVisibility(View.VISIBLE);

            cbPicFoot.setVisibility(View.VISIBLE);

        }
    }


}
