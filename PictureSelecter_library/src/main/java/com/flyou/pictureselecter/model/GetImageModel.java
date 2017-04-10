package com.flyou.pictureselecter.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.flyou.pictureselecter.R;
import com.flyou.pictureselecter.bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fzl on 2017/3/20.
 * VersionCode: 1
 * Desc:
 */

public class GetImageModel implements IGetImageModel {



    private Context context;


    public GetImageModel(Context context) {
        this.context = context;

    }

    public interface GetImageListenter {
        void OnStart();
        void onSuccess(Map<String, ArrayList<ImageBean>> imageList);

        void OnError(Exception e);

    }

    @Override
    public void getImages(boolean showGif ,GetImageListenter listenter) {
        new GetImageAsync(showGif,listenter).execute();
    }

    class GetImageAsync extends AsyncTask {

        GetImageListenter listenter;
        boolean showGif;

        public GetImageAsync(boolean showGif,GetImageListenter listenter) {
            this.listenter = listenter;
            this.showGif = showGif;
        }

        @Override
        protected Object doInBackground(Object[] params) {


            Map<String, ArrayList<ImageBean>> mGruopMap = new HashMap<>();
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();

            Cursor mCursor;

            mCursor = mContentResolver.query(mImageUri, null,
                    null,
                    null, MediaStore.Images.Media.DATE_MODIFIED);

            String allName = "";
            allName = context.getString(R.string.all_picture);
            ArrayList<ImageBean> allPics = new ArrayList<>();
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    //获取视频的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    String fileExt = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));//扩展名
                    File file = new File(path);
                    if (!file.exists() || file.length() <= 0
                            || file.isDirectory()) {
                        continue;
                    }
                    if (!showGif&&fileExt.equalsIgnoreCase("image/gif")){
                        continue;
                    }
                    String parentName = new File(path).getParentFile().getName();
                    if (!mGruopMap.containsKey(parentName)) {
                        ArrayList<ImageBean> chileList = new ArrayList<>();
                        ImageBean imageBean = new ImageBean();
                        imageBean.setPath(path);
                        imageBean.setSelected(false);

                        chileList.add(imageBean);
                        mGruopMap.put(parentName, chileList);
                        allPics.add(0, imageBean);
                    } else {
                        ImageBean imageBean = new ImageBean();
                        imageBean.setPath(path);
                        imageBean.setSelected(false);

                        mGruopMap.get(parentName).add(0, imageBean);
                        allPics.add(0, imageBean);
                    }

                }

                mCursor.close();
            }
            if (allPics.size() > 0) {
                mGruopMap.put(allName, allPics);
            }
            return mGruopMap;
        }

        @Override
        protected void onPreExecute() {
            listenter.OnStart();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o==null){
                listenter.OnError(new Exception("获取照片失败"));
                return;
            }
            listenter.onSuccess((Map<String, ArrayList<ImageBean>>) o);
            super.onPostExecute(o);
        }
    }
}
