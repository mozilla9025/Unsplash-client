package lib.webrtcallkit.video;

import androidx.annotation.Nullable;

import org.webrtc.VideoFrame;
import org.webrtc.VideoSink;

public final class VideoSinkProxy implements VideoSink {

    @Nullable
    private VideoSink targetProxy;

    public VideoSinkProxy(@Nullable VideoSink target) {
        this.targetProxy = target;
    }

    @Override
    public void onFrame(VideoFrame videoFrame) {
        if (targetProxy != null) targetProxy.onFrame(videoFrame);
    }
}
