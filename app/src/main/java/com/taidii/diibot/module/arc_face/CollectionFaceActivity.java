package com.taidii.diibot.module.arc_face;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Mode;
import com.taidii.diibot.R;
import com.taidii.diibot.app.Constant;
import com.taidii.diibot.base.BaseActivity;
import com.taidii.diibot.entity.face.FaceDetailInfo;
import com.taidii.diibot.utils.CameraThreadPool;
import com.taidii.diibot.utils.DataHolder;
import com.taidii.diibot.utils.Exif;
import com.taidii.diibot.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionFaceActivity extends BaseActivity {

    @BindView(R.id.camera)
    CameraView camera;

    /**
     *录入对应人脸相关
     */
    private int id;
    private String name;
    private String type;
    /*传值到详情界面数据*/
    private FaceDetailInfo faceDetailInfo = new FaceDetailInfo();
    public static CollectionFaceActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        instance = this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection_face;
    }

    @Override
    protected void initView() {
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        camera.setLifecycleOwner(this);
        camera.toggleFacing();
    }

    @Override
    protected void init() {
        /*跳转值*/
        id = getIntent().getIntExtra(Constant.ID,0);
        name = getIntent().getStringExtra(Constant.NAME);
        type = getIntent().getStringExtra(Constant.TYPE);
        faceDetailInfo.setId(id);
        faceDetailInfo.setName(name);

        /*摄像头*/
        camera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                if (camera.isTakingVideo()) {
                    return;
                }
                //通过jpeg获取图片的旋转角度orientation jpeg中包含了照片角度信息
                int orientation = Exif.getOrientation(result.getData());
                //通过jpeg获取bitmap对象
                Bitmap bitmap = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length);
                //将bitmap按解析出来的角度进行旋转orientation
                //有些机型会将照片旋转90度
                Matrix m = new Matrix();
                m.postRotate(orientation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                /*转换bgr24*/
                Bitmap bt = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
                // 为图像数据分配内存
                byte[] bgr24 = ArcSoftImageUtil.createImageData(bt.getWidth(), bt.getHeight(), ArcSoftImageFormat.BGR24);
                // 图像格式转换
                int transformCode = ArcSoftImageUtil.bitmapToImageData(bt, bgr24, ArcSoftImageFormat.BGR24);
                if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                    return;
                }
                handleCameraData(result.getData(),bgr24,bt.getWidth(), bt.getHeight());
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

    private void handleCameraData(final byte[] data,byte[] bgr24,int width, int height) {
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
                    faceDetailInfo.setUri(fileUri);
                    faceDetailInfo.setBgr24(bgr24);
                    faceDetailInfo.setWidth(width);
                    faceDetailInfo.setHeight(height);
                    message.obj = faceDetailInfo;
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
                    FaceDetailInfo faceDetailInfo = (FaceDetailInfo) msg.obj;
                    DataHolder.getInstance().setData(Constant.DATA,faceDetailInfo);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TYPE,type);
                    openActivity(FaceRegisterDetailActivity.class,bundle);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @OnClick({R.id.tv_cancel, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.btn_register:
                capturePicture();
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

}
