package app.wallpaper.di

import app.wallpaper.domain.usecase.GetCollectionsUseCase
import app.wallpaper.domain.usecase.GetPhotosUseCase
import app.wallpaper.domain.usecase.impl.GetCollectionsUseCaseImpl
import app.wallpaper.domain.usecase.impl.GetPhotosUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {

    @Binds
    fun bindGetPhotoUseCase(useCase: GetPhotosUseCaseImpl): GetPhotosUseCase

    @Binds
    fun bindGetCollectionUseCase(useCase: GetCollectionsUseCaseImpl): GetCollectionsUseCase
}