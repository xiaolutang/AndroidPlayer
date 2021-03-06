package com.txl.player.music;

/**
 * @author TXL
 * description :
 */
public interface IMusicPlayer {
    void init();

    long getCurrentPosition();

    boolean seekTo(long pos);

    boolean stop();

    boolean pause();

    boolean play();

    boolean open(String url);

    void loop(boolean loop);

    long getDuration();

    void destroy();

    void setEventListener(IMusicPlayerEvents listener);

    void removeEventListener(IMusicPlayerEvents listener);

    boolean isPlaying();

    /**
     * 返回判断是不是同一音频的标记
     * */
    PlayerTag getPlayTag();

    void setPlayTag(PlayerTag tag);

    /**
     * description :音频播放需要通过Notification操作需要及时更改界面
     */
    interface IMusicPlayerEvents {
        boolean onError(IMusicPlayer xmp, int code, String msg);

        boolean onPrepared(IMusicPlayer player);

        boolean onSeekComplete(IMusicPlayer player, long pos);

        boolean onComplete(IMusicPlayer player);

        boolean onBuffering(IMusicPlayer player, boolean buffering, float percentage);

        boolean onProgress(IMusicPlayer player, long pos, long total);

        /**
         * 音频服务destroy
         * */
        void onMusicServiceDestroy(IMusicPlayer player);

        boolean onPlay(IMusicPlayer player);

        boolean onPause(IMusicPlayer player);

        boolean onStop(IMusicPlayer player);

        /**
         * 接收controller发出的一些事件
         * */
        boolean onReceiveControllerCommand(String action, Object... o);
    }
}
