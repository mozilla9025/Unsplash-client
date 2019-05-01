package app.wallpaper.network.controllers

import app.wallpaper.network.api.CurrentUserApi
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

class CurrentUserApiController(
    private val currentUserApi: CurrentUserApi
) {
    fun getCurrentUser(): Observable<Response<ResponseBody>> {
        return currentUserApi.getCurrentUser()
    }
}