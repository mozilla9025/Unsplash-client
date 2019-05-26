package app.wallpaper.domain.repo.impl

import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.domain.repo.CollectionsRepository
import app.wallpaper.network.api.CollectionApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(private val api: CollectionApi) : CollectionsRepository {

    override fun getCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>> {
        return api.getCollections(page, perPage)
                .subscribeOn(Schedulers.io())
    }

    override fun getFeaturedCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>> {
        return api.getFeaturedCollections(page, perPage)
                .subscribeOn(Schedulers.io())
    }

    override fun getCuratedCollections(page: Int, perPage: Int): Observable<List<PhotoCollection>> {
        return api.getCuratedCollections(page, perPage)
                .subscribeOn(Schedulers.io())
    }

    override fun getCollection(id: Int): Single<PhotoCollection> {
        return api.getCollection(id)
                .subscribeOn(Schedulers.io())
    }

    override fun getRelatedCollections(id: Int): Observable<List<PhotoCollection>> {
        return api.getRelatedCollections(id)
                .subscribeOn(Schedulers.io())
    }
}