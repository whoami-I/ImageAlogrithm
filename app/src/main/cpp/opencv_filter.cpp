#include "opencv2/opencv.hpp"
#include "filters.h"

extern "C"
void JNIFUNCF(AlgManager, nativeGrayOpencv, jobject bitmap,
              jint width, jint height) {
    void *destination = 0;
//    AndroidBitmap_lockPixels(env, bitmap, &destination);
    int ret=0;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &destination)) < 0) {
        LOG("First Bitmap LockPixels Failed return=%d!", ret);
        return;
    }
    cv::Mat src(height, width, CV_8UC4, destination);
    LOG("---------nativeGrayOpencv");
    //将图像转为灰度图
    cv::Mat grayImage;
    cvtColor(src, grayImage, cv::COLOR_BGRA2GRAY);
    cvtColor(grayImage, src, cv::COLOR_GRAY2BGRA);
//    for (int i = 0; i < height; ++i) {
//        for (int j = 0; j < width; ++j) {
//            src.at<int>(i, j) = 0;
//        }
//    }
//    uchar* ptr = src.ptr(0);
//    for(int i = 0; i < width*height; i ++){
//        int grayScale = (int)(ptr[4*i+2]*0.299 + ptr[4*i+1]*0.587 + ptr[4*i+0]*0.114);
//        ptr[4*i+1] = grayScale;
//        ptr[4*i+2] = grayScale;
//        ptr[4*i+0] = grayScale;
//    }
    AndroidBitmap_unlockPixels(env, bitmap);
}

