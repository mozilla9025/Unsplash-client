package app.wallpaper.network.responses

import app.wallpaper.data.Photo

class PhotoResponse() : ApiResponse<PhotoResponse> {

    var status: ResponseStatus? = null
    var data: List<Photo>? = null
    var error: Throwable? = null

    constructor(status: ResponseStatus) : this() {
        this.status = status
    }

    constructor(status: ResponseStatus, data: List<Photo>) : this(status) {
        this.data = data
    }

    constructor(status: ResponseStatus, error: Throwable) : this(status) {
        this.error = error
    }

    companion object {
        val instance: PhotoResponse = PhotoResponse()
    }

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