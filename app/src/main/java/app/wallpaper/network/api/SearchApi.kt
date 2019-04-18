package app.wallpaper.network.api

import androidx.annotation.IntRange
import androidx.annotation.Nullable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("/search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") @IntRange(from = 1) page: Int,
        @Query("per_page") @IntRange(from = 10) perPage: Int,
        @Query("collections") @Nullable collectionIds: String,
        @Query("orientation") @Nullable orientation: String
    ): Observable<Response<ResponseBody>>

    @GET("/search/collections")
    fun searchCollections(
        @Query("query") query: String,
        @Query("page") @IntRange(from = 1) page: Int,
        @Query("per_page") @IntRange(from = 10) perPage: Int
    ): Observable<Response<ResponseBody>>

    @GET("/search/users")
    fun searchUsers(
        @Query("query") query: String,
        @Query("page") @IntRange(from = 1) page: Int,
        @Query("per_page") @IntRange(from = 10) perPage: Int
    ): Observable<Response<ResponseBody>>
}