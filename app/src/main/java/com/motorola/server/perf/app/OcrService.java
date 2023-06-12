//package com.motorola.server.perf.app;
//
//
//import android.annotation.SuppressLint;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.hardware.input.InputManager;
//import android.os.IBinder;
//
//import com.example.tf_ocr.google.TextRecognitionProcessor;
//import com.google.mlkit.common.MlKit;
//import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;
//import com.motorola.internal.app.IMotoOCRResultListener;
//import com.motorola.maxeai.utils.LogUtils;
//
///**
// * 监控什么时候可以抢票？
// */
//public class OcrService extends Service {
//    private static final int OCR_CAPTURE_SPLASH = 1;
//    private static final int OCR_BULLET_COMMENTS = 2;
//    // Binder given to clients
//    private final IBinder mBinder = new LocalBinder();
//
//    IMotoOCRResultListener mListener = null;
//
//    /**
//     * Class used for the client Binder.  Because we know this service always
//     * runs in the same process as its clients, we don't need to deal with IPC.
//     */
//    public class LocalBinder extends com.motorola.internal.app.IMotoOCRService.Stub {
//
//        public void ocrCaptureSplash(Bitmap bitmap) {
//            mImageProcessor.processBitmap(bitmap, result -> BenchmarkUtil.benchmark(result, mListener));
//        }
//
//
//        @Override
//        public void processBitmap(Bitmap bitmap, int type) {
//            if (type == OCR_CAPTURE_SPLASH) {
//                ocrCaptureSplash(bitmap);
//            }
//        }
//
//        @Override
//        public void registerListener(IMotoOCRResultListener listener) {
//            mListener = listener;
//        }
//
//        @Override
//        public void unRegisterListener(IMotoOCRResultListener listener) {
//            mListener = null;
//            InputManager systemService = (InputManager) getSystemService(Context.INPUT_SERVICE);
//        }
//    }
//
//    public OcrService() {
//        LogUtils.i(TAG, "OcrService==========" + getApplication());
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        LogUtils.i("onBind===", "onBind==motorola");
//        return mBinder;
//    }
//
//    public static final String TAG = "OCRService";
//    TextRecognitionProcessor mImageProcessor;
//
//    @SuppressLint("SimpleDateFormat")
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        LogUtils.i(TAG, "onCreate");
//        MlKit.initialize(getApplicationContext());
//        mImageProcessor =
//                new TextRecognitionProcessor(
//                        getApplicationContext(), new ChineseTextRecognizerOptions.Builder().build());
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        LogUtils.i(TAG, "onStartCommand==TextRecognitionProcessor");
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        LogUtils.i(TAG, "onDestroy");
//    }
//}
//
