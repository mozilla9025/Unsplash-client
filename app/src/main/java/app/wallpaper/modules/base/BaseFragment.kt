package app.wallpaper.modules.base

import androidx.fragment.app.Fragment
import butterknife.Unbinder

abstract class BaseFragment : Fragment() {
    protected var unbinder: Unbinder? = null

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }
}