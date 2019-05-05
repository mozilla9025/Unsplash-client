package app.wallpaper.network.responses

interface StateApiResponse<T> {
    fun success(): T
    fun loading(): T
    fun failure(error: Throwable?): T
}