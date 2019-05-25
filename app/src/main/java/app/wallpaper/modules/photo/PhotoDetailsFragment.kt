package app.wallpaper.modules.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment

class PhotoDetailsFragment : BaseFragment() {

    private val viewModel: PhotoDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(PhotoDetailsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        return view
    }
}