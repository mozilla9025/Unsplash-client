package app.wallpaper.domain.usecase

import app.wallpaper.domain.data.PhotoCollection
import io.reactivex.Observable
import io.reactivex.Single

interface GetCollectionsUseCase {

    fun getCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>>

    fun getFeaturedCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>>

    fun getCuratedCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>>

    fun getCollection(id: Int): Single<PhotoCollection>

    fun getRelatedCollections(id: Int): Observable<List<PhotoCollection>>
}