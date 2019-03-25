package app.wallpaper.modules

import androidx.appcompat.app.AppCompatActivity
import butterknife.Unbinder

abstract class BaseActivity : AppCompatActivity() {

    protected var unbinder: Unbinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null)
            unbinder!!.unbind()
    }
}
