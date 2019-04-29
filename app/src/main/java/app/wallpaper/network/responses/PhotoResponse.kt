package app.wallpaper.network.responses

import app.wallpaper.data.Photo

data class PhotoResponse(val status: ResponseStatus) {

    var data: List<Photo>? = null
    var error: Throwable? = null

    constructor(status: ResponseStatus, data: List<Photo>) : this(status) {
        this.data = data
    }

    constructor(status: ResponseStatus, error: Throwable) : this(status) {
        this.error = error
    }

    companion object {
        @JvmStatic
        fun loading(): PhotoResponse {
            return PhotoResponse(ResponseStatus.LOADING)
        }

        @JvmStatic
        fun success(data: List<Photo>): PhotoResponse {
            return PhotoResponse(ResponseStatus.SUCCESS, data)
        }

        @JvmStatic
        fun failure(error: Throwable): PhotoResponse {
            return PhotoResponse(ResponseStatus.FAILURE, error)
        }
    }

}