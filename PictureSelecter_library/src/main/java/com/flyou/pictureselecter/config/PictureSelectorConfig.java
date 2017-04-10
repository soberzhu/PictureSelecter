package com.flyou.pictureselecter.config;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.flyou.pictureselecter.R;

/**
 * Created by fzl on 2017/3/20.
 * VersionCode: 1
 * Desc:
 */

public class PictureSelectorConfig {
    private static PictureSelectorConfig instance;
    private static int maxSelectCount = 9;
    private static long maxFileLength = 10 * 1024 * 1024;
    private static boolean showGif = false;
    private static int statusBarColor = 0xff3173ba;
    private static int titleBarBgColor = 0xff3173ba;
    private
    @DrawableRes
    int checkDrawableNormal = R.drawable.cbx_choose_pic_unselected;
    private
    @DrawableRes
    int checkDrawablePressed = R.drawable.cbx_choose_pic_selected;


    private PictureSelectorConfig() {

    }

    public static PictureSelectorConfig getInstance() {
        if (instance == null) {
            synchronized (PictureSelectorConfig.class) {
                if (instance == null) {
                    instance = new PictureSelectorConfig();
                }
            }
        }
        return instance;
    }

    public int getCheckDrawableNormal() {
        return checkDrawableNormal;
    }

    public void setCheckDrawableNormal(@DrawableRes int checkDrawableNormal) {
        this.checkDrawableNormal = checkDrawableNormal;
    }

    public int getCheckDrawablePressed() {
        return checkDrawablePressed;
    }

    public void setCheckDrawablePressed(@DrawableRes int checkDrawablePressed) {
        this.checkDrawablePressed = checkDrawablePressed;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public void setStatusBarColor(@ColorInt int statusBarColor) {
        PictureSelectorConfig.statusBarColor = statusBarColor;
    }

    public int getTitleBarBgColor() {
        return titleBarBgColor;
    }

    public void setTitleBarBgColor(@ColorInt int titleBarBgColor) {
        PictureSelectorConfig.titleBarBgColor = titleBarBgColor;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        PictureSelectorConfig.showGif = showGif;
    }

    public long getMaxFileLength() {
        return maxFileLength;
    }

    public void setMaxFlieLength(long maxFlieLength) {
        PictureSelectorConfig.maxFileLength = maxFlieLength;
    }

    public void setMaxFlieLengthMB(int maxMB) {
        PictureSelectorConfig.maxFileLength = maxMB * 1024 * 1024;
    }

    public int getMaxSelectCount() {
        return maxSelectCount;
    }

    public void setMaxSelectCount(int maxSelectCount) {
        PictureSelectorConfig.maxSelectCount = maxSelectCount;
    }

    public void config(int maxSelectCount, long maxFlieLength) {
        PictureSelectorConfig.maxSelectCount = maxSelectCount;
        PictureSelectorConfig.maxFileLength = maxFlieLength;
    }

    public void config(int maxSelectCount, long maxFileLength, boolean showGif) {
        config(maxSelectCount, maxFileLength);
        this.showGif = showGif;
    }

    public void config(int maxSelectCount, long maxFileLength, boolean showGif,
                       @ColorInt int statusBarColor, @ColorInt int titleBarBgColor) {

        config(maxSelectCount, maxFileLength, showGif);
        this.statusBarColor = statusBarColor;
        this.titleBarBgColor = titleBarBgColor;

    }

    public void config(int maxSelectCount, long maxFileLength, boolean showGif,
                       @ColorInt int statusBarColor, @ColorInt int titleBarBgColor,
                       @DrawableRes int checkDrawableNormal, @DrawableRes int checkDrawablePressed) {

        config(maxSelectCount, maxFileLength, showGif, statusBarColor, titleBarBgColor);
        this.checkDrawableNormal = checkDrawableNormal;
        this.checkDrawablePressed = checkDrawablePressed;

    }


}
