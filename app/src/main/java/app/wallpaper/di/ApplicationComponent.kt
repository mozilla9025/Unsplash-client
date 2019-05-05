package app.wallpaper.di

import app.wallpaper.modules.collections.CollectionsViewModel
import app.wallpaper.modules.home.HomeViewModel
import app.wallpaper.modules.home.PhotoDataSource
import app.wallpaper.modules.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ApiModule::class, ApiControllerModule::class])
interface ApplicationComponent {
    fun inject(viewModel: MainViewModel)
    fun inject(viewModel: CollectionsViewModel)


    fun inject(dataSource: PhotoDataSource)
}