package app.wallpaper.modules.photo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.domain.data.Photo
import app.wallpaper.modules.base.BaseViewModelFragment
import app.wallpaper.modules.home.RelatedPhotosAdapter
import app.wallpaper.network.responses.Response
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.ViewModel
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.extentions.getLinkName
import app.wallpaper.util.extentions.loge
import app.wallpaper.util.recycler.GridItemDecoration
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.fragment_photo.*

@ViewModel(PhotoDetailsViewModel::class)
class PhotoFragment : BaseViewModelFragment<PhotoDetailsViewModel>(R.layout.fragment_photo) {

    private val decoration by lazy {
        GridItemDecoration(2, 2.dp, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.photo = PhotoFragmentArgs.fromBundle(it).photo
        }
        viewModel.updateInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.photo?.let {
            GlideApp.with(this)
                    .load(it.urls.regular)
                    .centerCrop()
                    .placeholder(ColorDrawable(Color.parseColor(it.color)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivPhoto)
        }

        setUpAuthor()
        observeData()
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
                loadingViewInfo.onError(
                        getString(R.string.Api_Call_Default_Error_Message)
                ) { viewModel.updateInfo() }
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
