package app.wallpaper.modules.photo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.domain.data.Photo
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.modules.home.RelatedPhotosAdapter
import app.wallpaper.network.responses.Response
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.extentions.getLinkName
import app.wallpaper.util.extentions.loge
import app.wallpaper.util.recycler.GridItemDecoration
import app.wallpaper.widget.progress.LoadingView
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
        arguments?.let {
            viewModel.photo = PhotoFragmentArgs.fromBundle(it).photo
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun observeData() {
        viewModel.photoLiveData.observe(viewLifecycleOwner, Observer { handleInfoUpdate(it) })
    }

    private fun setUpAuthor() {
        viewModel.photo?.let { photo ->
            photo.user?.let { user ->
                tvAuthor.text = user.name
                if (user.links == null) {
                    tvPortfolioUrl.visibility = View.GONE
                } else {
                    tvPortfolioUrl.visibility = View.VISIBLE
                    tvPortfolioUrl.text = user.links.getLinkName()
                }

                GlideApp.with(this)
                        .load(user.avatar?.medium)
                        .into(ivAvatar)
            }
        }
    }

    val decoration = GridItemDecoration(2, 2.dp, false)

    private fun handleInfoUpdate(response: Response<Pair<Photo, List<Photo>>>?) {
        when (response?.status) {
            ResponseStatus.SUCCESS -> {
                loadingViewInfo.onSuccess()
                clInfo.visibility = View.VISIBLE
                if (response.data?.second != null && response.data.second.isNotEmpty()) {
                    tvRvTitle.visibility = View.VISIBLE
                    rvRelatedPhotos.run {
                        visibility = View.VISIBLE
                        layoutManager = GridLayoutManager(context!!, 2)
                        adapter = RelatedPhotosAdapter().apply {
                            updateData(response.data.second)
                        }
                        removeItemDecoration(decoration)
                        addItemDecoration(decoration)
                        isNestedScrollingEnabled = false
                    }
                } else {
                    tvRvTitle.visibility = View.GONE
                    rvRelatedPhotos.visibility = View.GONE
                }
                setUpInfo()
            }
            ResponseStatus.LOADING -> {
                clInfo.visibility = View.GONE
                loadingViewInfo.onLoading()
            }
            ResponseStatus.FAILURE -> {
                clInfo.visibility = View.GONE
                loadingViewInfo.onError(getString(R.string.Api_Call_Default_Error_Message),
                        object : LoadingView.OnRetryClickListener {
                            override fun onRetryClicked() {
                                viewModel.updateInfo()
                            }
                        })
                response.error?.let { loge("err", it) }
            }
        }
    }

    private fun setUpInfo() {
        viewModel.photo?.let { photo ->
            if (photo.location != null) {
                itvLocation.visibility = View.VISIBLE
                itvLocation.subTitle = "${photo.location.city}, ${photo.location.country}"
            } else itvLocation.visibility = View.GONE

            if (photo.downloads != null) {
                itvDownloads.visibility = View.VISIBLE
                itvDownloads.subTitle = "${photo.downloads}"
            } else itvDownloads.visibility = View.GONE

            if (photo.likes != null) {
                itvLikes.visibility = View.VISIBLE
                itvLikes.subTitle = "${photo.likes}"
            } else itvLikes.visibility = View.GONE
        }
    }
}
