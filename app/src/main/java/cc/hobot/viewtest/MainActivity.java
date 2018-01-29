package cc.hobot.viewtest;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import cc.hobot.viewtest.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private IjkVideoView ijkVideo;
    private TableLayout tableLayout;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ijkVideo = findViewById(R.id.ijk_IVV);
        tableLayout = findViewById(R.id.table_tl);
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        //ijkVideo.setHudView(tableLayout);
        //ijkVideo.setVideoURI(Uri.parse("http://vod.cntv.lxdns.com/flash/mp4video61/TMS/2017/08/17/63bf8bcc706a46b58ee5c821edaee661_h264818000nero_aac32-5.mp4"));
        //ijkVideo.start();

    }
}
