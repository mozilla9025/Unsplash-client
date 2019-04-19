package app.wallpaper.di

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val clientId: String
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            chain.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Client-ID $clientId"
                )
                .build()
        )
    }
}