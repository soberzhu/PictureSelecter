package com.flyou.pictureselecter;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.flyou.pictureselecter.bean.ImageBean;
import com.flyou.pictureselecter.view.IGetImageView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Map;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest implements IGetImageView {
    @Test
    public void useAppContext() throws Exception {

    }

    @Override
    public void showLoading() {
        Log.d("ExampleInstrumentedTest", "showLoad");
    }

    @Override
    public void hideLoading() {
        Log.d("hideLoading", "showLoad");
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onGetImages(Map<String, ArrayList<ImageBean>> images) {
        Log.d("ExampleInstrumentedTest", "images.size():" + images.size());
    }
}
