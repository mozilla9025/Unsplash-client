package app.wallpaper.network.api

import androidx.annotation.IntRange
import androidx.annotation.Nullable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET

interface SearchApi {

    @GET("/search/photos")
    fun searchPhotos(
        @Field("query") query: String,
        @Field("page") @IntRange(from = 1) page: Int,
        @Field("per_page") @IntRange(from = 10) perPage: Int,
        @Field("collections") @Nullable collectionIds: String,
        @Field("orientation") @Nullable orientation: String
    ): Observable<Response<ResponseBody>>

    @GET("/search/collections")
    fun searchCollections(
        @Field("query") query: String,
        @Field("page") @IntRange(from = 1) page: Int,
        @Field("per_page") @IntRange(from = 10) perPage: Int
    ): Observable<Response<ResponseBody>>

    @GET("/search/users")
    fun searchUsers(
        @Field("query") query: String,
        @Field("page") @IntRange(from = 1) page: Int,
        @Field("per_page") @IntRange(from = 10) perPage: Int
    ): Observable<Response<ResponseBody>>
}