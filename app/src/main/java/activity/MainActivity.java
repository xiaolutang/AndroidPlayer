package activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.txl.android.player.R;
import com.txl.player.android.music.IMusicPlayer;
import com.txl.player.android.music.IMusicPlayerController;
import demo.AndroidMusicPlayerService;
import demo.DemoMusicPlayerController;
import demo.MusicData;

public class MainActivity extends AppCompatActivity implements IMusicPlayer.IMusicPlayerEvents{
    protected final String TAG = getClass().getSimpleName();
    IMusicPlayerController musicPlayerController;
    /**
     * 名字
     * */
    TextView tvMusicName;
    ImageView imagePreMusic;
    ImageView imageNextMusic;
    ImageView imageToggleMusic;
    ImageView imageMusicAuthorIcon;
    SeekBar seekBar;

    long duration = 0;

    ServiceConnection serviceConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initView();
        Intent intent = new Intent();
        intent.setClass(this,AndroidMusicPlayerService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if(service instanceof IMusicPlayerController){
                    musicPlayerController = (IMusicPlayerController) service;
                    musicPlayerController.addPlayerEventListener(MainActivity.this);
                    //在使用之前需要初始化一下
                    musicPlayerController.init(null);
                }else {
                    Log.e(TAG,"serviceConnection musicPlayerController init error unBind service");
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicPlayerController.removePlayerEventListener(MainActivity.this);
            }
        };
        bindService(intent, serviceConnection,Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        tvMusicName = findViewById( R.id.tv_music_name );
        imageMusicAuthorIcon = findViewById( R.id.image_music_author );
        imagePreMusic = findViewById( R.id.image_pre_music );
        imagePreMusic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayerController.playPre();
            }
        } );
        imageNextMusic = findViewById( R.id.image_next_music );
        imageNextMusic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayerController.playNext();
            }
        } );
        imageToggleMusic = findViewById( R.id.image_toggle_music );
        imageToggleMusic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(musicPlayerController.isPlaying()){
                   musicPlayerController.pause();
               }else {
                   musicPlayerController.play();
               }
            }
        } );
        seekBar = findViewById(R.id.music_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG,"onProgressChanged  progress :"+progress+"  fromUser: "+fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"onStartTrackingTouch  ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"onStopTrackingTouch  ");
                if(duration != 0){
                    long pos = seekBar.getProgress()+duration/100;
                    musicPlayerController.seek(pos);
                }
            }
        });
    }

    private void togglePlayerUi(boolean isPlay){
        if(isPlay){
            imageToggleMusic.setImageResource( R.drawable.image_pause );
        }else {
            imageToggleMusic.setImageResource( R.drawable.image_play );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        musicPlayerController = null;
    }

    private void changePageUi(MusicData musicData){
        tvMusicName.setText(musicData.getMusicName());
        imageMusicAuthorIcon.setImageResource(musicData.getPlayerUserIconResId());
    }

    @Override
    public boolean onError(IMusicPlayer xmp, int code, String msg) {
        togglePlayerUi(false);
        return false;
    }

    @Override
    public boolean onPrepared(IMusicPlayer player) {
        duration = musicPlayerController.getDuration();
        return false;
    }

    @Override
    public boolean onSeekComplete(IMusicPlayer player, long pos) {
        return false;
    }

    @Override
    public boolean onComplete(IMusicPlayer player) {
        togglePlayerUi(false);
        return false;
    }

    @Override
    public boolean onBuffering(IMusicPlayer player, boolean buffering, float percentage) {
        return false;
    }

    @Override
    public boolean onProgress(IMusicPlayer player, long pos) {
        if(duration != 0){
            seekBar.setProgress((int) (100*pos/duration));
        }else {
            duration = musicPlayerController.getDuration();
        }
        return false;
    }

    @Override
    public void onMusicServiceDestroy(IMusicPlayer player) {

    }

    @Override
    public boolean onPlay(IMusicPlayer player) {
        togglePlayerUi(true);
        return false;
    }

    @Override
    public boolean onPause(IMusicPlayer player) {
        togglePlayerUi(false);
        return false;
    }

    @Override
    public boolean onStop(IMusicPlayer player) {
        togglePlayerUi(false);
        return false;
    }

    @Override
    public boolean onReceiveControllerCommand(String action, Object... o) {
        switch (action){
            case DemoMusicPlayerController.ACTION_PLAY_NEXT:
            case DemoMusicPlayerController.ACTION_PLAY_PRE:
                if(o != null && o.length > 0){
                    MusicData musicData = (MusicData) o[0];
                    changePageUi(musicData);
                }
                break;
            default:
                break;
        }
        return false;
    }
}
