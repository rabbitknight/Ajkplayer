package cn.southtree.ajkplayer.widget;

import android.os.Bundle;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Listener集中处理类
 * @author zhuo.chen
 * @version 2018/1/30.
 */

public abstract class AjkplayerListener implements IjkMediaPlayer.OnControlMessageListener,
        IMediaPlayer.OnBufferingUpdateListener, IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnInfoListener, IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnPreparedListener, IjkMediaPlayer.OnSeekCompleteListener,
        IjkMediaPlayer.OnVideoSizeChangedListener, IjkMediaPlayer.OnTimedTextListener,
        IjkMediaPlayer.OnNativeInvokeListener, IjkMediaPlayer.OnMediaCodecSelectListener {

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

    }

    @Override
    public String onControlResolveSegmentUrl(int i) {
        return null;
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

    }

    @Override
    public String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String s, int i, int i1) {
        return null;
    }

    @Override
    public boolean onNativeInvoke(int i, Bundle bundle) {
        return false;
    }
}
