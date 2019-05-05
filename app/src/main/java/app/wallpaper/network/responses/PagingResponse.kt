package app.wallpaper.network.responses

class PagingResponse(var status: ResponseStatus) {

    var error: Throwable? = null

    constructor(status: ResponseStatus, error: Throwable?) : this(status) {
        this.error = error
    }

    companion object : StateApiResponse<PagingResponse> {
        override fun loading(): PagingResponse {
            return PagingResponse(ResponseStatus.LOADING)
        }

        override fun success(): PagingResponse {
            return PagingResponse(ResponseStatus.SUCCESS)
        }

        override fun failure(error: Throwable?): PagingResponse {
            return PagingResponse(ResponseStatus.FAILURE, error)
        }
    }
}