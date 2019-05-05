package app.wallpaper.util.extentions

import android.util.Log

inline fun <reified T> T.logi(message: String) = Log.i(T::class.java.simpleName, message)