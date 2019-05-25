package app.wallpaper.domain.repo.impl

import app.wallpaper.domain.data.Order
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.repo.PhotoRepository
import app.wallpaper.network.api.CollectionApi
import app.wallpaper.network.api.PhotosApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photosApi: PhotosApi,
                                              private val collectionApi: CollectionApi) : PhotoRepository {

    override fun getPhotos(page: Int, perPage: Int, order: Order): Observable<List<Photo>> {
        return photosApi.getPhotos(page, perPage, order.value)
                .subscribeOn(Schedulers.io())
    }

    override fun getPhotoById(id: String): Single<Photo> {
        return photosApi.getPhotoById(id)
                .subscribeOn(Schedulers.io())
    }

    override fun getRelatedPhotos(id: String): Observable<List<Photo>> {
        return photosApi.getRelatedPhotos(id)
                .subscribeOn(Schedulers.io())
    }

    override fun getCollectionPhotos(collectionId: Int): Observable<List<Photo>> {
        return collectionApi.getCollectionPhotos(collectionId)
                .subscribeOn(Schedulers.io())
    }
}