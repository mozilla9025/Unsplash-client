package app.wallpaper.network.controllers

import app.wallpaper.data.Order
import app.wallpaper.data.Photo
import app.wallpaper.network.api.PhotosApi
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

class PhotosApiController(
    private val api: PhotosApi
) {

    fun getPhotos(
        page: Int, perPage: Int, orderBy: Order
    ): Observable<List<Photo>> {
        return api.getPhotos(page, perPage, orderBy.value)
    }

    fun getPhotos(id: String): Observable<List<Photo>> {
        return api.getPhotos(id)
    }

    fun getDownloadUrl(id: String): Observable<Response<ResponseBody>> {
        return api.getDownloadUrl(id)
    }

    fun like(id: String): Observable<Response<ResponseBody>> {
        return api.like(id)
    }

    fun unlike(id: String): Observable<Response<ResponseBody>> {
        return api.unlike(id)
    }
}