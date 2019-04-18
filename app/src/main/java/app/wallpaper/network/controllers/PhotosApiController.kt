package app.wallpaper.network.controllers

import app.wallpaper.data.Order
import app.wallpaper.network.api.PhotosApi
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

open class PhotosApiController(
    private val api: PhotosApi
) {

    fun getPhotos(
        page: Int, perPage: Int, orderBy: Order
    ): Observable<Response<ResponseBody>> {
        return api.getPhotos(page, perPage, orderBy.value)
    }

    fun getPhotos(id: String): Observable<Response<ResponseBody>> {
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