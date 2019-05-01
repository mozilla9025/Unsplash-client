package app.wallpaper.network.api

import app.wallpaper.data.Photo
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PhotosApi {

    @GET("/photos")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Observable<List<Photo>>

    @GET("/photos/{id}")
    fun getPhotos(@Path("id") id: String): Observable<List<Photo>>

    @GET("/photos/{id}/download")
    fun getDownloadUrl(@Path("id") id: String): Observable<Response<ResponseBody>>

    //requires write_likes scope
    @POST("/photos/{id}/like")
    fun like(@Path("id") id: String): Observable<Response<ResponseBody>>

    @DELETE("/photos/{id}/like")
    fun unlike(@Path("id") id: String): Observable<Response<ResponseBody>>
}