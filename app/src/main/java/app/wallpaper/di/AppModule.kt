package app.wallpaper.di

import android.app.Application
import android.content.Context
import app.wallpaper.app.ApplicationLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class,
    ApiModule::class,
    RepositoryModule::class,
    UseCaseModule::class])
internal class AppModule {

    @Singleton
    @Provides
    fun provideApplication(application: Application): ApplicationLoader = application as ApplicationLoader

    @Singleton
    @Provides
    fun provideApplicationContext(application: Application): Context = application.applicationContext
}