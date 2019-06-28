package app.wallpaper.modules.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.wallpaper.util.annotation.Layout
import kotlin.reflect.full.findAnnotation

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        this::class.findAnnotation<Layout>()?.let {
            return inflater.inflate(it.layout, container, false)
        } ?: throw IllegalStateException("Not annotated fragment ${this::class}")
    }
}