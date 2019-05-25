package app.wallpaper.di

import app.wallpaper.network.api.CollectionApi
import app.wallpaper.network.api.CurrentUserApi
import app.wallpaper.network.api.PhotosApi
import app.wallpaper.network.api.UserApi
import app.wallpaper.network.controllers.CollectionApiController
import app.wallpaper.network.controllers.CurrentUserApiController
import app.wallpaper.network.controllers.PhotosApiController
import app.wallpaper.network.controllers.UserApiController
import dagger.Module
import dagger.Provides

@Module
class ApiControllerModule {

    @Provides
    fun provideCurrentUserApiController(api: CurrentUserApi): CurrentUserApiController {
        return CurrentUserApiController(api)
    }

    @Provides
    fun providePhotosApiController(api: PhotosApi): PhotosApiController {
        return PhotosApiController(api)
    }

    @Provides
    fun provideCollectionApiController(api: CollectionApi): CollectionApiController {
        return CollectionApiController(api)
    }

    @Provides
    fun provideUserApiController(api: UserApi): UserApiController {
        return UserApiController(api)
    }
}