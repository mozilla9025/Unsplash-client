package app.wallpaper.modules.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import app.wallpaper.util.annotation.ViewModel
import kotlin.reflect.full.findAnnotation

open class BaseViewModelFragment<T : BaseViewModel> : BaseFragment() {

    protected lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this::class.findAnnotation<ViewModel>()?.let {
            viewModel = ViewModelProviders.of(this@BaseViewModelFragment)
                .get(it.viewModelClass.java) as T
        }
            ?: throw IllegalArgumentException("Unable to find annotation @ViewModel for ${this::class.simpleName}")
    }
}