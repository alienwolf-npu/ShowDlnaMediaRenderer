/*****************************************************************
|
|      Android JNI Interface
|
|      (c) 2002-2012 Plutinosoft LLC
|      Author: Sylvain Rebaud (sylvain@plutinosoft.com)
|
 ****************************************************************/

/*----------------------------------------------------------------------
|       includes
+---------------------------------------------------------------------*/
#include <assert.h>
#include <jni.h>
#include <string.h>
#include <sys/types.h>
#include <stdint.h>
#include <unistd.h>

#include "platinum-jni.h"
#include "Platinum.h"
#include "PltUPnP.h"
#include "PltMediaRenderer.h"
#include "PltDeviceData.h"
#include "PltUtilities.h"
#include "DuMediaRendererDelegate.h"
#include "j4a_allclasses.h"
#include "j4a/class/com/show/dlnadmr/jni/PlatinumReflection.h"
#include "platinum_android_def.h"
#include <pthread.h>
#include <android/log.h>

/*----------------------------------------------------------------------
|   logging
+---------------------------------------------------------------------*/
NPT_SET_LOCAL_LOGGER("platinum.android.jni")

/*----------------------------------------------------------------------
|   functions
+---------------------------------------------------------------------*/
__attribute__((constructor)) static void onDlOpen(void) {
}

static JavaVM* g_jvm;
static jobject g_clazz;

static pthread_key_t g_thread_key;
static pthread_once_t g_key_once = PTHREAD_ONCE_INIT;

static PLT_DeviceHostReference *sDevice = NULL;
static PLT_UPnP *sUPnp = NULL;
static PLT_MediaRenderer *sMediaRenderer = NULL;

static jint jni_startMediaRender(JNIEnv *env, jclass clazz, jstring name, jstring uuid) {
    NPT_LOG_INFO("startMediaRender");
	const char *c_name = NULL;
    const char *c_uuid = NULL;
    if (sUPnp == NULL) {
        sUPnp = new PLT_UPnP();
    }

    c_name = env->GetStringUTFChars(name, NULL);
    c_uuid = env->GetStringUTFChars(uuid, NULL);

    if (sDevice == NULL) {
        DuMediaRendererDelegate *delegate = new DuMediaRendererDelegate();
        NPT_LOG_INFO_2("===>friendlyname: %s uuid: %s", c_name, c_uuid);
        sMediaRenderer = new PLT_MediaRenderer(c_name, false, c_uuid);
//        ((PLT_DeviceHost*)sMediaRenderer)->SetByeByeFirst(false);
        sMediaRenderer->SetDelegate(delegate);
        delegate->setMediaRenderer(sMediaRenderer);
        sDevice = new PLT_DeviceHostReference(sMediaRenderer);
    }
    sUPnp->Start();
    sUPnp->AddDevice(*sDevice);

    if (c_name) {
        env->ReleaseStringUTFChars(name, c_name);
    }
    if (c_uuid) {
        env->ReleaseStringUTFChars(uuid, c_uuid);
    }

    return 0;
}

static jint jni_stopMediaRender(JNIEnv *env, jclass clazz) {
    NPT_LOG_INFO("stopMediaRender");
	if (sUPnp != NULL) {
        sUPnp->RemoveDevice(*sDevice);
        sUPnp->Stop();
        delete sDevice;
        delete sUPnp;
        sDevice = NULL;
        sUPnp = NULL;
    }

    return 0;
}

