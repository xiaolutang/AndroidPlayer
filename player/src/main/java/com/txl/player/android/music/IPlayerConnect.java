package com.txl.player.android.music;

/**
 * @author TXL
 * description :
 */
public interface IPlayerConnect<T> {
    void init(T params);

    void registerPlayerEventListenser(IMusicPlayer.IMusicPlayerEvents listener);

    void ungisterPlayerEventListenser();
}
