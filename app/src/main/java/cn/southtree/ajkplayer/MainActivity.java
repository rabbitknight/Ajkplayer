package cn.southtree.ajkplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.southtree.ajkplayer.widget.Ajkplayer;


public class MainActivity extends AppCompatActivity {
    private Ajkplayer ajkplayer;
    //private TableLayout tableLayout;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        ajkplayer = findViewById(R.id.ijk_player);
        ajkplayer.setSource("http://aweme.snssdk.com/aweme/v1/play/?video_id=1f30704c441f482bb062f94eb9898f66&line=0&ratio=720p&media_type=4&vr_type=0",Ajkplayer.FLAG_URL);
        //ajkplayer.setSource("http://baobab.kaiyanapp.com/api/v1/playUrl?vid=35229&editionType=normal&source=aliyun", Ajkplayer.FLAG_URL);
        //ajkplayer.setSource("http://baobab.kaiyanapp.com/api/v1/playUrl?vid=9528&editionType=normal&source=ucloud",Ajkplayer.FLAG_URL);
        //ajkplayer.setSource("http://video.pearvideo.com/mp4/short/20170414/cont-1064146-10369519-ld.mp4", Ajkplayer.FLAG_URL);
        //ajkplayer.setSource("http://vevoplaylist-live.hls.adaptive.level3.net/vevo/ch1/appleman.m3u8",Ajkplayer.FLAG_URL);
        ajkplayer.recreate();
        ajkplayer.start();
        ajkplayer.setTitle("一个简单的播放器");
        ajkplayer.addListener(new Ajkplayer.OnStartListener() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: ");
            }
        });
        ajkplayer.addListener(new Ajkplayer.OnPauseListener() {
            @Override
            public void onPause() {
                Log.i(TAG, "onPause: ");
            }
        });
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            //设置全屏即隐藏状态栏

            //横屏 视频充满全屏
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //恢复状态栏
        }
    }
}
