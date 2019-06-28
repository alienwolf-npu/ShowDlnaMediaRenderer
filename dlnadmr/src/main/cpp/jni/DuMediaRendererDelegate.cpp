//
// Created by ztq on 19-5-20.
//

#include "DuMediaRendererDelegate.h"
#include "Platinum.h"
#include "PltUPnP.h"
#include "PltMediaRenderer.h"
#include "PltDeviceData.h"
#include "PltUtilities.h"
#include "PltAction.h"
#include "j4a/class/com/show/dlnadmr/jni/PlatinumReflection.h"
#include "platinum_android_def.h"
#include "platinum-jni.h"
#include <android/log.h>

NPT_SET_LOCAL_LOGGER("platinum.android.jni")

NPT_Result DuMediaRendererDelegate::OnGetCurrentConnectionInfo(PLT_ActionReference& action)
{
    NPT_LOG_INFO("OnGetCurrentConnectionInfo");
}

// AVTransport
NPT_Result DuMediaRendererDelegate::OnNext(PLT_ActionReference& action){
    NPT_LOG_INFO("DuMediaRenderer:OnNext");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnPause(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnPause");
    J4A_onActionReflection(MEDIA_RENDER_CTL_MSG_PAUSE, "pause", "data");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnPlay(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnPlay");
    J4A_onActionReflection(MEDIA_RENDER_CTL_MSG_PLAY, "play", "data");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnPrevious(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnPrevious");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnSeek(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnSeek");
    NPT_String seekPosition;
    NPT_CHECK_WARNING(action->GetArgumentValue("Target", seekPosition));
    NPT_LOG_INFO_1("OnSeek: %s", seekPosition.GetChars());
    J4A_onActionReflection(MEDIA_RENDER_CTL_MSG_SEEK, seekPosition.GetChars(), "data");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnStop(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnStop");
    J4A_onActionReflection(MEDIA_RENDER_CTL_MSG_STOP, "stop", "data");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnSetAVTransportURI(PLT_ActionReference& action) {
    NPT_String uri;
    NPT_CHECK_WARNING(action->GetArgumentValue("CurrentURI", uri));

    NPT_String metadata;
    NPT_CHECK_WARNING(action->GetArgumentValue("CurrentURIMetaData", metadata));

    PLT_Service* serviceAVT;
    NPT_CHECK_WARNING(m_renderer->FindServiceByType("urn:schemas-upnp-org:service:AVTransport:1", serviceAVT));

    // update service state variables
    serviceAVT->SetStateVariable("AVTransportURI", uri);
    serviceAVT->SetStateVariable("AVTransportURIMetaData", metadata);

    NPT_LOG_INFO_1("DuMediaRenderer:OnSetAVTransportURI: %s", uri.GetChars());
    NPT_LOG_INFO_1("DuMediaRenderer:OnSetAVTransportURI: %s", metadata.GetChars());

    J4A_onActionReflection(MEDIA_RENDER_CTL_MSG_SET_AV_URL, uri.GetChars(), metadata.GetChars());

    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnSetPlayMode(PLT_ActionReference& action) {
    NPT_LOG_INFO("OnSetPlayMode");
    return NPT_Int32 (0);
}

// RenderingControl
NPT_Result DuMediaRendererDelegate::OnSetVolume(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnSetVolume");
    NPT_String volume;
    NPT_CHECK_WARNING(action->GetArgumentValue("DesiredVolume", volume));
    NPT_LOG_INFO_1("OnSetVolume: %s", volume.GetChars());
    J4A_onActionReflection(MEDIA_RENDER_CTL_MSG_SETVOLUME, volume.GetChars(), "data");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnSetVolumeDB(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnSetVolumeDB");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnGetVolumeDBRange(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnGetVolumeDBRange");
    return NPT_Int32 (0);
}

NPT_Result DuMediaRendererDelegate::OnSetMute(PLT_ActionReference& action) {
    NPT_LOG_INFO("DuMediaRenderer:OnSetMute");
    J4A_onActionReflection(MEDIA_RENDER_CTL_MSG_SETMUTE, "", "data");
    return NPT_Int32 (0);
}

DuMediaRendererDelegate::~DuMediaRendererDelegate() {
    NPT_LOG_INFO("call ~DuMediaRenderDelegate!");
}

DuMediaRendererDelegate::DuMediaRendererDelegate() {
    NPT_LOG_INFO("call DuMediaRenderDelegate!");
}



