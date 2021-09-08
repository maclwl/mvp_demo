package com.taidii.diibot.utils;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.taidii.diibot.BuildConfig;
import com.taidii.diibot.app.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;

public class FileUtil {
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值

    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值

    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值

    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    public static String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}, {"", "*/*"}};

    /**
     * 创建File     *
     * @param dirPath
     * @return
     */
    public static File createFile(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取文件指定文件的指定单位的大小
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */

    public static double getFileOrFilesSize(String filePath, int sizeType) {

        File file = new File(filePath);

        long blockSize = 0;

        try {

            if (file.isDirectory()) {

                blockSize = getFileSizes(file);

            } else {

                blockSize = getFileSize(file);

            }

        } catch (Exception e) {

            e.printStackTrace();
            LogUtils.d("获取失败!");

        }

        return formatFileSize(blockSize, sizeType);

    }

    /**
     * 获取指定文件大小
     * @param
     * @return
     * @throws Exception
     */

    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        } else {
            file.createNewFile();
        }
        return size;

    }

    /**
     * 获取指定文件夹
     * @param f
     * @return
     * @throws Exception
     */

    private static long getFileSizes(File f) throws Exception {

        long size = 0;

        File flist[] = f.listFiles();

        for (int i = 0; i < flist.length; i++) {

            if (flist[i].isDirectory()) {

                size = size + getFileSizes(flist[i]);

            } else {

                size = size + getFileSize(flist[i]);

            }

        }

        return size;

    }

    private static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 根据文件路径获取文件名(接收文件)
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return new File(filePath).getName();
    }

    /**
     * 根据文件路径获取文件扩展名
     * @param filePath
     * @return
     */
    public static String getFileType(String filePath) {
        int dot = filePath.lastIndexOf('.');
        if ((dot > -1) && (dot < (filePath.length() - 1))) {
            return filePath.substring(dot + 1).toLowerCase();
        } else {
            return filePath;
        }
    }

    /**
     * 判断是否是除图片以外的文件
     * @param filePath 文件地址
     * @return
     * @author 斌华
     */
    public static boolean isFileButNotImage(String filePath) {
        String[] types = {"doc", "docx", "xls", "xlsx", "txt", "ppt", "pptx", "zip", "rar", "pdf"};
        List<String> typeList = Arrays.asList(types);
        String fileType = getFileType(filePath);
        return typeList.contains(fileType);
    }

    /**
     * 判断是否是视频
     * @param filePath 文件地址
     * @return
     * @author 斌华
     */
    public static boolean isVideo(String filePath) {
        String[] types = {"3gp", "mp4"};
        List<String> typeList = Arrays.asList(types);
        String fileType = getFileType(filePath);
        return typeList.contains(fileType);
    }

    /**
     * <code>openFile</code>
     * @param context
     * @param file
     * @description: 自动选择第三方软件打开附件
     */
    public static void openFile(Context context, File file) throws ActivityNotFoundException {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        // 获取文件file的MIME类型
        String type = getMIMEType(file);
        // 设置intent的data和Type属性。
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID+".avatar.provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType( uri, type);
        // 跳转
        context.startActivity(intent);
        // Intent.createChooser(intent, "请选择对应的软件打开该附件！");
    }

    public static void createFileDir(String path) {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "")
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {

            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }


    public static final void deleteAsync(final File file) {
        if (file == null || !file.exists()) {
            return;
        }
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                delete(file);
            }
        });
    }

    public static final void deleteAsync(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        deleteAsync(new File(path));
    }

    public static final void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
        } else {
            file.delete();
        }
    }

    public static final void delete(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        delete(new File(path));
    }

    public static final String getFileDownloadUrl(String fullUrl) {
        if (TextUtils.isEmpty(fullUrl) || !fullUrl.contains("/")) {
            return null;
        }
        String[] split = fullUrl.split("/");
        for (int i = split.length - 1; i >= 0; i--) {
            String temp = split[i];
            if (temp.isEmpty()) {
                return null;
            }
            if (temp.contains(".")) {
                char[] chars = temp.toCharArray();
                boolean allCharDigit = true;
                for (char aChar : chars) {
                    if (aChar == '.') {
                        continue;
                    }
                    if (!Character.isDigit(aChar)) {
                        allCharDigit = false;
                        break;
                    }
                }
                if (!allCharDigit) {
                    int indexOf = temp.lastIndexOf(".");
                    if (indexOf > 0 && indexOf < temp.length() - 2) {
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j <= i; j++) {
                            sb.append(split[j]);
                            if (j < i) {
                                sb.append("/");
                            }
                        }
                        return sb.toString();
                    }
                }
            }
        }
        return null;
    }

    public static final String[] getFileNameAndSuffix(String fileName) {
        if (TextUtils.isEmpty(fileName) || !fileName.contains("/")) {
            return null;
        }
        String[] split = fileName.split("/");
        for (int i = split.length - 1; i >= 0; i--) {
            String temp = split[i];
            if (temp.isEmpty()) {
                return null;
            }
            if (temp.contains(".")) {
                char[] chars = temp.toCharArray();
                boolean allCharDigit = true;
                for (char aChar : chars) {
                    if (aChar == '.') {
                        continue;
                    }
                    if (!Character.isDigit(aChar)) {
                        allCharDigit = false;
                        break;
                    }
                }
                if (!allCharDigit) {
                    int indexOf = temp.lastIndexOf(".");
                    if (indexOf > 0 && indexOf < temp.length() - 2) {
                        String name = temp.substring(0, indexOf);
                        String suffix = temp.substring(indexOf + 1);
                        int tempIndex = suffix.indexOf("?");
                        if (tempIndex > 0) {
                            suffix = suffix.substring(0, tempIndex);
                        }
                        return new String[]{name, suffix};
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public static Uri getImageContentUri(File imageFile, Context context) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    public static String saveBitmap(String fileDate, Bitmap bm) {
        String root = getExternalStorageDirectory().toString();
        File dir = new File(Constant.IMAGE_PATH);
        if (!dir.exists()) dir.mkdirs();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dir, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    public static void writeFile(Context context, String str){
        try {
            OutputStream os = getLogStream(context);
            os.write(str.getBytes("utf-8"));
            os.flush();
            os.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static OutputStream getLogStream(Context context) throws IOException {
        //crash_log_pkgname.log
        String model = Build.MODEL.replace(" ", "_");
        String fileName = String.format("compress_"+ model + ".log", context.getPackageName());
        File file  = new File(Environment.getExternalStorageDirectory(), fileName);

        if(!file.exists()){
            file.createNewFile();
        }

        return new FileOutputStream(file, true);
    }

    public static Bitmap decodeUri(Context context, Uri uri, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //只读取图片尺寸
        resolveUri(context, uri, options);

        //计算实际缩放比例
        int scale = 1;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if ((options.outWidth / scale > maxWidth &&
                    options.outWidth / scale > maxWidth * 1.4) ||
                    (options.outHeight / scale > maxHeight &&
                            options.outHeight / scale > maxHeight * 1.4)) {
                scale++;
            } else {
                break;
            }
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;//读取图片内容
        options.inPreferredConfig = Bitmap.Config.RGB_565; //根据情况进行修改
        Bitmap bitmap = null;
        try {
            bitmap = resolveUriForBitmap(context, uri, options);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // http://blog.sina.com.cn/s/blog_5de73d0b0100zfm8.html
    private static void resolveUri(Context context, Uri uri, BitmapFactory.Options options) {
        if (uri == null) {
            return;
        }

        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
                ContentResolver.SCHEME_FILE.equals(scheme)) {
            InputStream stream = null;
            try {
                stream = context.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(stream, null, options);
            } catch (Exception e) {
                Log.w("resolveUri", "Unable to open content: " + uri, e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.w("resolveUri", "Unable to close content: " + uri, e);
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            Log.w("resolveUri", "Unable to close content: " + uri);
        } else {
            Log.w("resolveUri", "Unable to close content: " + uri);
        }
    }

    // http://blog.sina.com.cn/s/blog_5de73d0b0100zfm8.html
    private static Bitmap resolveUriForBitmap(Context context, Uri uri, BitmapFactory.Options options) {
        if (uri == null) {
            return null;
        }

        Bitmap bitmap = null;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
                ContentResolver.SCHEME_FILE.equals(scheme)) {
            InputStream stream = null;
            try {
                stream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(stream, null, options);
            } catch (Exception e) {
                Log.w("resolveUriForBitmap", "Unable to open content: " + uri, e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.w("resolveUriForBitmap", "Unable to close content: " + uri, e);
                    }
                }
            }
        } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
        } else {
            Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
        }

        return bitmap;
    }
}