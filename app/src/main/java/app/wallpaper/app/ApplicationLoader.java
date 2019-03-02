package app.wallpaper.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;


public final class ApplicationLoader extends Application {

    public static Context applicationContext;
    public static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = this;
        handler = new Handler(Looper.getMainLooper());
    }
}
