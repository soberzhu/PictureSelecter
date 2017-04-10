package com.flyou.pictureselecter.util;

import android.content.Context;
import android.text.format.Formatter;
import android.widget.Toast;

import com.flyou.pictureselecter.config.PictureSelectorConfig;
import com.flyou.pictureselecter.R;

/**
 * Created by fzl on 2017/3/21.
 * VersionCode: 1
 * Desc: Toast 工具类
 */

public class PictureToastUtil {
    public static void showCountOverMaxSelect(Context context) {
        toast(context, String.format(context.getString(R.string.maxnum), PictureSelectorConfig.getInstance().getMaxSelectCount()));
    }

    public static void showLengthOver(Context context){
        String s = Formatter.formatFileSize(context, PictureSelectorConfig.getInstance().getMaxFileLength());
        toast(context, String.format(context.getString(R.string.file_max_size),  s));
    }

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
