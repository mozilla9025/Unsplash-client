package app.wallpaper.di

import app.wallpaper.domain.repo.CollectionsRepository
import app.wallpaper.domain.repo.PhotoRepository
import app.wallpaper.domain.repo.impl.CollectionsRepositoryImpl
import app.wallpaper.domain.repo.impl.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindPhotoRepository(repository: PhotoRepositoryImpl): PhotoRepository

    @Binds
    fun bindCollectionRepository(repository: CollectionsRepositoryImpl): CollectionsRepository

}