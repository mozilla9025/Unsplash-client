package app.wallpaper.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface PhotosApi {

    @FormUrlEncoded
    @GET("/photos")
    fun getPhotos(
        @Field("page") page: Int,
        @Field("per_page") perPage: Int,
        @Field("order_by") orderBy: String
    ): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @GET("/photos/{id}")
    fun getPhotos(@Path("id") id: String): Observable<Response<ResponseBody>>



}