package com.motorola.internal.app;


interface IMotoOCRResultListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onResult(boolean isBulletCommentOn);
    void onOCRSuccess(String result);
}