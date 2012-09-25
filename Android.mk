LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4
LOCAL_PACKAGE_NAME := MPTCPControl
LOCAL_SRC_FILES := $(call all-java-files-under,src)

include $(BUILD_PACKAGE)

##

