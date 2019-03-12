package model;

/**
 * Copyright (c) 2019, 唐小陆 All rights reserved.
 * author：txl
 * date：2019/3/12
 * description：
 */
public class MusicData {
    int playerUserIconResId = -1;
    String musicName;
    String playUrl;

    public MusicData() {
    }

    public MusicData(int playerUserIconResId, String musicName, String playUrl) {
        this.playerUserIconResId = playerUserIconResId;
        this.musicName = musicName;
        this.playUrl = playUrl;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getPlayerUserIconResId() {
        return playerUserIconResId;
    }

    public void setPlayerUserIconResId(int playerUserIconResId) {
        this.playerUserIconResId = playerUserIconResId;
    }
}
