package app.wallpaper.util.annotation

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes

annotation class NavigationParams(@IdRes val fragementId: Int,
                                  @NavigationRes val navGraph: Int)