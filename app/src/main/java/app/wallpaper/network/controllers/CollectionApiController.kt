package app.wallpaper.network.controllers

import app.wallpaper.domain.data.Collection
import app.wallpaper.network.api.CollectionApi
import io.reactivex.Observable

class CollectionApiController(
        private val api: CollectionApi
) {
    fun getCollections(page: Int, perPage: Int) : Observable<List<Collection>> {
        return api.getCollections(page, perPage)
    }
}