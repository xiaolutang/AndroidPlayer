package com.txl.player.video;

import android.content.Context;

import com.google.android.exoplayer2.BasePlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;

/**
 * @author TXL
 * description :集成谷歌官方的ExoPlayer github 地址： https://github.com/google/ExoPlayer
 */
public class ExoPlayerManager {

    public static BasePlayer getExoPlayer(Context context){
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);
        return player;
    }
}
