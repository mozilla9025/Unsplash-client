package app.wallpaper.network.responses

import app.wallpaper.domain.data.Photo

class PhotoListResponse(var status: ResponseStatus) {

    var data: List<Photo>? = null
    var error: Throwable? = null

    constructor(status: ResponseStatus, data: List<Photo>) : this(status) {
        this.data = data
    }

    constructor(status: ResponseStatus, error: Throwable) : this(status) {
        this.error = error
    }

    companion object : ApiResponse<PhotoListResponse> {
        override fun loading(): PhotoListResponse {
            return PhotoListResponse(ResponseStatus.LOADING)
        }

        override fun <V> success(data: V): PhotoListResponse {
            return PhotoListResponse(ResponseStatus.SUCCESS, data as List<Photo>)
        }

        override fun failure(error: Throwable): PhotoListResponse {
            return PhotoListResponse(ResponseStatus.FAILURE, error)
        }
    }
}