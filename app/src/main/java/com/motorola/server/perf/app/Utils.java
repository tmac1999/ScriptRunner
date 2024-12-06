package com.motorola.server.perf.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.Face;
import android.media.Image;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {
    interface LineCallback {
        void onLineRead(String line);
    }

    private static LineCallback mLineCallback;

    public static void runCMD(Context context) {

        String[] commands = {///  system_ext/snpe/snpe-sample
                "system_ext/snpe/snpe-sample",
                "-d", "/system_ext/snpe/emotion.dlc",
                "-i", "/system_ext/snpe/raw_list.txt",
                "-o", "/system_ext/snpe/output_sample"
        };
        try {
            mLineCallback = new LineCallback() {
                @Override
                public void onLineRead(String line) {
                    Log.d("xuhw7Demo", "onLineRead_showLines(line): " + line);
                    Log.d("xuhw7Demo", "showLines(line) end");
                }
            };

            Process process = Runtime.getRuntime().exec(commands);
            final Process proc = process;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                String line = reader.readLine();
                mLineCallback.onLineRead(line);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("xuhw7Demo", "Error reading from snpe-sample process!");
            }
            int exitCode = proc.waitFor();
            Log.d("xuhw7Demo", "snpe-sample process finished, exitCode = " + exitCode);
            if (exitCode != 0) {
                Log.d("xuhw7Demo", "snpe-sample failed!");
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        Log.d("xuhw7Demo", "snpe-sample error: " + errorLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("xuhw7Demo", "Error reading snpe-sample error stream!");
                }
                //            if (process != null){
                //                Process.killProcess(process.pid());
                //            }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runCMDFirst(Context context) {
        String[] commandsFirst = {///  system_ext/snpe/snpe-sample
                "export", " LD_LIBRARY_PATH=/system_ext/snpe/aarch64-android-clang8.0/lib"
        };

        try {
            mLineCallback = new LineCallback() {
                @Override
                public void onLineRead(String line) {
                    Log.d("xuhw7Demo", "onLineRead_showLines(line): " + line);

                    Log.d("xuhw7Demo", "showLines(line) end");
                }
            };

            Process process = Runtime.getRuntime().exec(commandsFirst);
            final Process proc = process;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                String line = reader.readLine();
                mLineCallback.onLineRead(line);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("xuhw7Demo", "Error reading from snpe-sample process!");
            }
            int exitCode = proc.waitFor();
            Log.d("xuhw7Demo", "snpe-sample process finished, exitCode = " + exitCode);
            if (exitCode != 0) {
                Log.d("xuhw7Demo", "snpe-sample failed!");
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        Log.d("xuhw7Demo", "snpe-sample error: " + errorLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("xuhw7Demo", "Error reading snpe-sample error stream!");
                }
                //            if (process != null){
                //                Process.killProcess(process.pid());
                //            }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static void saveImageToStorage(Context context, Bitmap bitmap) {
        Log.d(TAG, "xuhw7_saveImageToStorage");

        Matrix matrix = new Matrix();
        matrix.preRotate(270);
        //matrix.postScale(0.7f, 0.7f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        Log.d(TAG, "xuhw7_Bitmap.Config.ARGB_8888_begin");
        bitmap.setConfig(Bitmap.Config.ARGB_8888);
//        Log.d(TAG, "xuhw7_Bitmap.Config.ARGB_8888_end");
        float PIXEL_MAX = 255.0f;
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 遍历像素数组并使用位操作提取颜色通道
//        Log.d(TAG, "xuhw7_new file Input.raw_begin");
        File rgbFile = new File(context.getCacheDir(), "face_" + System.currentTimeMillis() + "_.raw");
//        Log.d(TAG, "xuhw7_new file Input.raw_end");
        try {
//            Log.d(TAG, "xuhw7_FileOutputStream(rgbFile)_begin");
            FileOutputStream fos = new FileOutputStream(rgbFile);
//            Log.d(TAG, "xuhw7_FileOutputStream(rgbFile)_end");
            for (int i = 0; i < pixels.length; i++) {
                int pixel = pixels[i];
                int alpha = (pixel >>> 24); // 无符号右移24位
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;
                float[] f_pixels = new float[]{
                        red / PIXEL_MAX,
                        green / PIXEL_MAX,
                        blue / PIXEL_MAX
                };
                ByteBuffer buffer = ByteBuffer.allocate(12);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                for (float f : f_pixels) {
                    buffer.putFloat(f);
                    //Log.d(TAG, "xuhw7_buffer_f = " + f);
                }
                fos.write(buffer.array());
                //Log.d(TAG, "xuhw7_fos.write_end" );
            }
            fos.close();
            Log.d(TAG, "xuhw7_Successful SaveF_pixels.");
            runCMD(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //save the image to sSensorSystemPersistDirPath
//        Log.d(TAG, "xuhw7_newPictureFile_BEGIN, path = sSensorSystemPersistDirPath = /mnt/product/persist/sensors");
        if (bitmap != null) {
            //File pictureFile = new File(sSensorSystemPersistDirPath, getPhotoName());
            File pictureFile = new File(context.getCacheDir(), "face_" + System.currentTimeMillis() + "_.jpg");
//            Log.d(TAG, "xuhw7_newPictureFile_END");
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                bitmap.recycle();
                fos.close();
                Log.d(TAG, "xuhw7_Successful SaveImageToStorage.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final String TAG = "CameraActivity";

    public static void save(Context context, Image latestImage, Face[] faces) {

        if (latestImage != null) {
            ByteBuffer buffer = latestImage.getPlanes()[0].getBuffer();
            if (buffer != null) {
                //convert the buffer to a byte array and save to file
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                Bitmap fullImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                for (Face face : faces) {
                    int left = face.getBounds().left;
                    int top = face.getBounds().top;
                    int right = left + face.getBounds().width();
                    int bottom = top + face.getBounds().height();

                    // 裁剪面部区域
                    Log.i(TAG, "fullImageBitmap width:" + fullImageBitmap.getWidth() + " height:" + fullImageBitmap.getHeight() + ",right：" + right + ",bottom：" + bottom);
                    //fullImageBitmap 缩了 2.1倍   4031 -> 1920
                    Float l = left / 2.1f;
                    Float t = top / 2.1f;
                    Float w = (right - left) / 2.1f;
                    Float h = (bottom - top) / 2.1f;
                    int i = l.intValue() + h.intValue();
                    Log.i(TAG, "y + height:" + i + " bitmap.height():" + fullImageBitmap.getHeight());
                    Bitmap faceBitmap = Bitmap.createBitmap(fullImageBitmap, l.intValue(), t.intValue(), w.intValue(), h.intValue());
                    saveImageToStorage(context, faceBitmap);
                    // 现在你可以使用 faceBitmap，例如保存到文件
                    // 注意：在实际应用中，你需要处理 Bitmap 的释放，以避免内存泄漏
                    // faceBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                }
            }

        }
    }

    public static int getFaceDetectMode() {
        // 这里返回简单面部检测模式
        return CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL;
    }
}
