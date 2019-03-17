package com.txl.demo;


import com.txl.player.music.AbsMusicPlayerService;
import com.txl.player.music.IMusicPlayerController;

/**
 * Copyright (c) 2019, 唐小陆 All rights reserved.
 * author：txl
 * date：2019/3/11
 */
public class AndroidMusicPlayerService extends AbsMusicPlayerService {

    @Override
    protected IMusicPlayerController createPlayerController() {
        return new DemoMusicPlayerController(this);
    }

    @Override
    public boolean
    onReceiveControllerCommand(String action, Object... o) {
        notificationManager.createOtherNotification(action,o);
        return false;
    }
}
