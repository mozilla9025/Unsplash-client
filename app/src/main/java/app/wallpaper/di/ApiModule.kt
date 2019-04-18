package app.wallpaper.di

import app.wallpaper.network.api.CurrentUserApi
import app.wallpaper.network.api.PhotosApi
import app.wallpaper.network.api.SearchApi
import app.wallpaper.network.api.UserApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
open class ApiModule {

    @Provides
    fun providePhotosApi(retrofit: Retrofit): PhotosApi {
        return retrofit.create(PhotosApi::class.java)
    }

    @Provides
    fun provideCurrentUserApi(retrofit: Retrofit): CurrentUserApi {
        return retrofit.create(CurrentUserApi::class.java)
    }

    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

}