package com.txl.player.android.music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author TXL
 * description :音频播放服务
 */
public abstract class AbsMusicPlayerService extends Service implements IMusicPlayer.IMusicPlayerEvents {
    protected boolean showNotification;
    IMusicPlayerController musicPlayerController;
    MusicPlayerControllerProxy playerControllerProxy;
    protected MediaNotificationStrategy notificationManager;

    public AbsMusicPlayerService() {
        musicPlayerController =  createPlayerController();
        musicPlayerController.addPlayerEventListener( this );
        playerControllerProxy = new MusicPlayerControllerProxy(musicPlayerController);
        notificationManager = getNotificationManager();
    }

    abstract IMusicPlayerController createPlayerController();
    abstract MediaNotificationStrategy getNotificationManager();

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(playerControllerProxy == null){
            playerControllerProxy = new MusicPlayerControllerProxy(musicPlayerController);
        }
        return playerControllerProxy;
    }

    @Override
    public boolean onError(IMusicPlayer xmp, int code, String msg) {
        return true;
    }

    @Override
    public boolean onPrepared(IMusicPlayer player) {
        return true;
    }

    @Override
    public boolean onSeekComplete(IMusicPlayer player, long pos) {
        return true;
    }

    @Override
    public boolean onComplete(IMusicPlayer player) {
        return true;
    }

    @Override
    public boolean onBuffering(IMusicPlayer player, boolean buffering, float percentage) {
        return true;
    }

    @Override
    public boolean onProgress(IMusicPlayer player, long pos) {
        return true;
    }

    @Override
    public void onMusicServiceDestroy(IMusicPlayer player) {

    }

    @Override
    public boolean onPlay(IMusicPlayer player) {

        return true;
    }

    @Override
    public boolean onPause(IMusicPlayer player) {
        return true;
    }

    @Override
    public boolean onStop(IMusicPlayer player) {
        return true;
    }
}
