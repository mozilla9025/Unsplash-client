package app.wallpaper.di

import app.wallpaper.network.api.CurrentUserApi
import app.wallpaper.network.api.PhotosApi
import app.wallpaper.network.controllers.CurrentUserApiController
import app.wallpaper.network.controllers.PhotosApiController
import dagger.Module
import dagger.Provides

@Module
open class ApiControllerModule {

    @Provides
    fun provideCurrentUserApiController(api: CurrentUserApi): CurrentUserApiController {
        return CurrentUserApiController(api)
    }

    @Provides
    fun providePhotosApiController(api: PhotosApi): PhotosApiController {
        return PhotosApiController(api)
    }
}