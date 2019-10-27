package app.wallpaper.app

import android.content.Context
import app.wallpaper.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ApplicationLoader : DaggerApplication() {

    private lateinit var injector: AndroidInjector<out DaggerApplication>

    override fun onCreate() {
        super.onCreate()
        APPLICATION = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        injector = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return injector
    }

    companion object {

        private lateinit var APPLICATION: ApplicationLoader

        fun get(): ApplicationLoader = APPLICATION
    }
}