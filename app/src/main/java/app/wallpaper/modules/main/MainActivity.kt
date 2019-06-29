package app.wallpaper.modules.main

import android.os.Bundle
import app.wallpaper.R
import app.wallpaper.modules.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(
            savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
