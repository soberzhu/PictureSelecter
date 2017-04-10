package com.flyou.pictureselecter;

import com.flyou.pictureselecter.ui.ChoosePictureActivity;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by wanjing on 2017/3/25.
 */
public class PictureSelecterTest extends BaseTest {

    // class under test
    private PictureSelecter pictureSelecter;

    private ChoosePictureActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(ChoosePictureActivity.class);
    }

    @Test
    public void getPicture() throws Exception {
        PictureSelecter.getPicture(activity, 0x1111, "result key");

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        ShadowActivity.IntentForResult nextIntent = shadowActivity.getNextStartedActivityForResult();
        assertEquals(ChoosePictureActivity.class.getName(), nextIntent.intent.getComponent().getClassName());
        assertEquals(PictureSelecter.requestCode, nextIntent.requestCode);
    }

    @Test
    public void getPicture1() throws Exception {
        ArrayList<String> selectedList = new ArrayList<>();
        selectedList.addAll(Arrays.asList("one", "two", "three"));
        PictureSelecter.getPicture(activity, 0x1111, selectedList, "result key");

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        ShadowActivity.IntentForResult nextIntent = shadowActivity.getNextStartedActivityForResult();
        assertEquals(ChoosePictureActivity.class.getName(), nextIntent.intent.getComponent().getClassName());
        assertEquals(PictureSelecter.requestCode, nextIntent.requestCode);
        assertEquals(selectedList, nextIntent.intent.getSerializableExtra(PictureSelecter.INTENT_PICTURES_RESULT));
    }
}