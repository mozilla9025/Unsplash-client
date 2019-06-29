package app.wallpaper.modules.photo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.network.responses.PhotoListResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.extentions.getLinkName
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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

        viewModel.photo?.let {
            val params = ivPhoto.layoutParams
            val scaleFactor = params.width / it.width!!
            params.height = scaleFactor * it.height!!
            ivPhoto.layoutParams = params

            GlideApp.with(this)
                    .load(it.urls?.regular!!)
                    .placeholder(ColorDrawable(Color.parseColor(it.color)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivPhoto)
        }

        setUpAuthor()
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

    private fun setUpAuthor() {
        viewModel.photo?.user?.let {
            tvAuthor.text = it.name
            if (it.links == null) {
                tvPortfolioUrl.visibility = View.GONE
            } else {
                tvPortfolioUrl.visibility = View.VISIBLE
                tvPortfolioUrl.text = it.links.getLinkName()
            }

            GlideApp.with(this)
                    .load(it.avatar?.medium)
                    .into(ivAvatar)
        }
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