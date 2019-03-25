package app.wallpaper.app

import android.app.Application
import app.wallpaper.di.ApplicationComponent
import app.wallpaper.di.DaggerApplicationComponent
import app.wallpaper.di.NetworkModule

class ApplicationLoader : Application() {
    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this

        applicationComponent = DaggerApplicationComponent.builder()
            .networkModule(NetworkModule())
            .build()
    }

    companion object {
        lateinit var instance: ApplicationLoader private set
    }
}
