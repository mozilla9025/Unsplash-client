package app.wallpaper.modules.photo

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.network.responses.PhotoListResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.Layout
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_photo.*
import javax.inject.Inject

@Layout(R.layout.fragment_photo)
class PhotoFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: PhotoDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            viewModel.photo = PhotoFragmentArgs.fromBundle(it).photo
        }

        GlideApp.with(this)
                .load(viewModel.photo?.urls?.regular!!)
                .into(ivPhoto)
        observeData()
        viewModel.getRelatedPhotos()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun observeData() {
        viewModel.relatedPhotoLiveData.observe(viewLifecycleOwner, Observer { handleRelatedPhotosResponse(it) })
    }

    private fun handleRelatedPhotosResponse(response: PhotoListResponse?) {
        when (response?.status) {
            ResponseStatus.SUCCESS -> {
            }
            ResponseStatus.LOADING -> {
            }
            ResponseStatus.FAILURE -> {
            }
        }
    }
}