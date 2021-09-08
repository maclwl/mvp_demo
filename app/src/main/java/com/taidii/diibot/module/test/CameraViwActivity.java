package com.taidii.diibot.module.test;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.utils.BitmapUtils;
import com.taidii.diibot.utils.CameraThreadPool;
import com.taidii.diibot.utils.FileUtil;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Mode;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class CameraViwActivity extends BaseActivity {

    @BindView(R.id.camera)
    CameraView camera;
    @BindView(R.id.im_picture)
    ImageView imPicture;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera_view;
    }

    @Override
    protected void initView() {
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        camera.setLifecycleOwner(this);
    }

    @Override
    protected void init() {
        camera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                if (camera.isTakingVideo()) {
                    return;
                }
//                //通过jpeg获取图片的旋转角度orientation jpeg中包含了照片角度信息
//                int orientation = Exif.getOrientation(result.getData());
//                //通过jpeg获取bitmap对象
//                Bitmap bitmap = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length);
//                //将bitmap按解析出来的角度进行旋转orientation
//                //有些机型会将照片旋转90度
//                Matrix m = new Matrix();
//                m.postRotate(orientation);
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
//                imPicture.setImageBitmap(bitmap);
                handleCameraData(result.getData());
            }
        });
    }

    //动态权限申请
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isOpened()) {
            camera.open();
        }
    }

    //根据activity生命周期 将来启动或者销毁相机
    @Override
    protected void onResume() {
        super.onResume();
        if (!camera.isOpened()) {
            camera.open();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁相机释放资源
        camera.destroy();
    }

    @OnClick({R.id.photo, R.id.change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.photo:
                capturePicture();
                break;
            case R.id.change:
                toggleCamera();
                break;
        }
    }

    private void toggleCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                break;
            case FRONT:
                break;
        }
    }

    private void capturePicture() {
        if (camera.getMode() == Mode.VIDEO) {
            return;
        }
        if (camera.isTakingPicture()) return;
        camera.takePicture();
    }

    private void handleCameraData(final byte[] data) {
        CameraThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                FileUtil.createFileDir(Constant.IMAGE_CACHE_DATA);
                String fileName = "Diibot" + System.currentTimeMillis() + ".jpeg";
                String fileUri = Constant.IMAGE_CACHE_DATA + fileName;
                File file = new File(fileUri);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = fileUri;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String uri = (String) msg.obj;
                    BitmapUtils.loadFullyBitmap(act, new File(uri), imPicture, 130, 130);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

}
