package app.wallpaper.network.responses

interface ApiResponse<T> {

    fun loading(): T

    fun <V> success(data: V): T

    fun failure(error: Throwable): T
}