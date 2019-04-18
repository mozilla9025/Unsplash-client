package app.wallpaper.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application = app
}
