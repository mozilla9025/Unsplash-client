package app.wallpaper.modules

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import app.wallpaper.R

open class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Main activity created", Toast.LENGTH_SHORT).show()
        Log.i("TAG", "onCreate")
        viewModel.getPhotos()
    }
}
