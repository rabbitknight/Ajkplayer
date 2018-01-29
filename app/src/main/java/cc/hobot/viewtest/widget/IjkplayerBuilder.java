package cc.hobot.viewtest.widget;

import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 基于Bilibili Ijkplayer封装的一个播放器
 *
 * @author zhuo.chen
 * @version 2018/1/29.
 */

public class IjkplayerBuilder {

    private WeakReference<ViewGroup> mHookParent; //
    private int mHeight;
    private int mWidth;
    private String url;
    private List<Listener> listeners = new ArrayList<>();
    // 参数option

    /**
     * 设置播放源
     *
     * @param url 链接
     * @return
     */
    public IjkplayerBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加监听器事件，支持pause、start、end等
     *
     * @param listener 监听器
     * @return this
     */
    public IjkplayerBuilder addListener(Listener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * 勾住父布局！
     *
     * @param parent 播放器要填充进的布局
     * @return this
     */
    public IjkplayerBuilder hook(ViewGroup parent) {
        mHookParent = new WeakReference<ViewGroup>(parent);
        mHeight = mHookParent.get().getHeight();
        mWidth = mHookParent.get().getWidth();
        return this;
    }

    /**
     * Build模式。创建出来播放器。
     *
     * @return
     */
    public Ajkplayer build() {
        return new Ajkplayer();
    }

    //**************************************

    /**
     * 抽象接口
     */
    private interface Listener {

    }

    /**
     * 暂停
     */
    public interface OnPauseListener extends Listener {
        void onPause();
    }

    /**
     * 开始
     */
    public interface OnStartListener extends Listener {
        void onStart();
    }

    /**
     * 结束
     */
    public interface OnEndListener extends Listener {
        void onEnd();
    }

}
