package cc.hobot.viewtest.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.hobot.viewtest.R;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

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
    private AjkplayerListener mListener;

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

    private SparseArray<Listener> events = new SparseArray<>();


    private int dataFlag = 0;
    private String dataSource;

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


    private void init() {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_ijkplayer, this);
        displayView = findViewById(R.id.player_texture);
        toolbar = findViewById(R.id.toolbar_tb);
        backIb = findViewById(R.id.back_ib);
        titleTv = findViewById(R.id.title_tv);
        ctlRl = findViewById(R.id.ctl_rl);
        seekSb = findViewById(R.id.seek_sb);
        switchIb = findViewById(R.id.switch_ib);
        progressTv = findViewById(R.id.progress_tv);
        fullIb = findViewById(R.id.full_ib);


        initTexture();
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
