package com.txl.android.player.music;

/**
 * Copyright (c) 2019, 唐小陆 All rights reserved.
 * author：txl
 * date：2019/3/11
 * description：播放器的标志器
 */
public class PlayerTag {
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(obj instanceof String){
            return this.toString().equals( obj );
        }
        return this.toString().equals( obj.toString() );
    }

    @Override
    public String toString() {
        return url;
    }
}
