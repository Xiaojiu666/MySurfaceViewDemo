package com.xinruan.libs.camera.Camera2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Camera2Manager extends CameraLoader {

    private static final String TAG = "Camera2manager";
    private TextureView mTextureView;
    private Handler mCameraHandler;
    private HandlerThread handlerThread = new HandlerThread("CameraThread");
    private Context mContext;
    private CameraCharacteristics mCameraCharacteristics;
    private String mCameraId;
    private int mCameraFacing = CameraCharacteristics.LENS_FACING_BACK;    //默认使用后置摄像头
    private int mCameraSensorOrientation;
    private CameraDevice mCameraDevice;
    private static final int PREVIEW_WIDTH = 640;  //预览的宽度
    private static final int PREVIEW_HEIGHT = 480;      //预览的高度
    private static final int SAVE_WIDTH = 720;         //保存图片的宽度
    private static final int SAVE_HEIGHT = 1280;        //保存图片的高度
    private Size mPreviewSize = new Size(PREVIEW_WIDTH, PREVIEW_HEIGHT);//预览大小
    private Size mSavePicSize = new Size(SAVE_WIDTH, SAVE_HEIGHT);       //保存图片大小
    private ImageReader mImageReader;
    private CameraManager mCameraManager;
    private CameraCaptureSession mCameraCaptureSession;

    public Camera2Manager(Activity mContext, TextureView mTextureView) {
        this.mContext = mContext;
        this.mTextureView = mTextureView;
        checkCameraPerssion(mContext);
        initSystemServer();
        handlerThread.start();
        mCameraHandler = new Handler(handlerThread.getLooper());
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Log.e(TAG, "onSurfaceTextureAvailable");
                openCamera(1920, 1080);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Log.e(TAG, "onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                Log.e(TAG, "onSurfaceTextureDestroyed");
                releaseCamera();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                Log.e(TAG, "onSurfaceTextureUpdated");
            }
        });
    }

    private WindowManager windowManager;
    private int mDisplayRotation;  //手机方向


    void releaseCamera() {
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    private void initCameraInfo() {
        String[] cameraIdList = null;
        try {
            cameraIdList = mCameraManager.getCameraIdList();
            if (cameraIdList.length == 0) {
                return;
            }
            for (String cameraId :
                    cameraIdList) {
                CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                int facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);

                if (facing == mCameraFacing) {
                    mCameraId = cameraId;
                    mCameraCharacteristics = cameraCharacteristics;
                }
                Log.e("设备中的摄像头 $mCameraId", mCameraId);
            }

            int supportLevel = mCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            if (supportLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                Log.e(TAG, "相机硬件不支持新特性");
            }
            //获取摄像头方向
            mCameraSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
            StreamConfigurationMap configurationMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            Size[] savePicSize = configurationMap.getOutputSizes(ImageFormat.JPEG);        //保存照片尺寸
            Size[] previewSize = configurationMap.getOutputSizes(SurfaceTexture.class); //预览尺寸
            boolean exchange = exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation);

            mSavePicSize = getBestSize(
                    mSavePicSize.getHeight(),
                    mSavePicSize.getWidth(),
                    mSavePicSize.getHeight(),
                    mSavePicSize.getWidth(),
                    Arrays.asList(savePicSize));
//            mSavePicSize = getBestSize(
//            if (exchange) mSavePicSize.height
//            else mSavePicSize.width,
//            if (exchange) mSavePicSize.width
//            else mSavePicSize.height,
//            if (exchange) mSavePicSize.height
//            else mSavePicSize.width,
//            if (exchange) mSavePicSize.width
//            else mSavePicSize.height,
//                    savePicSize.toList())

            mPreviewSize = getBestSize(
                    mPreviewSize.getHeight(),
                    mPreviewSize.getWidth(),
                    mTextureView.getHeight(),
                    mTextureView.getWidth(),
                    Arrays.asList(savePicSize));
            mTextureView.getSurfaceTexture().setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            Log.e(TAG, "预览最优尺寸 ：${mPreviewSize.width} * ${mPreviewSize.height}, 比例  ${mPreviewSize.width.toFloat() / mPreviewSize.height}");
            Log.e(TAG, "保存图片最优尺寸 ：${mSavePicSize.width} * ${mSavePicSize.height}, 比例  ${mSavePicSize.width.toFloat() / mSavePicSize.height}");
            //根据预览的尺寸大小调整TextureView的大小，保证画面不被拉伸
            int orientation = mContext.getResources().getConfiguration().orientation;
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
//                mTextureView.setAspectRatio(mPreviewSize.width, mPreviewSize.height);
//            else
//                mTextureView.setAspectRatio(mPreviewSize.height, mPreviewSize.width)

            mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.JPEG, 1);
            mImageReader.setOnImageAvailableListener(onImageAvailableListener, mCameraHandler);

//            if (openFaceDetect)
//                initFaceDetect()

