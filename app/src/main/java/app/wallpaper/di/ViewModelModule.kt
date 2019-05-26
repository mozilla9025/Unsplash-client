package app.wallpaper.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.wallpaper.modules.collections.CollectionsViewModel
import app.wallpaper.modules.home.HomeViewModel
import app.wallpaper.modules.photo.PhotoDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectionsViewModel::class)
    fun bindCollectionsViewModel(viewModel: CollectionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhotoDetailsViewModel::class)
    fun bindPhotoDetailsViewModel(viewModel: PhotoDetailsViewModel): ViewModel

}