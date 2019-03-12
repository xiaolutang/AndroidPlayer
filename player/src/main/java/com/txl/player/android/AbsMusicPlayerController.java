package com.txl.player.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.txl.player.android.music.AbsPlayerService;
import com.txl.player.android.music.IMusicPlayer;
import com.txl.player.android.music.IPlayerUi;

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

    public AbsMusicPlayerController(Context context) {
        _mContext = new WeakReference<>(context);
        iMusicPlayerEvents = new MusicPlayerEvents(this);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if(service instanceof IMusicPlayer){
                    _mMusicPlayer = (AbsPlayerService.PlayerAdapter) service;
                    _mMusicPlayer.setEventListener(iMusicPlayerEvents);
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
    }

    protected void initPlayer(Object playTag){
        //两次tag相同就认为播放的是同一个音频
        if(!playTag.equals(_mMusicPlayer.getPlayTag())){
            _mMusicPlayer.init();
            _mMusicPlayer.startNotification( true );
        }
    }

    public void setPlayerUiChangeListener(IPlayerUi _mPlayerUiChangeListener) {
        this._mPlayerUiChangeListener = _mPlayerUiChangeListener;
    }


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

        public void destroy(){
            musicPlayerController = null;
        }
    }
}
