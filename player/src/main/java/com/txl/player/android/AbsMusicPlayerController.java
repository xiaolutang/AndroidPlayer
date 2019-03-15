package com.txl.player.android;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.txl.player.android.music.AbsPlayerService;
import com.txl.player.android.music.IMusicPlayer;
import com.txl.player.android.music.IPlayerUi;
import com.txl.player.android.music.PlayerTag;

import java.lang.ref.WeakReference;

/**
 * @author TXL
 * description :
 */
public abstract class AbsMusicPlayerController{
    protected final String TAG = getClass().getSimpleName();
    WeakReference<Context> _mContext;
    AbsPlayerService.PlayerAdapter _mMusicPlayer;
    ServiceConnection serviceConnection;
    IPlayerUi _mPlayerUiChangeListener;
    MusicPlayerEvents iMusicPlayerEvents;

    /**
     * @param serviceClass 服务的class
     * */
    public AbsMusicPlayerController(Context context,Class<?extends Service> serviceClass) {
        _mContext = new WeakReference<>(context);
        iMusicPlayerEvents = new MusicPlayerEvents(this);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if(service instanceof IMusicPlayer){
                    _mMusicPlayer = (AbsPlayerService.PlayerAdapter) service;
                    _mMusicPlayer.setEventListener(iMusicPlayerEvents);
                    serviceConnect();
                }else {
                    Log.e(TAG,"serviceConnection _mMusicPlayer init error unBind service");
                    Context c = _mContext.get();
                    if (c != null){
                        c.unbindService(this);
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                _mMusicPlayer.removeEventListener(iMusicPlayerEvents);
            }
        };
        bindService(context,serviceClass);
    }

    private void bindService(Context context,Class<?extends Service> serviceClass){
        Intent intent = getBindServiceIntent();
        if(intent == null){
            intent = new Intent(  );
        }
        intent.setClass( context, serviceClass);
        context.bindService( intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 绑定服务所需要的Intent
     * */
    protected abstract Intent getBindServiceIntent();

    protected void serviceConnect(){

    }

    protected void initPlayer(PlayerTag playTag){
        //两次tag相同就认为播放的是同一个音频
        if(!playTag.equals(_mMusicPlayer.getPlayTag())){
            _mMusicPlayer.init();
        }
        _mMusicPlayer.startNotification( true );
    }

    protected PlayerTag getPlayTag(){
        return _mMusicPlayer.getPlayTag();
    }

    protected boolean isPlay(){
        if(_mMusicPlayer != null){
            return _mMusicPlayer.isPlaying();
        }
        return false;
    }

    public void setPlayerUiChangeListener(IPlayerUi _mPlayerUiChangeListener) {
        this._mPlayerUiChangeListener = _mPlayerUiChangeListener;
    }

    protected void open(String url,PlayerTag playerTag){
        _mMusicPlayer.setPlayTag( playerTag );
        _mMusicPlayer.open( url );
    }

    public void play(){
        if(_mMusicPlayer!=null){
            _mMusicPlayer.play();
        }
    }

    public void pause(){
        if(_mMusicPlayer!=null){
            _mMusicPlayer.pause();
        }
    }

    public void stop(){
        if(_mMusicPlayer!=null){
            _mMusicPlayer.stop();
        }
    }

    public void togglePlay(){
        if(_mMusicPlayer!=null){
            if(_mMusicPlayer.isPlaying()){
                pause();
            }else {
                play();
            }
        }
    }

    public void seek(long pos){
        if(_mMusicPlayer!=null){
            _mMusicPlayer.seekTo( pos );
        }
    }

    public abstract void playNext();

    public abstract void playPre();

    /**
     * 播放页面销毁，销毁对应的控制器
     * */
    public void destroy(){
        Context context = _mContext.get();
        if(context != null){
            context.unbindService( serviceConnection );
        }
        _mContext.clear();
        _mContext = null;
        iMusicPlayerEvents.destroy();
        iMusicPlayerEvents = null;
        _mPlayerUiChangeListener = null;
        _mMusicPlayer = null;
        serviceConnection = null;
    }

    private static class MusicPlayerEvents implements IMusicPlayer.IMusicPlayerEvents{
        AbsMusicPlayerController musicPlayerController;
        public MusicPlayerEvents(AbsMusicPlayerController musicPlayerController) {
            this.musicPlayerController = musicPlayerController;
        }

        @Override
        public boolean onError(IMusicPlayer xmp, int code, String msg) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController._mPlayerUiChangeListener.uiPause();
            }
            return false;
        }

        @Override
        public boolean onPrepared(IMusicPlayer player) {
            return false;
        }

        @Override
        public boolean onSeekComplete(IMusicPlayer player, long pos) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController._mPlayerUiChangeListener.uiPlay();
            }
            return false;
        }

        @Override
        public boolean onComplete(IMusicPlayer player) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController._mPlayerUiChangeListener.uiPause();
            }
            return false;
        }

        @Override
        public boolean onBuffering(IMusicPlayer player, boolean buffering, float percentage) {
            return false;
        }

        @Override
        public boolean onProgress(IMusicPlayer player, long pos) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController. _mPlayerUiChangeListener.updateProgress(pos);
            }
            return false;
        }

        @Override
        public void onMusicServiceDestroy(IMusicPlayer player) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController._mPlayerUiChangeListener.uiPause();
            }
        }

        @Override
        public boolean onPlay(IMusicPlayer player) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController._mPlayerUiChangeListener.uiPlay();
            }
            return false;
        }

        @Override
        public boolean onPause(IMusicPlayer player) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController._mPlayerUiChangeListener.uiPause();
            }
            return false;
        }

        @Override
        public boolean onStop(IMusicPlayer player) {
            if(musicPlayerController._mPlayerUiChangeListener != null){
                musicPlayerController._mPlayerUiChangeListener.uiPause();
            }
            return false;
        }

        @Override
        public boolean onReceiveControllerCommand(String action, Object... o) {
            return false;
        }

        public void destroy(){
            musicPlayerController = null;
        }
    }
}
