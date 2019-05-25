package app.wallpaper.domain.usecase.impl

import app.wallpaper.domain.data.Collection
import app.wallpaper.domain.repo.CollectionsRepository
import app.wallpaper.domain.usecase.GetCollectionsUseCase
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetCollectionsUseCaseImpl @Inject constructor(private val repository: CollectionsRepository) : GetCollectionsUseCase {

    override fun getCollections(page: Int, perPage: Int): Observable<List<Collection>> {
        return repository.getCollections(page, perPage)
    }

    override fun getFeaturedCollections(page: Int, perPage: Int): Observable<List<Collection>> {
        return repository.getFeaturedCollections(page, perPage)
    }

    override fun getCuratedCollections(page: Int, perPage: Int): Observable<List<Collection>> {
        return repository.getCuratedCollections(page, perPage)
    }

    override fun getCollection(id: Int): Single<Collection> {
        return repository.getCollection(id)
    }

    override fun getRelatedCollections(id: Int): Observable<List<Collection>> {
        return repository.getRelatedCollections(id)
    }
}