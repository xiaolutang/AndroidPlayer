package com.txl.player.android.music;

/**
 * @author TXL
 * description :音频播放ui相关
 */
public interface IPlayerUi <T>{
    /**
     * 展示暂停状态
     * */
    void uiPause();
    /**
     * 展示播放状态
     * */
    void uiPlay();

    void updateProgress(long position);

    void changePlayerUiData(T data);
}
