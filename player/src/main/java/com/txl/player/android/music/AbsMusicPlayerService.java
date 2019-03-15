package com.txl.player.android.music;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * @author TXL
 * description :音频播放服务
 */
public abstract class AbsMusicPlayerService extends Service implements IMusicPlayer.IMusicPlayerEvents {
    protected final String TAG = getClass().getSimpleName();

    final int MESSAGE_UPDATE_TIME = 0x01;
    /**
     * 每500毫秒查询一次
     * */
    protected int checkCurrentPositionDelayTime = 500;

    protected boolean showNotification = true;
    IMusicPlayerController musicPlayerController;
    MusicPlayerControllerProxy playerControllerProxy;
    protected MediaNotificationManager notificationManager;
    private Handler handler = new Handler(getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_UPDATE_TIME:
                    updateTime();
                    return;
                default:
                    if(handleOtherMessage(msg)){
                        return;
                    }
            }
            super.handleMessage(msg);
        }
    };

    protected void updateTime(){
        long position = playerControllerProxy.getPlayPosition();
        onProgress(musicPlayerController.getCurrentPlayer(),position);
        handler.sendEmptyMessageDelayed(MESSAGE_UPDATE_TIME, checkCurrentPositionDelayTime);
    }

    protected boolean handleOtherMessage(Message msg){
        return false;
    }

    public AbsMusicPlayerService() {
        musicPlayerController =  createPlayerController();
        musicPlayerController.addPlayerEventListener( this );
        playerControllerProxy = new MusicPlayerControllerProxy(musicPlayerController);
        notificationManager = getNotificationManager();
    }

    abstract IMusicPlayerController createPlayerController();
    abstract MediaNotificationManager getNotificationManager();

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
        notificationManager.removeNotification();
        handler.removeMessages(MESSAGE_UPDATE_TIME);
        return true;
    }

    @Override
    public boolean onPrepared(IMusicPlayer player) {
        return true;
    }

    @Override
    public boolean onSeekComplete(IMusicPlayer player, long pos) {
        Notification notification = notificationManager.createPlayNotification();
        updateTime();
        return checkAndStartNotification(notification, "onSeekComplete notification is null");
    }

    @Override
    public boolean onComplete(IMusicPlayer player) {
        handler.removeMessages(MESSAGE_UPDATE_TIME);
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
    public boolean onProgress(IMusicPlayer player, long pos) {
        return checkAndStartNotification(notificationManager.createSeekNotification(pos), "onProgress notification is null");
    }

    @Override
    public void onMusicServiceDestroy(IMusicPlayer player) {
        handler.removeMessages(MESSAGE_UPDATE_TIME);
        stopForeground(true);
    }

    @Override
    public boolean onPlay(IMusicPlayer player) {
        handler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
        return checkAndStartNotification(notificationManager.createPlayNotification(), "onPlay notification is null");
    }

    @Override
    public boolean onPause(IMusicPlayer player) {
        handler.removeMessages(MESSAGE_UPDATE_TIME);
        return checkAndStartNotification( notificationManager.createPauseNotification(), "onPause notification is null");
    }

    @Override
    public boolean onStop(IMusicPlayer player) {
        handler.removeMessages(MESSAGE_UPDATE_TIME);
        return checkAndStartNotification( notificationManager.createPauseNotification(), "onPause notification is null");
    }
}
