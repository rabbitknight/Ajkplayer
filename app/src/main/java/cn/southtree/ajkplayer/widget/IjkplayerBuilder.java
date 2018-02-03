package cn.southtree.ajkplayer.widget;

import android.content.Context;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于Bilibili Ijkplayer封装的一个播放器
 *
 * @author zhuo.chen
 * @version 2018/1/29.
 */

public class IjkplayerBuilder {

    private WeakReference<ViewGroup> mHookParent;
    private WeakReference<Context> mContext;
    private int mHeight;
    private int mWidth;
    private String url;
    private List<Ajkplayer.Listener> listeners = new ArrayList<>();
    // 参数option


    /**
     * 设置上下文
     *
     * @param context 上下文
     * @return
     */
    public IjkplayerBuilder with(Context context) {
        if (null != context) {
            mContext = new WeakReference<Context>(context);
        }
        return this;
    }

    /**
     * 设置播放源
     *
     * @param url 链接
     * @return
     */
    public IjkplayerBuilder load(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加监听器事件，支持pause、start、end等
     *
     * @param listener 监听器
     * @return this
     */
    public IjkplayerBuilder listener(Ajkplayer.Listener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * 相关配置
     * @return
     */
    public IjkplayerBuilder option() {
        return this;
    }


    /**
     * 勾住父布局！
     *
     * @param parent 播放器要填充进的布局
     * @return this
     */
    public IjkplayerBuilder target(ViewGroup parent) {
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
        return new Ajkplayer(null);
    }


}
