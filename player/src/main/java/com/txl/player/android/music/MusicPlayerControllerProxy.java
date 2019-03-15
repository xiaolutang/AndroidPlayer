package com.txl.player.android.music;

import android.content.Intent;
import android.os.Binder;

/**
 * Copyright (c) 2019, 唐小陆 All rights reserved.
 * author：txl
 * date：2019/3/14
 * description：
 */
public class MusicPlayerControllerProxy extends Binder implements IMusicPlayerController {
    private IMusicPlayerController playerController;

    public MusicPlayerControllerProxy(IMusicPlayerController playerController) {
        if(playerController == null){
            throw new RuntimeException( "playerController is null please check" );
        }
        this.playerController = playerController;
    }

    @Override
    public void init(Intent initParams) {
        playerController.init( initParams );
    }

    @Override
    public void play() {
        playerController.play();
    }

    @Override
    public void pause() {
        playerController.play();
    }

    @Override
    public void stop() {
        playerController.stop();
    }

    @Override
    public void destroyPlayer() {
        playerController.destroyPlayer();
    }

    @Override
    public long getPlayPosition() {
        return playerController.getPlayPosition();
    }

    @Override
    public void sendCommand(String action, Object... o) {
        playerController.sendCommand(action,o);
    }


    @Override
    public void addPlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener) {
        playerController.addPlayerEventListener( listener );
    }

    @Override
    public void removePlayerEventListener(IMusicPlayer.IMusicPlayerEvents listener) {
        playerController.removePlayerEventListener( listener );
    }

    @Override
    public IMusicPlayer getCurrentPlayer() {
        return playerController.getCurrentPlayer();
    }


}
