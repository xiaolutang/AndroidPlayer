package com.txl.player.android.music;

/**
 * @author TXL
 * description :
 */
public class AbsPlayerController implements IMusicPlayer.IMusicPlayerEvents{

    IMusicPlayer musicPlayer;

    /**
     * @param action 不同的操作
     * */
     void sendCommand(String action){}

    @Override
    public boolean onError(IMusicPlayer xmp, int code, String msg) {
        return false;
    }

    @Override
    public boolean onPrepared(IMusicPlayer player) {
        return false;
    }

    @Override
    public boolean onSeekComplete(IMusicPlayer player, long pos) {
        return false;
    }

    @Override
    public boolean onComplete(IMusicPlayer player) {
        return false;
    }

    @Override
    public boolean onBuffering(IMusicPlayer player, boolean buffering, float percentage) {
        return false;
    }

    @Override
    public boolean onProgress(IMusicPlayer player, long pos) {
        return false;
    }

    @Override
    public void onMusicServiceDestroy(IMusicPlayer player) {

    }

    @Override
    public boolean onPlay(IMusicPlayer player) {
        return false;
    }

    @Override
    public boolean onPause(IMusicPlayer player) {
        return false;
    }

    @Override
    public boolean onStop(IMusicPlayer player) {
        return false;
    }
}
