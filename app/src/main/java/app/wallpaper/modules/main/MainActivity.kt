package app.wallpaper.modules.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import app.wallpaper.R
import app.wallpaper.modules.base.BaseActivity

open class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getPhotos()
    }
}
