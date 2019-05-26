package app.wallpaper.network.api

import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.domain.data.User
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("/users/{username}")
    fun getUser(@Path("username") userName: String): Single<User>

    @GET("/users/{username}/photos")
    fun getPhotos(@Path("username") userName: String): Single<List<Photo>>

    @GET("/users/{username}/likes")
    fun getLikes(
            @Path("username") userName: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int,
            @Query("order_by") orderBy: String
    ): Observable<List<Photo>>

    @GET("/users/{username}/collections")
    fun getUserCollections(
            @Path("username") userName: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Observable<List<PhotoCollection>>

    @GET("/users/unsplash/collections")
    fun getUnsplashCollections(
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Observable<List<PhotoCollection>>
}