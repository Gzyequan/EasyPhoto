package com.yequan.easyphoto.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.yequan.easyphoto.constant.Constant;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/6/7 0007.
 */

public class FileUtils {

    public interface SavePhotoListener {
        public void onStart();

        public void onSuccess(byte[] data, String photoName);

        public void onError();
    }

    //从sh脚本中加载shader内容的方法
    public static String loadFromAssets(String fileName, Resources r) {
        String result = null;
        try {
            InputStream in = r.getAssets().open(fileName);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String loadFromSDCard(String filePath) {
        StringBuffer sb = new StringBuffer();
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String storagePath = "";

    private static String initPath() {
        if (storagePath.equals("")) {
            storagePath = Constant.PHOTO_PATH;
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        return storagePath;
    }

    public static boolean savePhoto(Bitmap b, String photoName) {
        boolean compress = false;
        String path = initPath();
        String jpegName = path + "/" + photoName + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            compress = b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            Log.i(TAG, "save bitmap error");
            e.printStackTrace();
        }
        return compress;
    }

    public static void savePhoto(final byte[] data, final SavePhotoListener listener) {

        new AsyncTask<Void, Boolean, Boolean>() {
            Bitmap bitmap = null;
            String photoName = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                listener.onStart();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                if (null != bitmap) {
                    Bitmap rotateBitmap = ImageUtil.getRotateBitmap(bitmap, 90.0f);
                    photoName = TimeUtil.getCurrentTimeMillis() + "";
                    return savePhoto(rotateBitmap, photoName);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    listener.onSuccess(data, photoName);
                } else {
                    listener.onError();
                }
            }
        }.execute();
    }
}
