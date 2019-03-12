package activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.txl.android.player.R;
import com.txl.player.android.AbsMusicPlayerController;
import com.txl.player.android.music.IPlayerUi;

public class MainActivity extends AppCompatActivity implements IPlayerUi {
    AbsMusicPlayerController musicPlayerController;

    /**
     * 名字
     * */
    TextView tvMusicName;
    ImageView imagePreMusic;
    ImageView imageNextMusic;
    ImageView imageToggleMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initView();
        musicPlayerController = new MusicPlayerController(this);
        musicPlayerController.setPlayerUiChangeListener(this);
    }

    private void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicPlayerController.destroy();
        musicPlayerController = null;
    }

    @Override
    public void uiPause() {

    }

    @Override
    public void uiPlay() {

    }

    @Override
    public void updateProgress(long position) {

    }

    class MusicPlayerController extends AbsMusicPlayerController{

        public MusicPlayerController(Context context) {
            super(context);
        }
    }
}