//            openCamera();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initSystemServer() {
        /**CAMER2 通过相机服务，拿到相机管理器*/
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * CAMER2 相机连接状态回调
     */
    private CameraDevice.StateCallback mStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e(TAG, "onOpened");
            mCameraDevice = camera;
            createCaptureSession(camera);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            releaseCamera();
            Log.e(TAG, "onDisconnected");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            releaseCamera();
            Log.e(TAG, "onError");
        }
    };

    private int viewWidth = 0;
    private int viewHeight = 0;

    @Override
    public void openCamera(int width, int height) {
        viewWidth = width;
        viewHeight = height;
        setUpCamera();
    }

    private void setUpCamera() {
        getCameraId();
        try {
            mCameraManager.openCamera(mCameraId, mStateCallBack, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cloaseCamera() {

    }

    @Override
    public void switchCamera(int Id) {

    }

    @Override
    public int getCameraOrientation() {
        mDisplayRotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (mDisplayRotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (TextUtils.isEmpty(mCameraId)) {
            return 0;
        }
        CameraCharacteristics characteristics = null;
        try {
            characteristics = mCameraManager.getCameraCharacteristics(mCameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        int orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        return mCameraFacing == CameraCharacteristics.LENS_FACING_FRONT ? (orientation + degrees) % 360 : (orientation - degrees) % 360;
    }


    /**
     * 创建预览会话
     */
    private void createCaptureSession(CameraDevice cameraDevice) {
        Size size = chooseOptimalSize();
        mImageReader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(onImageAvailableListener, mCameraHandler);

        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface = new Surface(mTextureView.getSurfaceTexture());
            mTextureView.getSurfaceTexture().setDefaultBufferSize(1600, 1200);
            captureRequestBuilder.addTarget(surface); // 将CaptureRequest的构建器与Surface对象绑定在一起
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);    // 闪光灯
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE); // 自动对焦
            ArrayList<Surface> surfaces = new ArrayList<>();
            surfaces.add(surface);
            surfaces.add(mImageReader.getSurface());
            try {
                cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        mCameraCaptureSession = session;
                        try {
                            session.setRepeatingRequest(captureRequestBuilder.build(), mCaptureCallBack, mCameraHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        Log.e(TAG, "开启预览会话失败");
                    }
                }, mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size chooseOptimalSize() {
        if (viewWidth == 0 || viewHeight == 0) {
            return new Size(0, 0);
        }
        Size[] outputSizes = new Size[0];
        try {
            outputSizes = mCameraManager.getCameraCharacteristics(mCameraId)
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.JPEG);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        int orientation = getCameraOrientation();
        int maxPreviewWidth = (orientation == 90 || orientation == 270) ? viewHeight : viewWidth;
        int maxPreviewHeight = (orientation == 90 || orientation == 270) ? viewWidth : viewHeight;
        for (Size camearSupporSize :
                outputSizes) {
            Log.e(TAG, "cameraSupSize" + camearSupporSize.toString());
            if (camearSupporSize.getWidth() < maxPreviewWidth / 2 && camearSupporSize.getHeight() < maxPreviewHeight / 2) {
                return camearSupporSize;
            }
        }
        return new Size(PREVIEW_WIDTH, PREVIEW_HEIGHT);
    }

    private void getCameraId() {
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();
            for (String cameraId :
                    cameraIdList) {
                CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                int facingId = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (facingId == mCameraFacing) {
                    mCameraId = cameraId;
                    mCameraCharacteristics = cameraCharacteristics;
                    Log.e("设备中的摄像头 $mCameraId", mCameraId);
                }

            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.CaptureCallback mCaptureCallBack = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            Log.e(TAG, "开启预览会话失败");
        }
    };

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
//            Image image = reader.acquireNextImage();
//            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
//            byte[] byteArray = byteBuffer.array();
//            byteBuffer.get(byteArray);
//            reader.close();
//            BitmapUtils.savePic(byteArray, mCameraSensorOrientation == 270, {savedPath, time ->
//                    mActivity.runOnUiThread{
//                    mActivity.toast("图片保存成功！ 保存路径：$savedPath 耗时：$time")
//            }
//            }, {msg ->
//                    mActivity.runOnUiThread{
//                    mActivity.toast("图片保存失败！ $msg")
//            }
//            });
        }
    };


    /**
     * 根据提供的屏幕方向 [displayRotation] 和相机方向 [sensorOrientation] 返回是否需要交换宽高
     */
    private boolean exchangeWidthAndHeight(int displayRotation, int sensorOrientation) {
        boolean exchange = false;
        if (displayRotation == Surface.ROTATION_0 || displayRotation == Surface.ROTATION_180) {
            exchange = true;
        } else {
            exchange = true;
        }

        Log.e(TAG, "屏幕方向  $displayRotation");
        Log.e(TAG, "相机方向  $sensorOrientation");
        return exchange;
    }

    /**
     * 根据提供的参数值返回与指定宽高相等或最接近的尺寸
     *
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @param maxWidth     最大宽度(即TextureView的宽度)
     * @param maxHeight    最大高度(即TextureView的高度)
     * @param sizeList     支持的Size列表
     * @return 返回与指定宽高相等或最接近的尺寸
     */
    private Size getBestSize(int targetWidth, int targetHeight, int maxWidth, int maxHeight, List<Size> sizeList) {
        ArrayList<Size> bigEnough = new ArrayList<>(); //比指定宽高大的Size列表
        ArrayList<Size> notBigEnough = new ArrayList<Size>();  //比指定宽高小的Size列表

        for (Size size :
                sizeList) {
            //宽<=最大宽度  &&  高<=最大高度  &&  宽高比 == 目标值宽高比
            if (size.getWidth() <= maxWidth && size.getHeight() <= maxHeight
                    && size.getWidth() == size.getHeight() * targetWidth / targetHeight) {
                if (size.getWidth() >= targetWidth && size.getHeight() >= targetHeight)
                    bigEnough.add(size);
                else
                    notBigEnough.add(size);
            }
            Log.e(TAG, "系统支持的尺寸: ${size.width} * ${size.height} ,  比例 ：${size.width.toFloat() / size.height}");
        }

        Log.e(TAG, "最大尺寸 ：$maxWidth * $maxHeight, 比例 ：${targetWidth.toFloat() / targetHeight}");
        Log.e(TAG, "目标尺寸 ：$targetWidth * $targetHeight, 比例 ：${targetWidth.toFloat() / targetHeight}");

        //选择bigEnough中最小的值  或 notBigEnough中最大的值

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizeByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizeByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return sizeList.get(0);
        }
    }

    class CompareSizeByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }
}
