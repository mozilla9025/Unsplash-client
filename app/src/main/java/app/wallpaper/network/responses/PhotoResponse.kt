package app.wallpaper.network.responses

import app.wallpaper.data.Photo

class PhotoResponse(var status: ResponseStatus) {

    var data: List<Photo>? = null
    var error: Throwable? = null

    constructor(status: ResponseStatus, data: List<Photo>) : this(status) {
        this.data = data
    }

    constructor(status: ResponseStatus, error: Throwable) : this(status) {
        this.error = error
    }

    companion object : ApiResponse<PhotoResponse> {
        override fun loading(): PhotoResponse {
            return PhotoResponse(ResponseStatus.LOADING)
        }

        override fun <V> success(data: V): PhotoResponse {
            return PhotoResponse(ResponseStatus.SUCCESS, data as List<Photo>)
        }

        override fun failure(error: Throwable): PhotoResponse {
            return PhotoResponse(ResponseStatus.FAILURE, error)
        }
    }
}