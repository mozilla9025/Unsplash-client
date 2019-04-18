package app.wallpaper.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("/users/{username}")
    fun getUser(@Path("username") userName: String): Observable<Response<ResponseBody>>

    @GET("/users/{username}/portfolio")
    fun getPortfolio(@Path("username") userName: String): Observable<Response<ResponseBody>>

    @GET("/users/{username}/photos")
    fun getPhotos(@Path("username") userName: String): Observable<Response<ResponseBody>>

    @GET("/users/{username}/likes")
    fun getLikes(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Observable<Response<ResponseBody>>

    @GET("/users/{username}/collections")
    fun getCollections(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Observable<Response<ResponseBody>>

    @GET("/users/{username}/statistics")
    fun getStatistics(
        @Path("username") userName: String,
        @Query("resolution") resolution: String,
        @Query("quantity") quantity: Int
    ): Observable<Response<ResponseBody>>

}