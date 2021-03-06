package com.txl.player.music;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author TXL
 * description :音频播放服务
 */
public abstract class AbsMusicPlayerService extends Service implements IMusicPlayer.IMusicPlayerEvents {
    protected final String TAG = getClass().getSimpleName();

    protected boolean showNotification = true;
    IMusicPlayerController musicPlayerController;
    MusicPlayerControllerProxy playerControllerProxy;
    protected AbsNotificationFactory notificationManager;

    public AbsMusicPlayerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayerController =  createPlayerController();
        musicPlayerController.addPlayerEventListener( this );
        notificationManager = musicPlayerController.getNotificationManager();
        playerControllerProxy = new MusicPlayerControllerProxy(musicPlayerController);
    }



    protected abstract IMusicPlayerController createPlayerController();

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(playerControllerProxy == null){
            playerControllerProxy = new MusicPlayerControllerProxy(musicPlayerController);
        }
        return playerControllerProxy;
    }

    @Override
    public void onDestroy() {
        musicPlayerController.destroyPlayer();
        musicPlayerController =null;
        super.onDestroy();
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
        Notification notification = notificationManager.createPlayNotification();
        return checkAndStartNotification(notification, "onSeekComplete notification is null");
    }

    @Override
    public boolean onComplete(IMusicPlayer player) {
        return checkAndStartNotification(notificationManager.createPauseNotification(), "onComplete notification is null");
    }

    private boolean checkAndStartNotification(Notification notification, String s) {
        if(!showNotification){
            return true;
        }
        if (notification == null) {
            Log.e(TAG, s);
            return true;
        }
        startForeground(notificationManager.getNotificationId(), notification);

        return true;
    }

    @Override
    public boolean onBuffering(IMusicPlayer player, boolean buffering, float percentage) {
        return true;
    }

    @Override
    public boolean onProgress(IMusicPlayer player, long pos, long total) {

        return checkAndStartNotification(notificationManager.createSeekNotification(pos), "onProgress notification is null");
    }

    @Override
    public void onMusicServiceDestroy(IMusicPlayer player) {
        stopForeground(true);
    }

    @Override
    public boolean onPlay(IMusicPlayer player) {
        return checkAndStartNotification(notificationManager.createPlayNotification(), "onPlay notification is null");
    }

    @Override
    public boolean onPause(IMusicPlayer player) {
        return checkAndStartNotification( notificationManager.createPauseNotification(), "onPause notification is null");
    }

    @Override
    public boolean onStop(IMusicPlayer player) {
        return checkAndStartNotification( notificationManager.createPauseNotification(), "onPause notification is null");
    }
}
