package com.taidii.diibot.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class BitmapUtils {

    private BitmapUtils() {

    }

    public static void loadBitmapWithListener(Context context, String uri, ImageView
            imageView, RequestListener listener) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions();
        options.dontAnimate();
        Glide.with(context)
                .load(uri)
                .thumbnail(0.1f)
                .apply(options)
                .listener(listener)
                .into(imageView);
    }

    public static void loadBitmapCenterCrop(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context).load(url).apply(options).into(imageView);
    }
    public static void loadBitmapCenterCrop(Context context, String url, ImageView target, int p) {

        Glide.with(context).asBitmap().load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(p).fitCenter()).into(target);

    }
    public static void loadCircleBitmap(final Context context, String url, final ImageView
            imageView, int placeHolder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }

        RequestOptions options = new RequestOptions().placeholder(placeHolder).error(error).centerCrop();

        Glide.with(context).asBitmap().load(url).apply(options)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static void loadFullyBitmap(Context context, String uri, ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions();
        options.dontAnimate();
        options.centerCrop();
        Glide.with(context)
                .load(uri)
                .thumbnail(1f)
                .apply(options)
                .into(imageView);
    }

    public static void loadQualityBitmap(Context context, String uri, float thumbnail, ImageView
            imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions();
        options.dontAnimate();
        options.centerCrop();
        Glide.with(context)
                .load(uri)
                .thumbnail(thumbnail)
                .apply(options)
                .into(imageView);
    }

    public static void loadBitmapNoCenterCrop(Context context, String uri, ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions();
        options.dontAnimate();
        options.dontTransform();
        Glide.with(context)
                .load(uri)
                //.thumbnail(0.1f)
                .apply(options)
                .into(imageView);
    }

    public static void loadBitmapNoTrans(Context context, String url, ImageView imageView, int placeHolder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }

        RequestOptions options = new RequestOptions();
        options.dontAnimate().dontTransform();
        if (placeHolder > 0) {
            options.placeholder(placeHolder);
        }

        if (error > 0) {
            options.error(error);
        }

        RequestBuilder<Bitmap> requestBuilder = Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options);
        requestBuilder.into(imageView);
    }

    public static void loadBitmapNoTrans(Context context, String url, ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions().dontAnimate().dontTransform();
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void loadBitmapDefault(Context ctx, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.dontTransform().dontAnimate();
        Glide.with(ctx).asBitmap().load(url).apply(options).into(imageView);
    }


    public static void loadCircleDrawable(Context context, String res, final View view) {
        Glide.with(context)
                .asBitmap()
                .load(res)
                .into(new SimpleTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                      //  view.setBackground(new BitmapDrawable(resource));
                    }
                });
    }
    public static void loadBitmapDefault(Context ctx, String url, ImageView imageView, int place,
                                         int error) {

        RequestOptions options = new RequestOptions();

        if (place > 0) {
            options.placeholder(place);
        }
        if (error > 0) {
            options.error(error);
        }

        RequestBuilder<Drawable> requestBuilder = Glide.with(ctx).load(url).apply(options);
        requestBuilder.into(imageView);
    }

    public static void loadFullyBitmap(Fragment fragment, String url, ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(fragment)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void loadBitmap(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        Glide.with(context).load(url).apply(options.centerInside()).into(imageView);
    }

    public static void loadBitmap(Context context, String url, ImageView imageView, boolean isScreen) {
        RequestOptions options = new RequestOptions();
        if (!isScreen){
            options.centerInside();
        }
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadAvatar(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(new RequestOptions().centerInside().dontAnimate()).into(imageView);
    }

    public static void loadAvatar(Context context, String url, ImageView imageView, int place, int
            error) {

        RequestOptions options = new RequestOptions();
        if (place > 0) {
            options.placeholder(place);
        }

        if (error > 0) {
            options.error(error);
        }

        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url).apply(options
                .centerInside().dontAnimate());
        requestBuilder.into(imageView);
    }

    public static void loadBitmap(Context context, String url, ImageView imageView, int place, int
            error) {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        if (place > 0) {
            options.placeholder(place);
        }

        if (error > 0) {
            options.error(error);
        }


        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url).apply(options
                .centerInside());
        requestBuilder.into(imageView);
    }

    public static void loadBitmapCenterCrop(Context context, String url, ImageView imageView, int place, int
            error) {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        if (place > 0) {
            options.placeholder(place);
        }

        if (error > 0) {
            options.error(error);
        }
        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url).apply(options
                .centerCrop());
        requestBuilder.into(imageView);
    }

    public static void loadFullyBitmap(Context context, File file, ImageView imageView, int
            width, int height) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.override(width, height);
        Glide.with(context)
                .load(file)
                .apply(options)
                .into(imageView);
    }

    public static void loadFullyBitmap(Context context, File file, ImageView imageView, int
            placeHolder) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions().centerCrop();
        if (placeHolder > 0) {
            options.placeholder(placeHolder);
        }
        RequestBuilder<Drawable> requestBuilder = Glide.with(context)
                .load(file)
                .apply(options);
        requestBuilder.into(imageView);
    }

    public static void loadBitmapNoCache(Context context, String uri, ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("target ImageView is null");
        }
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        options.skipMemoryCache(true);
        options.dontAnimate();
        options.centerCrop();
        Glide.with(context)
                .load(uri)
                .apply(options)
                .thumbnail(0.1f)
                .into(imageView);
    }

    public static void getBitmap1(Context context, String uri, ImageView imageView, final LoadImageCallback callback) {
        RequestOptions options = new RequestOptions();
        options.dontAnimate();
        options.dontTransform();
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        callback.callback(resource);
                        return true;
                    }

                }).into(imageView);
    }

    public static void getBitmap(Context context, String uri, final LoadImageCallback callback) {
        RequestOptions options = new RequestOptions();
        options.dontAnimate().dontTransform();
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        callback.callback(resource);

                    }
                });
    }

    public static void getDrawableBitmap(Context context, int res, final LoadImageCallback
            callback) {
        Glide.with(context)
                .asBitmap()
                .load(res)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        callback.callback(resource);

                    }
                });
    }

    public static void loadBitmapIntoTargetNoCache(Context context, String uri, final View view) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                .into(new SimpleTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        view.setBackground(new BitmapDrawable(resource));

                    }
                });
    }

    public static void loadDrawable(Context context, int res, final View view) {
        Glide.with(context)
                .asBitmap()
                .load(res)
                .into(new SimpleTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        view.setBackground(new BitmapDrawable(resource));
                    }
                });
    }

    public static void loadDrawable(Context context, int res, ImageView view) {
        Glide.with(context)
                .load(res)
                .into(view);
    }

    public static void loadFullyBitmap(Context ctx, String url, ImageView targetView, int resPlace,
                                       int resError) {

        RequestOptions options = new RequestOptions();
        if (resPlace > 0) {
            options.placeholder(resPlace);
        }
        if (resError > 0) {
            options.error(resError);
        }

        RequestBuilder<Drawable> requestBuilder = Glide.with(ctx).load(url).apply(options);
        requestBuilder.into(targetView);
    }

    public static void loadFullyBitmapFitCenter(Context ctx, String url, ImageView targetView, int resPlace, int
            resError) {
        RequestOptions options = new RequestOptions();

        if (resPlace > 0) {
            options.placeholder(resPlace);
        }
        if (resError > 0) {
            options.error(resError);
        }

        RequestBuilder<Drawable> request = Glide.with(ctx).load(url).apply(options.fitCenter());
        request.into(targetView);
    }

    public static void saveImageToGallery(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            return;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
    }

    public interface LoadImageCallback {
        void callback(Bitmap result);
    }

    public static File getSmallBitmap(File file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(file.getAbsolutePath());
        LogUtils.out("旋转" + degree);
        bm = rotateBitmapByDegree(bm, degree);
        saveBitmapFile(bm, file);
        return saveBitmapFile(bm, file);
//        return saveBitmap(bm, filePath, 70);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * 旋转图片
     *
     * @param
     * @param
     * @return
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static File saveBitmapFile(Bitmap bitmap, File tarFile) {
        if (bitmap == null || tarFile == null) {
            return null;
        }
        File parentFolder = tarFile.getParentFile();
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tarFile);
            stream.writeTo(fos);
            stream.flush();
            fos.flush();
        } catch (Exception e) {
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return tarFile;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param rotate
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * 添加图片到sd卡并规定压缩比例，100默认原图
     */
    public static File saveBitmap(Bitmap bitmap, String savePath, int quality) {
        if (bitmap == null)
            return null;
        try {
            File f = new File(savePath);
            if (f.exists()) f.delete();
            FileOutputStream fos = new FileOutputStream(f);
            f.createNewFile();
            // 把Bitmap对象解析成流
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
            bitmap.recycle();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            bitmap.recycle();
            return null;
        }
    }

    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    public static Bitmap getSampledBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(filePath, options);
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

}
