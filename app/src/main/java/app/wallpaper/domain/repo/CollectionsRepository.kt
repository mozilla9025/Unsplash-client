package app.wallpaper.domain.repo

import app.wallpaper.domain.data.Collection
import io.reactivex.Observable
import io.reactivex.Single

interface CollectionsRepository {

    fun getCollections(page: Int, perPage: Int): Observable<List<Collection>>

    fun getFeaturedCollections(page: Int, perPage: Int): Observable<List<Collection>>

    fun getCuratedCollections(page: Int, perPage: Int): Observable<List<Collection>>

    fun getCollection(id: Int): Single<Collection>

    fun getRelatedCollections(id: Int): Observable<List<Collection>>
}