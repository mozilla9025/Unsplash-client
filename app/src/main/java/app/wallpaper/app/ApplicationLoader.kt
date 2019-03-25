package app.wallpaper.app

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import app.wallpaper.di.ApplicationComponent

class ApplicationLoader : Application() {
//    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()
    }

    fun setup() {
//        applicationComponent = DaggerApplicationComponent.builder()
//            .appModule(new AppModule(this))
//            .netModule(new NetModule())
//            .realmModule(new RealmModule(this))
//            .build();
    }

//    fun getApplicationComponent(): ApplicationComponent {
//        return applicationComponent
//    }

    companion object {
        lateinit var instance: ApplicationLoader private set
    }
}
