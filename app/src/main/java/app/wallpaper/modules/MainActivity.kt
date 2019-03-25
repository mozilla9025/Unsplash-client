package app.wallpaper.modules

import android.os.Bundle
import android.os.PersistableBundle
import app.wallpaper.R

class MainActivity : BaseActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
    }
}
