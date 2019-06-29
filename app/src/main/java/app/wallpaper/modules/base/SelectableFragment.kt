package app.wallpaper.modules.base

import app.wallpaper.util.extentions.logi

abstract class SelectableFragment : BaseFragment() {

    init {
        logi("init")
    }

    abstract fun onFragmentSelected()
}