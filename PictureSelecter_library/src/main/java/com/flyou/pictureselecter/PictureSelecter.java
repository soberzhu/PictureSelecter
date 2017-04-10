package com.flyou.pictureselecter;

import android.app.Activity;
import android.content.Intent;

import com.flyou.pictureselecter.config.PictureSelectorConfig;
import com.flyou.pictureselecter.ui.ChoosePictureActivity;

import java.util.ArrayList;

/**
 * Created by fzl on 2017/3/21.
 * VersionCode: 1
 * Desc:
 */

public class PictureSelecter {
    public static String INTENT_PICTURES_RESULT;
    public static int requestCode;
    public static void getPicture(Activity activity, PictureSelectorConfig config, int requestCode,String resultKey) {
//        config.getMaxSelectCount()
        PictureSelecter.INTENT_PICTURES_RESULT = resultKey;
        getPicture(activity, requestCode,null,resultKey);
    }

    public static void getPicture(Activity activity, int requestCode,String resultKey) {
        PictureSelecter.INTENT_PICTURES_RESULT = resultKey;
        getPicture(activity,requestCode,null,resultKey);
    }

    public static void getPicture(Activity activity, int requestCode, ArrayList<String> selectPathList,String resultKey) {
        PictureSelecter.INTENT_PICTURES_RESULT = resultKey;
        PictureSelecter. requestCode=requestCode;
        Intent intent = new Intent(activity, ChoosePictureActivity.class);
        if (selectPathList!=null && selectPathList.size() != 0){
            intent.putStringArrayListExtra(INTENT_PICTURES_RESULT,selectPathList);
        }
        activity.startActivityForResult(intent, requestCode);
    }
}
