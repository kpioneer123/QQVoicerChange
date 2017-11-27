LOCAL_PATH := $(call my-dir)


include $(CLEAR_VARS)
LOCAL_MODULE := fmod
LOCAL_SRC_FILES := $(LOCAL_PATH)/lib/$(TARGET_ARCH_ABI)/libfmod.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := fmodL
LOCAL_SRC_FILES := $(LOCAL_PATH)/lib/$(TARGET_ARCH_ABI)/libfmodL.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)


LOCAL_MODULE    := qq_voicer
#LOCAL_SRC_FILES := play_sound.cpp common.cpp common_platform.cpp  #音效果一 用MainActivity
#LOCAL_SRC_FILES := effects.cpp common.cpp common_platform.cpp   #音效果二 用MainActivity
LOCAL_SRC_FILES :=effects_qq_voicer.cpp    #qq变声用 QQVoiceActivity
LOCAL_SHARED_LIBRARIES := fmod fmodL
LOCAL_LDLIBS := -llog
LOCAL_CPP_FEATURES := exceptions  #支持异常处理
include $(BUILD_SHARED_LIBRARY)


