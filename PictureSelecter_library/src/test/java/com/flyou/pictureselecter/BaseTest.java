package com.flyou.pictureselecter;

import android.content.Context;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by wanjing on 2017/3/24.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BaseTest {

    protected Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext = RuntimeEnvironment.application;
    }
}
