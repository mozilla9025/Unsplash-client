package app.wallpaper.domain.usecase.impl

import app.wallpaper.domain.data.Order
import app.wallpaper.domain.data.Photo
import app.wallpaper.domain.repo.PhotoRepository
import app.wallpaper.domain.usecase.GetPhotosUseCase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetPhotosUseCaseImpl @Inject constructor(private val repository: PhotoRepository) : GetPhotosUseCase {
    override fun getPhotos(limit: Int, offset: Int, order: Order): Observable<List<Photo>> {
        return repository.getPhotos(limit, offset, order)
                .subscribeOn(Schedulers.io())
    }

    override fun getPhotoById(id: String): Single<Photo> {
        return repository.getPhotoById(id)
                .subscribeOn(Schedulers.io())
    }

    override fun getRelatedPhotos(id: String): Observable<List<Photo>> {
        return repository.getRelatedPhotos(id)
                .subscribeOn(Schedulers.io())
    }

    override fun getCollectionPhotos(collectionId: Int): Observable<List<Photo>> {
        return repository.getCollectionPhotos(collectionId)
                .subscribeOn(Schedulers.io())
    }
}