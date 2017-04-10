package com.flyou.pictureselecter.util;

import android.text.format.Formatter;
import android.widget.Toast;

import com.flyou.pictureselecter.BaseTest;
import com.flyou.pictureselecter.R;
import com.flyou.pictureselecter.config.PictureSelectorConfig;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.*;

/**
 * Created by wanjing on 2017/3/24.
 */
public class PictureToastUtilTest extends BaseTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void showCountOverMaxSelect() throws Exception {
        Toast toast = ShadowToast.getLatestToast();
        assertNull(toast); //toast未弹出

        PictureToastUtil.showCountOverMaxSelect(appContext);

        toast = ShadowToast.getLatestToast();
        assertNotNull(toast); //toast已弹出

        String content = String.format(appContext.getString(R.string.maxnum), PictureSelectorConfig.getInstance().getMaxSelectCount());
        assertEquals(content, ShadowToast.getTextOfLatestToast()); //判断弹出内容是否与预期相同
    }

    @Test
    public void showLengthOver() throws Exception {
        Toast toast = ShadowToast.getLatestToast();
        assertNull(toast); //toast未弹出

        PictureToastUtil.showLengthOver(appContext);

        toast = ShadowToast.getLatestToast();
        assertNotNull(toast); //toast已弹出

        String s = Formatter.formatFileSize(appContext, PictureSelectorConfig.getInstance().getMaxFileLength());
        String content = String.format(appContext.getString(R.string.file_max_size), s);
        assertEquals(content, ShadowToast.getTextOfLatestToast()); //判断弹出内容是否与预期相同
    }

}