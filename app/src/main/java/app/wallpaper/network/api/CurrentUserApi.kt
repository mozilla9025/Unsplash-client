package app.wallpaper.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface CurrentUserApi {

    @GET("/me")
    fun getCurrentUser(): Observable<Response<ResponseBody>>

}