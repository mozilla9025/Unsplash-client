package app.wallpaper.network.responses

class Response<out T> private constructor(
        val status: ResponseStatus,
        val data: T?,
        val error: Throwable?) {

    companion object {
        fun <T> loading(): Response<T> {
            return Response(ResponseStatus.LOADING, null, null)
        }

        fun <T> success(data: T?): Response<T> {
            return Response(ResponseStatus.SUCCESS, data, null)
        }

        fun <T> failure(error: Throwable?): Response<T> {
            return Response(ResponseStatus.FAILURE, null, error)
        }
    }
}