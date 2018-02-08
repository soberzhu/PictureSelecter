package com.flyou.pictureselecter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyou.pictureselecter.PictureSelecter;
import com.flyou.pictureselecter.R;
import com.flyou.pictureselecter.adapter.ChooseChildPicAdapter;
import com.flyou.pictureselecter.adapter.ChooseGroupPicAdapter;
import com.flyou.pictureselecter.bean.ImageBean;
import com.flyou.pictureselecter.bean.ImageGroupBean;
import com.flyou.pictureselecter.config.PictureSelectorConfig;
import com.flyou.pictureselecter.manager.ChooseChangeManager;
import com.flyou.pictureselecter.persenter.GetImagePersenter;
import com.flyou.pictureselecter.util.ListUtil;
import com.flyou.pictureselecter.util.PictureToastUtil;
import com.flyou.pictureselecter.util.StatusBarCompat;
import com.flyou.pictureselecter.view.IGetImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ChoosePictureActivity extends Activity
        implements View.OnClickListener, ChooseChildPicAdapter.OnSelectedChanged, IGetImageView {
    public static final String TAG="ChoosePictureActivity";
    private GridView gv_content;
    private ListView lv_content;
    private LinearLayout ll_click_area;
    private ProgressBar pb_loading;
    private ImageButton back;
    private TextView title;
    private TextView operate;
    private RelativeLayout title_bar;

    private ChooseGroupPicAdapter groupPicAdapter;
    private ChooseChildPicAdapter childPicAdapter;
    private Map<String, ArrayList<ImageBean>> mGroupMap;
    private List<ImageGroupBean> list;
    public static final String SELECTED_IMGS = "selectedImgs";
    private static final int REQUEST_CODE = 0x1000;

    private GetImagePersenter getimagePersenter;
    private ArrayList<ImageBean> selectedPicture;

    private  PictureSelectorConfig config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xdja_picture_selector_activity_choose_pic);
       ArrayList<String> imagePath=(ArrayList<String>) getIntent().getSerializableExtra(PictureSelecter.INTENT_PICTURES_RESULT);
       if (imagePath!=null&&imagePath.size()>0){
        selectedPicture = new ArrayList<>();
        for (String s : imagePath) {
            selectedPicture.add(new ImageBean(s,true));
        }
       }
        config=PictureSelectorConfig.getInstance();
        initView();
        ChooseChangeManager.getInstance().init();

        getimagePersenter = new GetImagePersenter(this, getApplicationContext());
        getimagePersenter.getImages(PictureSelectorConfig.getInstance().isShowGif());

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (groupPicAdapter.getCurrentSelected() == position) return;
                title.setText(list.get(position).getFolderName());
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
                childPicAdapter.refresh(list.get(position).getFolderName());
                ll_click_area.setVisibility(View.GONE);
                groupPicAdapter.setCurrentSelected(position);
                groupPicAdapter.refresh();
            }
        });

        gv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent mIntent = new Intent(ChoosePictureActivity.this,
                        ChooseBigPictureActivity.class);
                mIntent.putExtra("index", position);

                mIntent.putExtra("foldername", list.get(groupPicAdapter.getCurrentSelected()).getFolderName());
                startActivityForResult(mIntent, REQUEST_CODE);

            }
        });
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
        title.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.picture_selector_padding_16));
        title.setOnClickListener(this);
        back.setOnClickListener(this);

        operate.setVisibility(View.VISIBLE);
        operate.setOnClickListener(this);

        ll_click_area.setOnClickListener(this);
    }

    private void initView() {
        this.title_bar= (RelativeLayout) findViewById(R.id.title_bar);
        this.pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        this.ll_click_area = (LinearLayout) findViewById(R.id.ll_click_area);
        this.lv_content = (ListView) findViewById(R.id.lv_pics);
        this.gv_content = (GridView) findViewById(R.id.gv_pics);
        this.back = (ImageButton) findViewById(R.id.imgbtn_back);
        this.title = (TextView) findViewById(R.id.tv_title);
        this.operate = (TextView) findViewById(R.id.tv_operate);

        title_bar.setBackgroundColor(config.getTitleBarBgColor());
        //StatusBarCompat.compat(this, config.getStatusBarColor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        int selectedCount = ChooseChangeManager.getInstance().getChoosedCount();
        if (selectedCount != 0) {

            operate.setText("完成(" + selectedCount + "/" +config.getMaxSelectCount() + ")");


        }
        if (list != null) {
            childPicAdapter.refresh(list.get(groupPicAdapter.getCurrentSelected()).getFolderName());
        }
    }


    /**
     * 组装分组界面ListView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     */
    private List<ImageGroupBean> subGroupOfImage(Map<String, ArrayList<ImageBean>> mGruopMap) {
        if (mGroupMap == null) {
            return null;
        }
        if (mGruopMap.size() == 0) {
            return null;
        }
        List<ImageGroupBean> list = new ArrayList<>();

        Iterator<Map.Entry<String, ArrayList<ImageBean>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<ImageBean>> entry = it.next();
            String key = entry.getKey();
            List<ImageBean> value = entry.getValue();
            if (value != null && value.size() > 0) {

                ImageGroupBean mImageBean = new ImageGroupBean();
                mImageBean.setFolderName(key);
                mImageBean.setImageCounts(value.size());
                mImageBean.setTopImagePath(value.get(0).getPath());//获取该组的第一张图片
                if (TextUtils.equals(getString(R.string.all_picture), key) ||
                        TextUtils.equals(getString(R.string.all_video), key)) {
                    list.add(0, mImageBean);
                } else {
                    list.add(mImageBean);
                }
            }
        }

        return list;

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_title) {
            if (ll_click_area.getVisibility() != View.VISIBLE) {
                ll_click_area.setVisibility(View.VISIBLE);
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_up, 0);
            } else {
                ll_click_area.setVisibility(View.GONE);
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);
            }

        } else if (i == R.id.tv_operate) {
            ArrayList<ImageBean> tempImgs = ChooseChangeManager.getInstance().getChoosedImg();
            ArrayList<String>imagePathList=new ArrayList<>();
            if (tempImgs != null && tempImgs.size() > 0) {
                for (ImageBean tempImg : tempImgs) {
                    imagePathList.add(tempImg.getPath());
                }
                Intent mIntent = new Intent();
                mIntent.putExtra(PictureSelecter.INTENT_PICTURES_RESULT, imagePathList);
                setResult(RESULT_OK, mIntent);
                finish();
            }


        } else if (i == R.id.imgbtn_back) {
            finish();

        } else if (i == R.id.ll_click_area) {
            ll_click_area.setVisibility(View.GONE);
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_down, 0);

        }
    }

    @Override
    public void showLoading() {

        pb_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        pb_loading.setVisibility(View.GONE);
    }

    @Override
    public void showError(String msg) {
        PictureToastUtil.toast(ChoosePictureActivity.this,msg);

    }

    @Override
    public void onGetImages(Map<String, ArrayList<ImageBean>> images) {
        mGroupMap = images;
        ChooseChangeManager.getInstance().setmGroupMap(mGroupMap);
        if (selectedPicture != null && selectedPicture.size() > 0) {
            ChooseChangeManager.getInstance().setSelectPic(selectedPicture);
            onChanged();
        }
        groupPicAdapter = new ChooseGroupPicAdapter(this, list = subGroupOfImage(mGroupMap), false);
        lv_content.setAdapter(groupPicAdapter);
        groupPicAdapter.setCurrentSelected(0);
        groupPicAdapter.refresh();
        if (!ListUtil.isEmpty(list)) {
            title.setText(list.get(0).getFolderName());
            childPicAdapter = new ChooseChildPicAdapter(this, list.get(0).getFolderName(), this);
            gv_content.setAdapter(childPicAdapter);
        } else {
            title.setText("相册里没有图像");
            childPicAdapter = new ChooseChildPicAdapter(this, null, this);
            gv_content.setAdapter(childPicAdapter);
        }

    }

    @Override
    public void onChanged() {
        int selectedCount = ChooseChangeManager.getInstance().getChoosedCount();
        if (selectedCount == 0) {
            operate.setText("");
        } else {
            operate.setText("完成(" + selectedCount + "/" + config.getMaxSelectCount() + ")");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Intent mIntent = new Intent();
                    mIntent.putExtra(PictureSelecter.INTENT_PICTURES_RESULT,
                            data.getSerializableExtra(PictureSelecter.INTENT_PICTURES_RESULT));
                setResult(RESULT_OK, mIntent);
                finish();
            }
        }
    }
}
