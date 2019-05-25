package app.wallpaper.domain.usecase

import app.wallpaper.domain.data.Collection
import io.reactivex.Observable
import io.reactivex.Single

interface GetCollectionsUseCase {

    fun getCollections(page: Int, perPage: Int): Observable<List<Collection>>

    fun getFeaturedCollections(page: Int, perPage: Int): Observable<List<Collection>>

    fun getCuratedCollections(page: Int, perPage: Int): Observable<List<Collection>>

    fun getCollection(id: Int): Single<Collection>

    fun getRelatedCollections(id: Int): Observable<List<Collection>>
}