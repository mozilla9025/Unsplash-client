package app.wallpaper.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("/users/{username}")
    fun getUser(@Path("username") userName: String): Observable<Response<ResponseBody>>

    @GET("/users/{username}/portfolio")
    fun getPortfolio(@Path("username") userName: String): Observable<Response<ResponseBody>>

    @GET("/users/{username}/photos")
    fun getPhotos(@Path("username") userName: String): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @GET("/users/{username}/likes")
    fun getLikes(
        @Path("username") userName: String,
        @Field("page") page: Int,
        @Field("per_page") perPage: Int,
        @Field("order_by") orderBy: String
    ): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @GET("/users/{username}/collections")
    fun getCollections(
        @Path("username") userName: String,
        @Field("page") page: Int,
        @Field("per_page") perPage: Int
    ): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @GET("/users/{username}/statistics")
    fun getStatistics(
        @Path("username") userName: String,
        @Field("resolution") resolution: String,
        @Field("quantity") quantity: Int
    ): Observable<Response<ResponseBody>>

}