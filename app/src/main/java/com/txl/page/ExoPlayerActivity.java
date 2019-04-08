package com.txl.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.BasePlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.txl.android.player.R;
import com.txl.player.video.ExoPlayerManager;

public class ExoPlayerActivity extends AppCompatActivity {

    PlayerView playerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
    }

    private void initView(){
        BasePlayer player = ExoPlayerManager.getExoPlayer(this);
        playerView.setPlayer(player);
    }

}
