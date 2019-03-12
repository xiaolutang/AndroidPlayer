package com.txl.player.android.music;

/**
 * @author TXL
 * description :音频播放ui相关
 */
public interface IPlayerUi {
    /**
     * 展示暂停状态
     * */
    void uiPause();
    /**
     * 展示播放状态
     * */
    void uiPlay();

    void updateProgress(long position);
}
