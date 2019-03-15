package com.txl.player.music;

import android.content.Intent;

/**
 * Copyright (c) 2019, 唐小陆 All rights reserved.
 * author：txl
 * date：2019/3/14
 * description：
 */
public interface IMusicPlayerController {
    /**
     *初始化准备播放器相关的数据
     * */
    void init(Intent initParams);
    void playNext();
    void playPre();
    void play();
    void pause();
    void stop();
    boolean isPlaying();
    void destroyPlayer();
    long getPlayPosition();
    long getDuration();
    void seek(long pos);
    MediaNotificationManager getNotificationManager();
    /**
     * 设置播放模式，
     * 单曲循环
     * 列表循环随机播放
     * */
    void setPlayMode(int mode);
    /**
     * 对控制器而言，接收其他未定义的一些页面操作。比如收藏当前播放的音频，
     * */
    void receiveCommand(String action, Object... o);

    void addPlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener);

    void removePlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener);

    /**
     * 获取当前播放器
     * */
    IMusicPlayer getCurrentPlayer();
}
