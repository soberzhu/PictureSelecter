package com.flyou.pictureselecter.manager;

import android.text.TextUtils;

import com.flyou.pictureselecter.bean.ImageBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ChooseChangeManager {

    private Map<String, ArrayList<ImageBean>> mGroupMap;

    private static final ChooseChangeManager manager = new ChooseChangeManager();

    private ChooseChangeManager() {
    }

    public static ChooseChangeManager getInstance() {
        return manager;
    }

    public Map<String, ArrayList<ImageBean>> getmGroupMap() {
        return mGroupMap;
    }

    public void setmGroupMap(Map<String, ArrayList<ImageBean>> mGroupMap) {
        this.mGroupMap = mGroupMap;
    }

    public void setSelectPic(List<ImageBean> selectPics) {
        mSelectedImgs = selectPics; //已选中图片，按顺序排列。
        if (mGroupMap != null) {
            Iterator<Map.Entry<String, ArrayList<ImageBean>>> it = mGroupMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<ImageBean>> entry = it.next();
                if (!TextUtils.equals("所有图片", entry.getKey())
                        && !TextUtils.equals("所有视频", entry.getKey())
                        && !TextUtils.equals("图片和视频", entry.getKey())) {
                    List<ImageBean> value = entry.getValue();
                    for (int i = 0; i < value.size(); i++) {
                        for (int j = 0; j < selectPics.size(); j++) {
                            if (TextUtils.equals(selectPics.get(j).getPath(), value.get(i).getPath())) {
                                value.get(i).setSelected(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<ImageBean> getChoosedImg() {
        ArrayList<ImageBean> list = new ArrayList<>();
        if (mGroupMap != null) {
            Iterator<Map.Entry<String, ArrayList<ImageBean>>> it = mGroupMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<ImageBean>> entry = it.next();
                if (!TextUtils.equals("所有图片", entry.getKey())
                        && !TextUtils.equals("所有视频", entry.getKey())
                        && !TextUtils.equals("图片和视频", entry.getKey())) {
                    List<ImageBean> value = entry.getValue();
                    for (int i = 0; i < value.size(); i++) {
                        if (value.get(i).isSelected()) {
                            list.add(value.get(i));
                        }
                    }
                }
            }
        }
        return list;
    }

    public int getChoosedCount() {
        return getChoosedImg().size();
    }

    public void init() {
        if (mGroupMap != null) {
            mGroupMap.clear();
        }
        if (mSelectedImgs != null) {
            mSelectedImgs.clear();
        }
    }

    private static List<ImageBean> mSelectedImgs = new ArrayList<>();

    public static List<ImageBean> getmSelectedImgs() {
        return mSelectedImgs;
    }

    public static void setmSelectedImgs(ArrayList<ImageBean> mSelectedImgs) {
        ChooseChangeManager.mSelectedImgs = mSelectedImgs;
    }
}
