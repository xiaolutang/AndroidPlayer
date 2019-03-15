package demo;


import android.content.Context;

import com.txl.player.android.music.AbsMusicPlayerService;
import com.txl.player.android.music.IMusicPlayerController;
import com.txl.player.android.music.MediaNotificationManager;

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
    protected MediaNotificationManager getNotificationManager(Context context) {
        return new MediaNotificationManager(context) {
        };
    }

    @Override
    public boolean onReceiveControllerCommand(String action, Object... o) {
        notificationManager.createOtherNotification(action,o);
        return false;
    }
}
