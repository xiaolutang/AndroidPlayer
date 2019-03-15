package com.txl.player.android.music;

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
    private MediaNotificationManager notificationManager;
    private Context _mContext;
    private List<IMusicPlayer.IMusicPlayerEvents> _eventsList;

    protected IMusicPlayer _musicPlayer;

    public AbsPlayerController(Context context) {
        _mContext = context;
        _eventsList = new ArrayList<>(  );
        _musicPlayer = createMusicPlayer();
        _musicPlayer.setEventListener( this );
        _musicPlayer.init();
        notificationManager = createMediaNotificationManager(context);
    }

    protected IMusicPlayer createMusicPlayer() {
        return new AndroidMusicPlayer( _mContext, true, true);
    }

    public abstract MediaNotificationManager createMediaNotificationManager(Context context);


    protected boolean open(String url){
        return _musicPlayer.open(url);
    }

    protected boolean open(String url,PlayerTag playTag){
        setPlayTag(playTag);
        return _musicPlayer.open(url);
    }

    protected void setPlayTag(PlayerTag playTag){
        _musicPlayer.setPlayTag(playTag);
    }

    protected PlayerTag getPlayTag(){
        return _musicPlayer.getPlayTag();
    }

    @Override
    public boolean isPlaying() {
        return _musicPlayer.isPlaying();
    }

    @Override
    public long getPlayPosition() {
        if(_musicPlayer.isPlaying()){
            return _musicPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public IMusicPlayer getCurrentPlayer() {
        return _musicPlayer;
    }

    @Override
    public void play() {
        onPlay(_musicPlayer);
        _musicPlayer.play();
    }

    @Override
    public void pause() {
        onPause(_musicPlayer);
        _musicPlayer.pause();
    }

    @Override
    public void stop() {
        onStop(_musicPlayer);
        _musicPlayer.stop();
    }

    @Override
    public void destroyPlayer() {
        _musicPlayer.destroy();
        _musicPlayer = null;
        _eventsList.clear();
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

    @Override
    public boolean onReceiveControllerCommand(String action, Object... o) {
        for (IMusicPlayer.IMusicPlayerEvents event:_eventsList){
            event.onReceiveControllerCommand( action,o);
        }
        return true;
    }
}
