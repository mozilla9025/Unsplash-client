package app.wallpaper.modules.base

import butterknife.Unbinder
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    protected var unbinder: Unbinder? = null

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }

}
