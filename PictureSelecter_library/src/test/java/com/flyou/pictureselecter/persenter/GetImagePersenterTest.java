package com.flyou.pictureselecter.persenter;

import android.content.ContentResolver;
import android.content.Context;

import com.flyou.pictureselecter.BaseTest;
import com.flyou.pictureselecter.bean.ImageBean;
import com.flyou.pictureselecter.model.GetImageModel;
import com.flyou.pictureselecter.view.IGetImageView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by wanjing on 2017/3/25.
 */
public class GetImagePersenterTest extends BaseTest {

    // class under test
    private GetImagePersenter getImagePersenter;

    private IGetImageView iGetImageView;
    private GetImageModel getImageModel;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Context context = mock(Context.class);
        ContentResolver contentResolver = mock(ContentResolver.class);
        when(context.getContentResolver()).thenReturn(contentResolver);

        iGetImageView = mock(IGetImageView.class);
        getImagePersenter = new GetImagePersenter(iGetImageView, context);

        // 反射 替换私有变量
        getImageModel = mock(GetImageModel.class, Mockito.withSettings().useConstructor(context));
        Field loginModelFiled = getImagePersenter.getClass().getDeclaredField("loginModel");
        loginModelFiled.setAccessible(true);
        loginModelFiled.set(getImagePersenter, getImageModel);
    }

    /**
     * 验证loginModel的getImages是否得到了调用
     *
     * @throws Exception
     */
    @Test
    public void getImages() throws Exception {
        getImagePersenter.getImages(true);

        verify(getImageModel).getImages(eq(true), any(GetImageModel.GetImageListenter.class));
    }

    @Test
    public void onStart() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((GetImageModel.GetImageListenter)invocation.getArgument(1)).OnStart();
                // 验证onStart()中的方法 loginView.showLoading()得到了执行
                verify(iGetImageView).showLoading();
                return "onStart()";
            }
        }).when(getImageModel).getImages(eq(true), any(GetImageModel.GetImageListenter.class));

        // 执行
        getImagePersenter.getImages(true);
    }

    @Test
    public void onSuccess() throws Exception {
        // 构建参数
        Map<String, ArrayList<ImageBean>> map = new HashMap<>();
        ArrayList<ImageBean> list = new ArrayList<>();
        list.addAll(Arrays.asList(new ImageBean("", false), new ImageBean("", true)));
        map.put("", list);

        testSuccess(map);
    }

    @Test
    public void onSuccess_null() throws Exception {
        testSuccess(null);
    }

    private void testSuccess(final Map<String, ArrayList<ImageBean>> map) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((GetImageModel.GetImageListenter)invocation.getArgument(1)).onSuccess(map);
                // 验证onSuccess()中的方法 loginView.hideLoading(), loginView.onGetImages()得到了执行
                verify(iGetImageView).hideLoading();
                verify(iGetImageView).onGetImages(map);
                return "onSuccess()";
            }
        }).when(getImageModel).getImages(eq(true), any(GetImageModel.GetImageListenter.class));

        // 执行
        getImagePersenter.getImages(true);
    }

    @Test
    public void onError() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                ((GetImageModel.GetImageListenter)invocation.getArgument(1)).OnError(new Exception());
                // 验证onError()中的方法 loginView.showError()得到了执行
                verify(iGetImageView).showError(null);
                return "onError()";
            }
        }).when(getImageModel).getImages(eq(true), any(GetImageModel.GetImageListenter.class));

        // 执行
        getImagePersenter.getImages(true);
    }
}