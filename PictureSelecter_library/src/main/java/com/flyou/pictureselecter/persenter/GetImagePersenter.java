package com.flyou.pictureselecter.persenter;

import android.content.Context;

import com.flyou.pictureselecter.bean.ImageBean;
import com.flyou.pictureselecter.model.GetImageModel;
import com.flyou.pictureselecter.model.IGetImageModel;
import com.flyou.pictureselecter.view.IGetImageView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fzl on 2017/3/20.
 * VersionCode: 1
 * Desc:
 */

public class GetImagePersenter implements GetImageModel.GetImageListenter {
    private IGetImageView loginView;
    private IGetImageModel loginModel;

    public GetImagePersenter(IGetImageView loginView, Context context) {
        this.loginView = loginView;
        this.loginModel = new GetImageModel(context);
    }
   public void getImages(boolean showGif){
       loginModel.getImages(showGif,this);
   }


    @Override
    public void OnStart() {
        loginView.showLoading();
    }

    @Override
    public void onSuccess(Map<String, ArrayList<ImageBean>> imageList) {
        loginView.hideLoading();
        loginView.onGetImages(imageList);
    }

    @Override
    public void OnError(Exception e) {
        loginView.showError(e.getMessage());
    }
}
