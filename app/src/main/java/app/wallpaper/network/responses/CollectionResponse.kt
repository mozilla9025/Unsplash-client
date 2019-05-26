package app.wallpaper.network.responses

import app.wallpaper.domain.data.PhotoCollection

class CollectionResponse(var status: ResponseStatus? = null) {

    var data: List<PhotoCollection>? = null
    var error: Throwable? = null

    constructor(status: ResponseStatus, data: List<PhotoCollection>) : this(status) {
        this.data = data
    }

    constructor(status: ResponseStatus, error: Throwable) : this(status) {
        this.error = error
    }

    companion object : ApiResponse<CollectionResponse> {

        override fun loading(): CollectionResponse {
            return CollectionResponse(ResponseStatus.LOADING)
        }

        override fun <V> success(data: V): CollectionResponse {
            return CollectionResponse(ResponseStatus.SUCCESS, data as List<PhotoCollection>)
        }

        override fun failure(error: Throwable): CollectionResponse {
            return CollectionResponse(ResponseStatus.FAILURE, error)
        }
    }
}