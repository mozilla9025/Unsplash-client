package app.wallpaper.di

import app.wallpaper.network.api.*
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

    @Provides
    fun provideCollectionApi(retrofit: Retrofit): CollectionApi {
        return retrofit.create(CollectionApi::class.java)
    }

}