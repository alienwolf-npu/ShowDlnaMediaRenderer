LOCAL_PATH := $(call my-dir)

PLT_ROOT := $(LOCAL_PATH)/../platinum
PLT_SRC_ROOT := $(PLT_ROOT)/Source

ifeq ($(NDK_DEBUG),1)
PLT_PREBUILT_PATH := $(PLT_ROOT)/Build/Targets/arm-android-linux/Debug
else
PLT_PREBUILT_PATH := $(PLT_ROOT)/Build/Targets/arm-android-linux/Release
endif

include $(CLEAR_VARS)
LOCAL_MODULE := Platinum
LOCAL_SRC_FILES := $(PLT_PREBUILT_PATH)/libPlatinum.a
LOCAL_EXPORT_C_INCLUDES += $(PLT_SRC_ROOT)/Platinum
LOCAL_EXPORT_C_INCLUDES += $(PLT_SRC_ROOT)/Core
LOCAL_EXPORT_C_INCLUDES += $(PLT_SRC_ROOT)/Devices/MediaConnect
LOCAL_EXPORT_C_INCLUDES += $(PLT_SRC_ROOT)/Devices/MediaServer
LOCAL_EXPORT_C_INCLUDES += $(PLT_SRC_ROOT)/Devices/MediaRenderer
LOCAL_EXPORT_C_INCLUDES += $(PLT_SRC_ROOT)/Extras
#LOCAL_C_INCLUDES += $(PLT_ROOT)/../Neptune/Source/Core
LOCAL_C_INCLUDES += $(PLT_ROOT)/ThirdParty/Neptune/Source/Core
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := Neptune
LOCAL_SRC_FILES := $(PLT_PREBUILT_PATH)/libNeptune.a
#LOCAL_EXPORT_C_INCLUDES += $(PLT_ROOT)/../Neptune/Source/Core
LOCAL_EXPORT_C_INCLUDES += $(PLT_ROOT)/ThirdParty/Neptune/Source/Core
include $(PREBUILT_STATIC_LIBRARY)

ifneq ($(NPT_CONFIG_NO_SSL),1)
include $(CLEAR_VARS)
LOCAL_MODULE := axTLS
LOCAL_SRC_FILES := $(PLT_PREBUILT_PATH)/libaxTLS.a
include $(PREBUILT_STATIC_LIBRARY)
endif

include $(CLEAR_VARS)
LOCAL_MODULE := pltMediaServer
LOCAL_SRC_FILES := $(PLT_PREBUILT_PATH)/libPltMediaServer.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := pltMediaRenderer
LOCAL_SRC_FILES := $(PLT_PREBUILT_PATH)/libPltMediaRenderer.a
LOCAL_C_INCLUDES += $(PLT_SRC_ROOT)/Devices/MediaRenderer
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE     := platinum-jni
LOCAL_SRC_FILES  := platinum-jni.cpp
LOCAL_SRC_FILES  += DuMediaRendererDelegate.cpp
LOCAL_SRC_FILES  += j4a/class/android/os/Build.cpp
LOCAL_SRC_FILES  += j4a/class/com/show/dlnadmr/jni/PlatinumReflection.cpp
LOCAL_SRC_FILES  += j4a/j4a_allclasses.cpp
LOCAL_SRC_FILES  += j4a/j4a_base.cpp
LOCAL_LDLIBS     += -llog
LOCAL_LDLIBS     += -landroid

LOCAL_CFLAGS += -DNPT_CONFIG_ENABLE_LOGGING -fpermissive

LOCAL_C_INCLUDES += $(PLT_SRC_ROOT)/Devices/MediaRenderer
LOCAL_C_INCLUDES += $(LOCAL_PATH)/j4a/class/android/os
LOCAL_C_INCLUDES += $(LOCAL_PATH)/j4a/class/com/show/dlnadmr/jni
LOCAL_C_INCLUDES += $(LOCAL_PATH)/j4a

LOCAL_STATIC_LIBRARIES := Platinum
LOCAL_STATIC_LIBRARIES += Neptune
LOCAL_STATIC_LIBRARIES += pltMediaServer
LOCAL_STATIC_LIBRARIES += pltMediaRenderer

ifneq ($(NPT_CONFIG_NO_SSL),1)
LOCAL_STATIC_LIBRARIES += axTLS
endif

include $(BUILD_SHARED_LIBRARY)
