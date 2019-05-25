package app.wallpaper.network.controllers

import app.wallpaper.domain.data.Collection
import app.wallpaper.domain.data.User
import app.wallpaper.network.api.UserApi
import io.reactivex.Observable

class UserApiController(private val api: UserApi) {
    fun getUser(userName: String): Observable<User> = api.getUser(userName)

    fun getUnsplashCollections(
            page: Int,
            perPage: Int
    ): Observable<List<Collection>> = api.getUnsplashCollections(page, perPage)
}