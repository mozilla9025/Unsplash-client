package app.wallpaper.domain.usecase

import app.wallpaper.domain.data.Order
import app.wallpaper.domain.data.Photo
import io.reactivex.Observable
import io.reactivex.Single

interface GetPhotosUseCase {
    fun getPhotos(limit: Int, offset: Int, order: Order): Observable<List<Photo>>

    fun getPhotoById(id: String): Single<Photo>

    fun getRelatedPhotos(id: String): Observable<List<Photo>>

    fun getCollectionPhotos(collectionId: Int): Observable<List<Photo>>
}