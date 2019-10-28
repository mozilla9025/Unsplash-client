package app.wallpaper.modules.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import app.wallpaper.di.ViewModelFactory
import app.wallpaper.util.annotation.ViewModel
import javax.inject.Inject
import kotlin.reflect.full.findAnnotation

abstract class BaseViewModelFragment<T : BaseViewModel> : BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelFactory
    protected lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this::class.findAnnotation<ViewModel>()?.let {
            viewModel = ViewModelProviders.of(this@BaseViewModelFragment, vmFactory)[it.viewModelClass.java] as T
        }
                ?: throw IllegalArgumentException("Unable to find annotation @ViewModel for ${this::class.simpleName}")
    }
}