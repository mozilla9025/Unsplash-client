package app.wallpaper.modules;

import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import app.wallpaper.R;

public final class MainActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
    }
}
