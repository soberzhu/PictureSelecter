package com.flyou.pictureselecter.view;

import com.flyou.pictureselecter.base.IBaseView;
import com.flyou.pictureselecter.bean.ImageBean;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fzl on 2017/3/20.
 * VersionCode: 1
 * Desc:
 */

public interface IGetImageView extends IBaseView {
    void onGetImages(Map<String, ArrayList<ImageBean>> images);
}
