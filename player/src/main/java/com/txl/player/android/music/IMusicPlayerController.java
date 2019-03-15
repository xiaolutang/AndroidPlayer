package com.txl.player.android.music;

import android.content.Intent;

/**
 * Copyright (c) 2019, 唐小陆 All rights reserved.
 * author：txl
 * date：2019/3/14
 * description：
 */
interface IMusicPlayerController {
    void init(Intent initParams);
    void play();
    void pause();
    void stop();
    void destroyPlayer();
    long getPlayPosition();
    /**
     * 发送命令做一些自定义操作
     * */
    void sendCommand(String action, Object... o);

    void addPlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener);

    void removePlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener);

    /**
     * 获取当前播放器
     * */
    IMusicPlayer getCurrentPlayer();
}
