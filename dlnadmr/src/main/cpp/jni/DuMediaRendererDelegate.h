//
// Created by ztq on 19-5-20.
//

#ifndef AUTOPARSEDCMAKEPROJECT_DUMEDIARENDERERDELEGATE_H
#define AUTOPARSEDCMAKEPROJECT_DUMEDIARENDERERDELEGATE_H

#include "PltMediaRenderer.h"

class PLT_MediaRendererDelegate;

class DuMediaRendererDelegate : public PLT_MediaRendererDelegate {
    // ConnectionManager
public:
    DuMediaRendererDelegate();
    virtual NPT_Result OnGetCurrentConnectionInfo(PLT_ActionReference& action);

    // AVTransport
    virtual NPT_Result OnNext(PLT_ActionReference& action);
    virtual NPT_Result OnPause(PLT_ActionReference& action);
    virtual NPT_Result OnPlay(PLT_ActionReference& action);
    virtual NPT_Result OnPrevious(PLT_ActionReference& action);
    virtual NPT_Result OnSeek(PLT_ActionReference& action);
    virtual NPT_Result OnStop(PLT_ActionReference& action);
    virtual NPT_Result OnSetAVTransportURI(PLT_ActionReference& action);
    virtual NPT_Result OnSetPlayMode(PLT_ActionReference& action);

    // RenderingControl
    virtual NPT_Result OnSetVolume(PLT_ActionReference& action);
    virtual NPT_Result OnSetVolumeDB(PLT_ActionReference& action);
    virtual NPT_Result OnGetVolumeDBRange(PLT_ActionReference& action);
    virtual NPT_Result OnSetMute(PLT_ActionReference& action);

    ~DuMediaRendererDelegate();

    void setMediaRenderer(PLT_MediaRenderer *renderer) { m_renderer = renderer;}

private:
    PLT_MediaRenderer *m_renderer;
};


#endif //AUTOPARSEDCMAKEPROJECT_DUMEDIARENDERERDELEGATE_H
