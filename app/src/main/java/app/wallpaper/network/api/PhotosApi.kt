package app.wallpaper.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

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

    @GET("/photos/{id}/download")
    fun getDownloadUrl(@Path("id") id: String): Observable<Response<ResponseBody>>

    //requires write_likes scope
    @POST("/photos/{id}/like")
    fun like(@Path("id") id: String): Observable<Response<ResponseBody>>

    @DELETE("/photos/{id}/like")
    fun unlike(@Path("id") id: String): Observable<Response<ResponseBody>>

/*
    @FormUrlEncoded
    @GET("/photos/random")
    fun getRandom(@): Observable<Response<ResponseBody>>
    collections
    featured
    username
    query
    orientation
    count
*/

}