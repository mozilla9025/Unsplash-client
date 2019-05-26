package app.wallpaper.di

import app.wallpaper.domain.repo.CollectionsRepository
import app.wallpaper.domain.repo.PhotoRepository
import app.wallpaper.domain.repo.UserRepository
import app.wallpaper.domain.repo.impl.CollectionsRepositoryImpl
import app.wallpaper.domain.repo.impl.PhotoRepositoryImpl
import app.wallpaper.domain.repo.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindPhotoRepository(repository: PhotoRepositoryImpl): PhotoRepository

    @Binds
    fun bindCollectionRepository(repository: CollectionsRepositoryImpl): CollectionsRepository

    @Binds
    fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

}