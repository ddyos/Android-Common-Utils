package com.ddyos.android.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.ScrollView;

/**
 * Bitmap相关的工具类
 *
 * @author ddyos
 */
public class BitmapUtils {

    /**
     * 截取scrollview的显示内容作为Bitmap返回
     */
    public static Bitmap getBitmapFromScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 获取view的显示内容
     * @param view
     * @return
     */
    public static Bitmap getDrawingCacheFromView(View view){
        Bitmap bitmap=null;
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 清空缓存,防止重复截旧内容
        view.destroyDrawingCache();
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        bitmap=view.getDrawingCache();
        int width = view.getWidth();
        int height = view.getHeight();
        // 根据坐标点和需要的宽和高创建bitmap
        bitmap=Bitmap.createBitmap(bitmap, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 回收Bitmap资源
     * @param bitmap
     */
    public static void recycleBitmap(Bitmap bitmap){
        if(bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 根据给出的长宽,从File中获取合适大小的Bitmap
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {

        return decodeSampledBitmapFromFile(pathName, reqWidth, reqHeight ,Bitmap.Config.ARGB_8888);
    }

    /**
     * 根据给出的长宽,从File中获取合适大小的Bitmap
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @param inPreferredConfig 图片质量格式,参见 Bitmap.Config
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight , Bitmap.Config inPreferredConfig ) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //set the config
        options.inPreferredConfig = inPreferredConfig;

        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * 根据给出的长宽,从Resource中获取合适大小的Bitmap
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 根据给出的长,宽,计算出合适的inSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;//设置inSampleSize为2的幂是因为decoder最终还是会对非2的幂的数进行向下处理，获取到最靠近2的幂的数。
            }
        }
        return inSampleSize;
    }
}

