package com.txl.player.android.music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author TXL
 * description :音频播放服务
 */
public abstract class AbsMusicPlayerService extends Service {

    AbsPlayerController playerController;

    public AbsMusicPlayerService() {

    }

    abstract AbsPlayerController createPlayerController();

    abstract IMusicPlayer createMusicPlayer();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
