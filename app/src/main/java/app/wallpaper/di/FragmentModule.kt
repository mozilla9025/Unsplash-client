package app.wallpaper.di

import app.wallpaper.modules.collections.CollectionsFragment
import app.wallpaper.modules.home.HomeFragment
import app.wallpaper.modules.photo.PhotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeCollectionsFragment(): CollectionsFragment

    @ContributesAndroidInjector
    abstract fun contributePhotoDetailsFragment(): PhotoFragment
}