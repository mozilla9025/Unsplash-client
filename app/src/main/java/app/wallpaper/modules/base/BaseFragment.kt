package app.wallpaper.modules.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.extentions.logd
import dagger.android.support.DaggerFragment
import kotlin.reflect.full.findAnnotation

abstract class BaseFragment : DaggerFragment() {

    protected val navController by lazy {
        view!!.findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this::class.findAnnotation<Layout>()?.let {
            return inflater.inflate(it.layout, container, false)
        } ?: throw IllegalStateException("Not annotated fragment ${this::class}")
    }

    private val backPressedCallback = object : OnBackPressedCallback(backPressedEnabled()) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun onStop() {
        super.onStop()
        backPressedCallback.remove()
    }

    open fun backPressedEnabled(): Boolean = true

    open fun onBackPressed() {
        logd("${this.javaClass.simpleName} OnBackPressed")
    }
}