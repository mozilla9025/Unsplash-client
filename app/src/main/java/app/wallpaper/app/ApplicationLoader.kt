package app.wallpaper.app

import android.content.Context
import app.wallpaper.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ApplicationLoader : DaggerApplication() {

    private lateinit var injector: AndroidInjector<out DaggerApplication>

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        injector = DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return injector
    }
}