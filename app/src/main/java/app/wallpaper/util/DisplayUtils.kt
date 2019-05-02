package app.wallpaper.util

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

class DisplayUtils {
    companion object {
        fun getDisplayMetrics(context: Context): Point {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }

        fun getDisplayWidth(context: Context): Int {
            return getDisplayMetrics(context).x
        }

        fun getDisplayHeight(context: Context): Int {
            return getDisplayMetrics(context).y
        }
    }
}