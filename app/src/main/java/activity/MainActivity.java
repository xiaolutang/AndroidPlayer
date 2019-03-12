package activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.txl.android.player.R;
import com.txl.player.android.AbsMusicPlayerController;
import com.txl.player.android.music.AndroidMusicPlayerService;
import com.txl.player.android.music.IPlayerUi;
import com.txl.player.android.music.PlayerTag;

import java.util.ArrayList;
import java.util.List;

import model.MusicData;

public class MainActivity extends AppCompatActivity implements IPlayerUi<MusicData> {
    AbsMusicPlayerController musicPlayerController;

    /**
     * 名字
     * */
    TextView tvMusicName;
    ImageView imagePreMusic;
    ImageView imageNextMusic;
    ImageView imageToggleMusic;
    ImageView imageMusicAuthorIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initView();
        musicPlayerController = new MusicPlayerController(this, AndroidMusicPlayerService.class);
        musicPlayerController.setPlayerUiChangeListener(this);
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
                musicPlayerController.togglePlay();
            }
        } );
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
        musicPlayerController.destroy();
        musicPlayerController = null;
    }
    @Override
    public void uiPause() {
        togglePlayerUi(false);
    }

    @Override
    public void uiPlay() {
        togglePlayerUi(true);
    }

    @Override
    public void updateProgress(long position) {

    }

    @Override
    public void changePlayerUiData(MusicData data) {
        tvMusicName.setText( data.getMusicName() );
        imageMusicAuthorIcon.setImageResource( data.getPlayerUserIconResId() );
    }

    class MusicPlayerController extends AbsMusicPlayerController{

        List<MusicData> musicData;
        int currentPlayIndex = 0;

        /**
         * @param context
         * @param serviceClass 服务的class
         */
        public MusicPlayerController(Context context, Class<? extends Service> serviceClass) {
            super( context, serviceClass );
        }

        @Override
        protected void serviceConnect() {
            super.serviceConnect();
            prepareMusicData();
            PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
            initPlayer(tag);
            open( musicData.get( currentPlayIndex ).getPlayUrl(),tag );
            changePlayerUiData( musicData.get( currentPlayIndex ) );
        }

        /**
         * 在controller中实现播放器数据相关的处理
         * */
        private void prepareMusicData(){
            musicData = new ArrayList<>(  );
            musicData.add( new MusicData(R.drawable.music_author_01,"一曲相思","http://fs.w.kugou.com/201903122113/79e43f0077b4649d76c73bc7c2336124/G085/M07/0B/10/lQ0DAFujV42AK4xpACkHR2d9qTo587.mp3") );
            musicData.add( new MusicData(R.drawable.music_author_02,"生僻字","http://fs.w.kugou.com/201903122118/7ce6189bd6d162e27f15bd01fc957e76/G111/M06/1D/10/D4cBAFoL9VyASCmXADTAFw14uaI428.mp3") );
        }

        @Override
        protected Intent getBindServiceIntent() {
            Intent intent = new Intent(  );
            return intent;
        }

        @Override
        public void playNext() {
            currentPlayIndex ++;
            currentPlayIndex = currentPlayIndex % musicData.size();
            PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
            open( musicData.get( currentPlayIndex ).getPlayUrl(),tag );
            changePlayerUiData( musicData.get( currentPlayIndex ) );
        }

        @Override
        public void playPre() {
            if(currentPlayIndex == 0){
                currentPlayIndex = musicData.size()-1;
            }else {
                currentPlayIndex--;
            }
            PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
            open( musicData.get( currentPlayIndex ).getPlayUrl(),tag );
            changePlayerUiData( musicData.get( currentPlayIndex ) );
        }
    }
}
