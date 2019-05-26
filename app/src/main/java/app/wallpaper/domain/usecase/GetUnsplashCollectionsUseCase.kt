package app.wallpaper.domain.usecase

import app.wallpaper.domain.data.PhotoCollection
import io.reactivex.Observable

interface GetUnsplashCollectionsUseCase {
    fun getCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>>
}