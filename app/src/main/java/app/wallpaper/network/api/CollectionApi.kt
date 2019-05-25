package app.wallpaper.network.api

import app.wallpaper.domain.data.Collection
import app.wallpaper.domain.data.Photo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectionApi {

    @GET("/collections")
    fun getCollections(
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Observable<List<Collection>>

    @GET("/collections/featured")
    fun getFeaturedCollections(
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Observable<List<Collection>>

    @GET("/collections/curated")
    fun getCuratedCollections(
            @Query("page") page: Int,
            @Query("per_page") perPage: Int
    ): Observable<List<Collection>>

    @GET("/collections/{id}")
    fun getCollection(
            @Path("id") id: Int
    ): Observable<Collection>

    @GET("/collections/{id}/photos")
    fun getCollectionPhotos(
            @Path("id") id: Int
    ): Observable<List<Photo>>

    @GET("/collections/{id}/related")
    fun getRelatedCollections(
            @Path("id") id: Int
    ): Observable<List<Collection>>

}