package demo;

import android.content.Context;
import android.content.Intent;

import com.txl.player.android.music.AbsPlayerController;
import com.txl.player.android.music.MediaNotificationManager;
import com.txl.player.android.music.PlayerTag;
import com.txl.player.android.player.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author TXL
 * description :
 */
public class DemoMusicPlayerController extends AbsPlayerController {
    List<MusicData> musicData;
    int currentPlayIndex = 0;

    public static final String ACTION_PLAY_NEXT = "play_next";
    public static final String ACTION_PLAY_PRE = "play_pre";

    public DemoMusicPlayerController(Context context) {
        super(context);
    }

    @Override
    public MediaNotificationManager createMediaNotificationManager(Context context) {
        return null;
    }

    @Override
    public void init(Intent initParams) {
        prepareMusicData();
        //原来的
        PlayerTag lastPlayerTag = getPlayTag();
        PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
        //前后播放的不是同一个切换播放源
        if(!tag.equals(lastPlayerTag)){
            open(musicData.get( currentPlayIndex ).getPlayUrl(),tag);
            onReceiveControllerCommand(ACTION_PLAY_NEXT, musicData.get( currentPlayIndex ));
        }
    }

    private void prepareMusicData(){
        musicData = new ArrayList<>(  );
        //需要自己处理播放地址，这个地址仅限当天有效
        musicData.add( new MusicData(R.drawable.music_author_01,"生僻字","http://fs.w.kugou.com/201903132339/d4f8bcfe9e2a0fdacf26617fa3d1ad07/G111/M06/1D/10/D4cBAFoL9VyASCmXADTAFw14uaI428.mp3") );
        musicData.add( new MusicData(R.drawable.music_author_02,"一曲相思","http://fs.w.kugou.com/201903132343/8d519e70134a078de8cbe588317bfe7d/G085/M07/0B/10/lQ0DAFujV42AK4xpACkHR2d9qTo587.mp3") );
    }

    @Override
    public void playNext() {
        currentPlayIndex ++;
        currentPlayIndex = currentPlayIndex % musicData.size();
        PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
        open( musicData.get( currentPlayIndex ).getPlayUrl(),tag );
        onReceiveControllerCommand(ACTION_PLAY_NEXT, musicData.get( currentPlayIndex ));
        if(isPlaying()){
            play();
        }
    }

    @Override
    public void playPre() {
        if(currentPlayIndex == 0){
            currentPlayIndex = musicData.size()-1;
        }else {
            currentPlayIndex--;
        }
        PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
        setPlayTag(tag);
        open(musicData.get( currentPlayIndex ).getPlayUrl());
        onReceiveControllerCommand(ACTION_PLAY_PRE, musicData.get( currentPlayIndex ));
        if(isPlaying()){
            play();
        }
    }

    @Override
    public void setPlayMode(int mode) {

    }

    @Override
    public void receiveCommand(String action, Object... o) {

    }
}
