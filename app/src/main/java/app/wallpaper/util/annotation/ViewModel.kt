package app.wallpaper.util.annotation

import app.wallpaper.modules.base.BaseViewModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ViewModel(val viewModelClass: KClass<out BaseViewModel>)