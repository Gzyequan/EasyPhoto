LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)


OpenCV_INSTALL_MODULES := on
OpenCV_CAMERA_MODULES := off

OPENCV_LIB_TYPE :=STATIC

ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
include E:\developtool\opencv\OpenCV-android-sdk\sdk\native\jni\OpenCV.mk
else
include $(OPENCV_MK_PATH)
endif


LOCAL_MODULE := OpenCVProcess
LOCAL_SRC_FILES := OpenCVProcess.cpp

LOCAL_LDLIBS +=  -lm -llog

include $(BUILD_SHARED_LIBRARY)
