package app.wallpaper.modules.base

import androidx.appcompat.app.AppCompatActivity
import butterknife.Unbinder

abstract class BaseActivity : AppCompatActivity() {

    protected var unbinder: Unbinder? = null

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }
}
