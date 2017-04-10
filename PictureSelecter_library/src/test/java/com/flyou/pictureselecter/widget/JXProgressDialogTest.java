package com.flyou.pictureselecter.widget;

import android.app.Dialog;

import com.flyou.pictureselecter.BaseTest;
import com.flyou.pictureselecter.ui.ChoosePictureActivity;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowDialog;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by wanjing on 2017/3/25.
 */
public class JXProgressDialogTest extends BaseTest {

    // class under test
    private JXProgressDialog jxProgressDialog;

    private String msg = "some test";

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        jxProgressDialog = new JXProgressDialog();
    }

    @Test
    public void show() throws Exception {
        jxProgressDialog.show(appContext, msg); // appContext 是应用上下文
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
        assertTrue(dialog.isShowing());
        // 验证弹出的dialog的属性是否与预期相同
        ShadowDialog shadowDialog = shadowOf(dialog);
        assertFalse(shadowDialog.isCancelableOnTouchOutside());
        assertTrue(shadowDialog.isCancelable());
        assertNull(shadowDialog.getOnCancelListener());

        jxProgressDialog.dismiss();
        dialog = ShadowDialog.getLatestDialog();
        assertFalse(dialog.isShowing()); //验证dialog是否被关闭
    }

    @Test
    public void show1() throws Exception {
        jxProgressDialog.show(null, msg);
    }

    @Test
    public void show2() throws Exception {
        ChoosePictureActivity activity = Robolectric.setupActivity(ChoosePictureActivity.class);
        jxProgressDialog.show(activity, msg);
    }
}