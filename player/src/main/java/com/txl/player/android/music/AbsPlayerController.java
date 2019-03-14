package com.txl.player.android.music;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TXL
 * description :
 */
public abstract class AbsPlayerController implements IMusicPlayerController, IMusicPlayer.IMusicPlayerEvents {
    protected final String TAG = getClass().getSimpleName();
    MediaNotificationStrategy notificationManager;
    Context _mContext;
    IMusicPlayer _musicPlayer;
    List<IMusicPlayer.IMusicPlayerEvents> _eventsList;

    public AbsPlayerController(Context context) {
        _mContext = context;
        _eventsList = new ArrayList<>(  );
        _musicPlayer = createMusicPlayer();
        _musicPlayer.setEventListener( this );
        notificationManager = new MediaNotificationStrategy(context){};
    }

    protected IMusicPlayer createMusicPlayer() {
        return new AndroidMusicPlayer( _mContext, true, true);
    }

    public MediaNotificationStrategy getNotificationManager() {
        return notificationManager;
    }

    @Override
    public void play() {
        _musicPlayer.play();
    }

    @Override
    public void pause() {
        _musicPlayer.pause();
    }

    @Override
    public void stop() {
        _musicPlayer.stop();
    }

    @Override
    public void destroyPlayer() {
        _musicPlayer.destroy();
    }

    @Override
    public void addPlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener) {
        if(listener != null){
            _eventsList.add( listener );
        }
    }

    @Override
    public void removePlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener) {
        if(listener != null){
            _eventsList.remove( listener );
        }
    }

    @Override
    public boolean onError(IMusicPlayer player, int code, String msg) {
        Log.d( TAG,"MusicPlayer onError code: "+code +" msg : "+msg);
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onError( player,code,msg );
        }
        return true;
    }

    @Override
    public boolean onPrepared(IMusicPlayer player) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onPrepared( player );
        }
        return true;
    }

    @Override
    public boolean onSeekComplete(IMusicPlayer player, long pos) {
        Log.i( TAG,"MusicPlayer onSeekComplete pos: "+pos );
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onSeekComplete( player,pos );
        }
        return true;
    }

    @Override
    public boolean onComplete(IMusicPlayer player) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onComplete( player );
        }
        return true;
    }

    @Override
    public boolean onBuffering(IMusicPlayer player, boolean buffering, float percentage) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onBuffering( player ,buffering,percentage);
        }
        return true;
    }

    @Override
    public boolean onProgress(IMusicPlayer player, long pos) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onProgress( player ,pos);
        }
        return true;
    }

    @Override
    public void onMusicServiceDestroy(IMusicPlayer player) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onMusicServiceDestroy( player);
        }
    }

    @Override
    public boolean onPlay(IMusicPlayer player) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onPlay( player);
        }
        return true;
    }

    @Override
    public boolean onPause(IMusicPlayer player) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onPause( player);
        }
        return true;
    }

    @Override
    public boolean onStop(IMusicPlayer player) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onStop( player);
        }
        return true;
    }
}
