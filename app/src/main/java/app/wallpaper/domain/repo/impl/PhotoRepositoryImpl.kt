package app.wallpaper.domain.repo.impl

import app.wallpaper.domain.data.Order
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.repo.PhotoRepository
import app.wallpaper.network.api.CollectionApi
import app.wallpaper.network.api.PhotosApi
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
        private val photosApi: PhotosApi,
        private val collectionApi: CollectionApi,
        private val gson: Gson
) : PhotoRepository {

    override fun getPhotos(page: Int, perPage: Int, order: Order): Observable<List<Photo>> {
        return photosApi.getPhotos(page, perPage, order.value)
    }

    override fun getPhotoById(id: String): Single<Photo> {
        return photosApi.getPhotoById(id)
    }

    override fun getRelatedPhotos(id: String): Observable<List<Photo>> {
        return photosApi.getRelatedPhotos(id)
                .map { body ->
                    val string = body.string()

                    val result = gson.fromJson<JsonObject>(string, JsonObject::class.java)
                    val results = result.getAsJsonArray("results")

                    return@map gson.fromJson(results, Array<Photo>::class.java).toList()
                }
    }

    override fun getCollectionPhotos(collectionId: Int): Observable<List<Photo>> {
        return collectionApi.getCollectionPhotos(collectionId)
    }
}