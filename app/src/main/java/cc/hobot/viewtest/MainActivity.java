package cc.hobot.viewtest;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.MediaController;

import java.io.IOException;

import cc.hobot.viewtest.widget.Ajkplayer;
import cc.hobot.viewtest.widget.IjkplayerBuilder;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private Ajkplayer ajkplayer;
    //private TableLayout tableLayout;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        ajkplayer = findViewById(R.id.ijk_player);
        ajkplayer.setSource("http://baobab.kaiyanapp.com/api/v1/playUrl?vid=14914&editionType=default&source=ucloud",Ajkplayer.FLAG_URL);
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



    }

}
