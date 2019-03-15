package demo;

import android.app.Notification;
import android.content.Context;

import com.txl.player.android.music.INotificationStrategy;
import com.txl.player.android.music.MediaNotificationManager;

/**
 * @author TXL
 * description :
 */
public class NormalMediaNotificationFactory extends MediaNotificationManager {
    public NormalMediaNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createPlayNotification() {
        return null;
    }

    @Override
    public Notification createPauseNotification() {
        return null;
    }

    @Override
    public Notification createSeekNotification(long pos) {
        return null;
    }

    @Override
    public Notification createOtherNotification(String action, Object... o) {
        return null;
    }
}
