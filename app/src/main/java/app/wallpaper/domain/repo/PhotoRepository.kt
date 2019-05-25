package app.wallpaper.domain.repo

import app.wallpaper.domain.data.Order
import app.wallpaper.domain.data.Photo
import io.reactivex.Observable
import io.reactivex.Single

interface PhotoRepository {

    fun getPhotos(page: Int, perPage: Int, order: Order): Observable<List<Photo>>

    fun getPhotoById(id: String): Single<Photo>

    fun getRelatedPhotos(id: String): Observable<List<Photo>>

    fun getCollectionPhotos(collectionId: Int): Observable<List<Photo>>

//    fun getDownloadUrl(id: String): Single<Response<ResponseBody>>
//
//    //requires write_likes scope
//    fun like(id: String): Observable<Response<ResponseBody>>
//
//    fun dislike(id: String): Observable<Response<ResponseBody>>

}