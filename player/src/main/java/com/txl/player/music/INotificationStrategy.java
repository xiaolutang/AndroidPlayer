package com.txl.player.music;

import android.app.Notification;

/**
 * Copyright (c) 2019, 唐小陆 All rights reserved.
 * author：txl
 * date：2019/3/14
 * description：
 */
public interface INotificationStrategy {

    int getNotificationId();

    Notification createPlayNotification();
    Notification createPauseNotification();
    /**
     * 进度改变
     * */
    Notification createSeekNotification(long pos);
    /**
     * 
     * */
    Notification createOtherNotification(String action, Object... o);
}
