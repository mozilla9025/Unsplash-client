package app.wallpaper.domain.repo

import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.domain.data.User
import io.reactivex.Observable
import io.reactivex.Single

interface  UserRepository {

    fun getUser(userName: String): Single<User>

    fun getPhotos(userName: String): Single<List<Photo>>

    fun getLikes(userName: String, page: Int, perPage: Int, orderBy: String): Observable<List<Photo>>

    fun getUserCollections(userName: String, page: Int, perPage: Int): Observable<List<PhotoCollection>>

    fun getUnsplashCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>>
}