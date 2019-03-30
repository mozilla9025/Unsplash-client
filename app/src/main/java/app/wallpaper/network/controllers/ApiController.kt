package app.wallpaper.network.controllers

import app.wallpaper.network.api.PhotosApi
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response

class ApiController(val api: PhotosApi) {

    fun getRandomPicture(): Observable<Response<ResponseBody>> {
        return api.getRandomPicture()
    }

}