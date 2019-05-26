package app.wallpaper.domain.repo.impl

import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.domain.data.User
import app.wallpaper.domain.repo.UserRepository
import app.wallpaper.network.api.UserApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userApi: UserApi) : UserRepository {
    override fun getUser(userName: String): Single<User> {
        return userApi.getUser(userName)
                .subscribeOn(Schedulers.io())
    }

    override fun getPhotos(userName: String): Single<List<Photo>> {
        return userApi.getPhotos(userName)
                .subscribeOn(Schedulers.io())
    }

    override fun getLikes(userName: String, page: Int, perPage: Int, orderBy: String): Observable<List<Photo>> {
        return userApi.getLikes(userName, page, perPage, orderBy)
                .subscribeOn(Schedulers.io())
    }

    override fun getUserCollections(userName: String, page: Int, perPage: Int): Observable<List<PhotoCollection>> {
        return userApi.getUserCollections(userName, page, perPage)
                .subscribeOn(Schedulers.io())
    }

    override fun getUnsplashCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>> {
        return userApi.getUnsplashCollections(page, perPage)
                .subscribeOn(Schedulers.io())
    }
}