static jboolean jni_responseGenaEvent(JNIEnv *env, jclass clazz, jint cmd, jstring value, jstring data) {
    NPT_LOG_INFO("responseGenaEvent");
	const char *c_value = NULL;
    const char *c_data = NULL;

	do {
		if (!value || !data || !sUPnp) {
			break;
		}
        
		if (value) {
            c_value = env->GetStringUTFChars(value, NULL);
        }
        if (data) {
            c_data = env->GetStringUTFChars(data, NULL);
        }

        PLT_Service *serviceAVT;
        NPT_CHECK_WARNING(sMediaRenderer->FindServiceByType("urn:schemas-upnp-org:service:AVTransport:1", serviceAVT));
		if (!serviceAVT) {
			break;
		}
		PLT_Service *serivceControl;
		NPT_CHECK_WARNING(sMediaRenderer->FindServiceByType("urn:schemas-upnp-org:service:RenderingControl:1", serivceControl));
        switch (cmd) {
            case MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_DURATION:
                NPT_LOG_INFO_1("set duration: %s", c_value);
                serviceAVT->SetStateVariable("CurrentMediaDuration", c_value);
                serviceAVT->SetStateVariable("CurrentTrackDuration", c_value);
                break;
            case MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_POSITION:
                NPT_LOG_INFO_1("set position: %s", c_value);
                serviceAVT->SetStateVariable("RelativeTimePosition", c_value);
                serviceAVT->SetStateVariable("AbsoluteTimePosition", c_value);
                break;
            case MEDIA_RENDER_TOCONTRPOINT_SET_MEDIA_PLAYINGSTATE:
                NPT_LOG_INFO_1("set state: %s", c_value);
                serviceAVT->SetStateVariable("TransportState", c_value);
                break;
            case MEDIA_RENDER_TOCONTRPOINT_SET_MUTE:
            	NPT_LOG_INFO_1("set mute: %s", c_value);
            	serivceControl->SetStateVariable("Mute", c_value);
            	serivceControl->SetStateVariableExtraAttribute("Volume", "Channel", "Master");
            	break;
            case MEDIA_RENDER_TOCONTRPOINT_SET_VOLUME:
            	NPT_LOG_INFO_1("set volume: %s", c_value);
            	serivceControl->SetStateVariable("Volume", c_value);
            	serivceControl->SetStateVariableExtraAttribute("Volume", "Channel", "Master");
            	break;
            case MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_UI:
                NPT_LOG_INFO_1("set currenturi: %s", c_value);
                serviceAVT->SetStateVariable("CurrentTrackURI", c_value);
            	break;
            case MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_META:
                NPT_LOG_INFO_1("set current_meta: %s", c_value);
                serviceAVT->SetStateVariable("CurrentTrackMetaData", c_value);
                break;
            case MEDIA_RENDER_TOCONTPROINT_SET_CURRENT_TRACK:
                NPT_LOG_INFO_1("set current_track: %s", c_value);
                serviceAVT->SetStateVariable("CurrentTrack", c_value);
                break;
            default:
                NPT_LOG_INFO("not implemented!");
                break;
        }
	} while (0);

	if (c_value) {
		env->ReleaseStringUTFChars(value, c_value);
	}
	if (c_data) {
		env->ReleaseStringUTFChars(data, c_data);
	}

    return (jboolean)1;
}

static jboolean jni_enableLogPrint(JNIEnv *env, jclass clazz, jboolean enable) {
    if (enable) {
        NPT_LogManager::GetDefault().Configure(
                "plist:.level=INFO;.handlers=ConsoleHandler;.ConsoleHandler.outputs=2;.ConsoleHandler.colors=false;.ConsoleHandler.filter=59");
    } else {
        NPT_LogManager::GetDefault().Configure(
                "plist:.level=OFF;.handlers=ConsoleHandler;.ConsoleHandler.outputs=2;.ConsoleHandler.colors=false;.ConsoleHandler.filter=59");
    }
    return (jboolean)0;
}

static jstring jni_getConnectedDmcUserAgent(JNIEnv *env, jclass clazz) {
    if (sMediaRenderer) {
        return env->NewStringUTF(sMediaRenderer->GetConnectedDmcUserAgent());
    }

    return env->NewStringUTF("none");
}

static jstring jni_getConnectedDmcIP(JNIEnv *env, jclass clazz) {
    if (sMediaRenderer) {
        return env->NewStringUTF(sMediaRenderer->GetConnectedDmcIP());
    }

    return env->NewStringUTF("0.0.0.0");
}

