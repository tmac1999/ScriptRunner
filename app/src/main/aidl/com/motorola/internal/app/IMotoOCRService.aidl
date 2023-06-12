package com.motorola.internal.app;

import com.motorola.internal.app.IMotoOCRResultListener;

interface IMotoOCRService {

  oneway void processBitmap(in Bitmap bitmap,int type);
  oneway void registerListener(IMotoOCRResultListener listener);
  oneway void unRegisterListener(IMotoOCRResultListener listener);
}