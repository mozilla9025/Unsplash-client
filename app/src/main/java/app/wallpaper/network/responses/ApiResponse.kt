package app.wallpaper.network.responses

import android.util.Log

data class ApiResponse(var status: ResponseStatus) {

    var data: Any? = null
    var error: Throwable? = null

    init {
        Log.i("ApiResponse", "$this ${this.hashCode()}")
    }

    constructor(status: ResponseStatus, data: Any) : this(status) {
        this.data = data
    }

    constructor(status: ResponseStatus, error: Throwable) : this(status) {
        this.error = error
    }

    companion object {
        @JvmStatic
        fun loading(): ApiResponse {
            return ApiResponse(ResponseStatus.LOADING)
        }

        @JvmStatic
        fun success(data: Any): ApiResponse {
            return ApiResponse(ResponseStatus.SUCCESS, data)
        }

        @JvmStatic
        fun failure(error: Throwable): ApiResponse {
            return ApiResponse(ResponseStatus.FAILURE, error)
        }
    }
}