static JNINativeMethod g_methods[] = {
    {"nativeStartMediaRender", "(Ljava/lang/String;Ljava/lang/String;)I", (void *) jni_startMediaRender},
    {"nativeStopMediaRender", "()I", (void *) jni_stopMediaRender},
    {"nativeResponseGenaEvent", "(ILjava/lang/String;Ljava/lang/String;)Z", (void *) jni_responseGenaEvent},
    {"nativeGetConnectedDmcUserAgent", "()Ljava/lang/String;", (void *)jni_getConnectedDmcUserAgent},
    {"nativeGetConnectedDmcIP", "()Ljava/lang/String;", (void *)jni_getConnectedDmcIP},
    {"nativeEnableLogPrint", "(Z)Z", (void *) jni_enableLogPrint}
};

static void SDL_JNI_ThreadDestroyed(void* value)
{
    JNIEnv *env = (JNIEnv*) value;
    if (env != NULL) {
        NPT_LOG_INFO_2("%s: [%d] didn't call SDL_JNI_DetachThreadEnv() explicity\n", __func__, (int)gettid());
        g_jvm->DetachCurrentThread();
        pthread_setspecific(g_thread_key, NULL);
    }
}

static void make_thread_key()
{
    pthread_key_create(&g_thread_key, SDL_JNI_ThreadDestroyed);
}

jint SDL_JNI_SetupThreadEnv(JNIEnv **p_env)
{
    JavaVM *jvm = g_jvm;
    if (!jvm) {
        NPT_LOG_INFO("SDL_JNI_GetJvm: AttachCurrentThread: NULL jvm");
        return -1;
    }

    pthread_once(&g_key_once, make_thread_key);

    JNIEnv *env = (JNIEnv*) pthread_getspecific(g_thread_key);
    if (env) {
        *p_env = env;
        return 0;
    }

    if (jvm->AttachCurrentThread(&env, NULL) == JNI_OK) {
        pthread_setspecific(g_thread_key, env);
        *p_env = env;
        return 0;
    }

    return -1;
}

void SDL_JNI_DetachThreadEnv()
{
    JavaVM *jvm = g_jvm;

    NPT_LOG_INFO_1("ttid:[%d]", (int)gettid());

    pthread_once(&g_key_once, make_thread_key);

    JNIEnv *env = (JNIEnv *)pthread_getspecific(g_thread_key);
    if (!env)
        return;
    pthread_setspecific(g_thread_key, NULL);

    if (jvm->DetachCurrentThread() == JNI_OK)
        return;

    return;
}

void J4A_onActionReflection(int cmd, const char* value, const char* data) {
    JNIEnv *env = NULL;

    if (JNI_OK != SDL_JNI_SetupThreadEnv(&env)) {
        NPT_LOG_INFO("SetupThreadEnv failed");
        return;
    }

    J4AC_com_show_dlnadmr_jni_PlatinumReflection__onActionReflection__catchAll(env, cmd, value, data);
    if (J4A_ExceptionCheck__catchAll(env)) {
        NPT_LOG_INFO("J4A_onActionReflection failed");
        SDL_JNI_DetachThreadEnv();
        return;
    }

    SDL_JNI_DetachThreadEnv();

    return;
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    NPT_LogManager::GetDefault().Configure(
            "plist:.level=INFO;.handlers=ConsoleHandler;.ConsoleHandler.outputs=2;.ConsoleHandler.colors=false;.ConsoleHandler.filter=59");

    JNIEnv* env = NULL;

    g_jvm = vm;
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    assert(env != NULL);

    int retval = J4A_LoadAll__catchAll(env);
    NPT_LOG_INFO_1("J4A_Local_class: %d", retval);

    jclass clazz = env->FindClass("com/show/dlnadmr/jni/PlatinumJniProxy");
    if (!clazz) {
        return -1;
    }
    g_clazz = env->NewGlobalRef(clazz);
    if (!g_clazz) {
        env->DeleteLocalRef(clazz);
        return -1;
    }

    env->DeleteLocalRef(clazz);

    env->RegisterNatives((jclass)g_clazz, g_methods, sizeof(g_methods)/sizeof(g_methods[0]));

    return JNI_VERSION_1_4;
}

JNIEXPORT void JNI_OnUnload(JavaVM *jvm, void *reserved)
{

}
