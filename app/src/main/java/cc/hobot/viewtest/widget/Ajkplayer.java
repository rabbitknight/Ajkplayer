package cc.hobot.viewtest.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cc.hobot.viewtest.R;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by zhuo.chen on 2018/1/29.
 */

public class Ajkplayer extends FrameLayout {
    private static final String TAG = Ajkplayer.class.getSimpleName();

    public static final int FLAG_URL = 2;
    public static final int FLAG_FILE = 4;
    public static final int FLAG_A = 8;

    public static final int TYPE_LIS_START = 2 * 1024;
    public static final int TYPE_LIS_PAUSE = 4 * 1024;
    public static final int TYPE_LIS_END = 8 * 1024;

    private IjkMediaPlayer mediaPlayer;
    private SurfaceTexture mSurfaceTexture;
    private TextureView.SurfaceTextureListener mTextureListener;
    private SurfaceView mSurfaceView;
    private static Timer timer;
    private static Handler handler;
    // 播放器专注于播放逻辑，并不关心自身生命周期，生命周期处理，交给Builder处理
    //private Application.ActivityLifecycleCallbacks acticityLifecycleCallbacks;
    //private FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks;

    private AjkplayerListener mListener = new AjkplayerListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            super.onPrepared(iMediaPlayer);
            duration = mediaPlayer.getDuration();
            Log.d(TAG, "onPrepared: duration=" + mediaPlayer.getDuration() +
                    ",seekDuration=" + mediaPlayer.getSeekLoadDuration() +
                    ",audioDuration=" + mediaPlayer.getAudioCachedDuration() +
                    ",videoDuration=" + mediaPlayer.getVideoCachedDuration());
        }

        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.d(TAG, "onInfo: " + i + "," + i1 + "," + iMediaPlayer.getDuration());
            return super.onInfo(iMediaPlayer, i, i1);
        }

        @Override
        public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
            super.onTimedText(iMediaPlayer, ijkTimedText);
            Log.d(TAG, "onTimedText: " + ijkTimedText);
        }

        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
            super.onBufferingUpdate(iMediaPlayer, i);
            Log.d(TAG, "onBufferingUpdate: " + i + "," + mediaPlayer.getSeekLoadDuration());
            Log.d(TAG, "onBufferingUpdate: duration=" + mediaPlayer.getDuration() +
                    ",seekDuration=" + mediaPlayer.getSeekLoadDuration() +
                    ",audioDuration=" + mediaPlayer.getAudioCachedDuration() +
                    ",videoDuration=" + mediaPlayer.getVideoCachedDuration() +
                    ",currentPos" + mediaPlayer.getCurrentPosition());

        }

        @Override
        public boolean onNativeInvoke(int i, Bundle bundle) {
            Log.d(TAG, "onNativeInvoke: " + i + "," + bundle);
            return super.onNativeInvoke(i, bundle);
        }

        @Override
        public String onControlResolveSegmentUrl(int i) {
            Log.d(TAG, "onControlResolveSegmentUrl: " + i);
            return super.onControlResolveSegmentUrl(i);
        }

        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            super.onCompletion(iMediaPlayer);
            Log.d(TAG, "onCompletion: ");
        }

        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.d(TAG, "onError: " + i + "," + i1);
            return super.onError(iMediaPlayer, i, i1);
        }

        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {
            super.onSeekComplete(iMediaPlayer);
            Log.d(TAG, "onSeekComplete: ");

        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            super.onVideoSizeChanged(iMediaPlayer, i, i1, i2, i3);
            Log.d(TAG, "onVideoSizeChanged: " + i + "," + i1 + "," + i2 + "," + i3);
        }

        @Override
        public String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String s, int i, int i1) {
            Log.d(TAG, "onMediaCodecSelect: " + s + "," + i + "," + i1);
            return super.onMediaCodecSelect(iMediaPlayer, s, i, i1);

        }
    };

    private View contentView;
    private TextureView displayView;
    private Toolbar toolbar;
    private ImageButton backIb;
    private TextView titleTv;
    private RelativeLayout ctlRl;
    private AppCompatSeekBar seekSb;
    private ImageButton switchIb;
    private TextView progressTv;
    private ImageButton fullIb;

    private ViewGroup fullParent;

    private SparseArray<Listener> events = new SparseArray<>();


    private int dataFlag = 0;
    private String dataSource;
    private long duration;
    private boolean isMax = false;

    public Ajkplayer(Context context) {
        this(context, null);
    }

    public Ajkplayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ajkplayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: " + event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(120), 3000);
                Log.d(TAG, "onTouch: +ACTION_UP");
                return false;
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(120);
                Log.d(TAG, "onTouch: +ACTION_DOWN");
                ctlRl.setVisibility(VISIBLE);
                toolbar.setVisibility(VISIBLE);
                return false;
        }
        return super.onTouchEvent(event);

    }

    private void init() {
        fullParent = ((Activity) this.getContext()).findViewById(Window.ID_ANDROID_CONTENT);
        Log.d(TAG, "init: " + fullParent);

        contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_ijkplayer, null);
        this.addView(contentView);
        displayView = contentView.findViewById(R.id.player_texture);
        toolbar = contentView.findViewById(R.id.toolbar_tb);
        backIb = contentView.findViewById(R.id.back_ib);
        titleTv = contentView.findViewById(R.id.title_tv);
        ctlRl = contentView.findViewById(R.id.ctl_rl);
        seekSb = contentView.findViewById(R.id.seek_sb);
        switchIb = contentView.findViewById(R.id.switch_ib);
        progressTv = contentView.findViewById(R.id.progress_tv);
        fullIb = contentView.findViewById(R.id.full_ib);
        contentView.requestFocus();
        initTexture();
        // 进度
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                    Message msg = new Message();
                    msg.what = 110;
                    Bundle bundle = new Bundle();
                    bundle.putLong("time", mediaPlayer.getCurrentPosition());
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        }, 0, 1000);
        handler = new Handler(getContext().getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (110 == msg.what) {
                    progressTv.setText(String.valueOf(convertTime(msg.getData().getLong("time", -1)) + "/" + convertTime(duration)));
                    if (0 != (msg.getData().getLong("time")) && 0 != duration) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            // TODO: 2018/1/31 crash
                            seekSb.setProgress((int) (msg.getData().getLong("time") * 100 / duration), true);
                            Log.d(TAG, "handleMessage: " + msg.getData().getLong("time") * 100 / duration);
                        } else {
                            seekSb.setProgress((int) (msg.getData().getLong("time") * 100 / duration));
                        }
                    }

                } else if (120 == msg.what) {
                    ctlRl.setVisibility(GONE);
                    toolbar.setVisibility(GONE);
                }

            }
        };

        // 组件
        seekSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress + "," + fromUser);
                if (null != mediaPlayer) {
                    progressTv.setText(String.valueOf(convertTime(progress * duration / 100) + "/" + convertTime(duration)));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                if (null != mediaPlayer) {
                    Log.d(TAG, "onStopTrackingTouch: " + seekBar.getProgress() * duration / 100);
                    seekTo(seekBar.getProgress() * duration / 100);
                }
            }
        });

        switchIb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mediaPlayer) {
                    if (mediaPlayer.isPlaying()) {
                        switchIb.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_24dp));
                        pause();
                        seekSb.setThumb(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_off));
                    } else {
                        switchIb.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause));
                        start();
                        seekSb.setThumb(ContextCompat.getDrawable(getContext(), R.drawable.ic_star));
                    }
                }
            }
        });
        backIb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/1/31 预留
            }
        });
        fullIb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMax) {
                    Log.d(TAG, "onClick: ");
                    setMax();
                } else {
                    setMin();
                }
            }
        });


        //setMax();
    }

    // 全屏
    private void setMax() {
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (null != parent) {
            parent.removeView(contentView);
            Log.d(TAG, "setMax: " + parent);
        }
        fullParent.addView(contentView);
        isMax = true;
    }

    private void setMin() {
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (null != parent) {
            parent.removeView(contentView);
            Log.d(TAG, "setMin: " + parent);
        }
        this.addView(contentView);
        isMax = false;
    }

    public void initTexture() {
        mTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                if (null != mediaPlayer) {
                    mSurfaceTexture = surface;
                    mediaPlayer.setSurface(new Surface(surface));
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };
        displayView.setSurfaceTextureListener(mTextureListener);
    }

    // 事件
    public void addListener(Listener listener) {
        if (listener instanceof OnStartListener) {
            events.put(TYPE_LIS_START, listener);
            Log.d(TAG, "addListener: START");
        } else if (listener instanceof OnPauseListener) {
            events.put(TYPE_LIS_PAUSE, listener);
            Log.d(TAG, "addListener: PAUSE");
        } else if (listener instanceof OnEndListener) {
            events.put(TYPE_LIS_END, listener);
            Log.d(TAG, "addListener: END");
        } else {
            Log.d(TAG, "addListener: NULL");
        }
    }

    // 开始
    public void start() {
        if (null != mediaPlayer) {
            mediaPlayer.start();
            Log.d(TAG, "start: duration=" + mediaPlayer.getDuration() +
                    ",seekDuration=" + mediaPlayer.getSeekLoadDuration() +
                    ",audioDuration=" + mediaPlayer.getAudioCachedDuration() +
                    ",videoDuration=" + mediaPlayer.getVideoCachedDuration());

            if (null != events.get(TYPE_LIS_START)) {
                ((OnStartListener) events.get(TYPE_LIS_START)).onStart();

            }
        }
    }

    // 暂停
    public void pause() {
        if (null != mediaPlayer) {
            mediaPlayer.pause();
        }
    }

    // 进度
    public void seekTo(long time) {
        if (null != mediaPlayer) {
            mediaPlayer.seekTo(time);
        }
    }

    // 停止
    public void stop() {
        if (null != mediaPlayer) {
            mediaPlayer.stop();
        }
    }

    // 重置
    public void reset() {
        if (null != mediaPlayer) {
            mediaPlayer.reset();
        }
    }

    // 释放
    public void release() {
        if (null != mediaPlayer) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 重建
    public void recreate() {
        if (null != mediaPlayer) {
            release();
        }
        mediaPlayer = new IjkMediaPlayer();
        mediaPlayer.setOnCompletionListener(mListener);
        mediaPlayer.setOnNativeInvokeListener(mListener);
        mediaPlayer.setOnPreparedListener(mListener);
        mediaPlayer.setOnControlMessageListener(mListener);
        mediaPlayer.setOnErrorListener(mListener);
        mediaPlayer.setOnMediaCodecSelectListener(mListener);
        mediaPlayer.setOnBufferingUpdateListener(mListener);
        mediaPlayer.setOnInfoListener(mListener);
        mediaPlayer.setOnVideoSizeChangedListener(mListener);
        mediaPlayer.setOnSeekCompleteListener(mListener);
        mediaPlayer.setOnTimedTextListener(mListener);
        setDataSource(dataSource, dataFlag);
    }

    // 资源
    public void setSource(String source, int dataFlag) {
        this.dataFlag = dataFlag;
        this.dataSource = source;
        setDataSource(source, dataFlag);
    }

    // 资源
    private void setDataSource(String source, int dataFlag) {
        if (null == source || "".equals(source) || dataFlag == 0)
            return;

        if (null != mediaPlayer) {
            try {
                switch (dataFlag) {
                    case FLAG_FILE:
                        mediaPlayer.setDataSource(source);
                        break;
                    case FLAG_URL:
                        mediaPlayer.setDataSource(getContext(), Uri.parse(source));
                        break;
                    default:
                        break;
                }
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.e(TAG, "setSource: ", e);
            }

        }
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    /**
     * time转化类
     * 179000 -> 00:02:59
     *
     * @param time 179000
     * @return 00:02:59
     */
    private String convertTime(long time) {
        long h = -1;  // 时
        long m = -1;  // 分
        long s = -1;  // 喵

        s = time / 1000 % 60;
        m = time / 1000 / 60 % 60;
        h = time / 1000 / 3600;

        return String.valueOf(h) +
                ":" +
                (m < 10 ? "0" + m : m) +
                ":" +
                (s < 10 ? "0" + s : s);
    }
    //**************************************

    /**
     * 抽象接口
     */
    public interface Listener {

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
