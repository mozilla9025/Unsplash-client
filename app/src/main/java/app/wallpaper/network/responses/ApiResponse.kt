package app.wallpaper.network.responses

interface ApiResponse<out T> {

    fun loading(): T

    fun <V> success(data: V): T

    fun failure(error: Throwable): T
}