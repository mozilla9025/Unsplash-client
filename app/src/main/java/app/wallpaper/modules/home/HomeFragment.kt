package app.wallpaper.modules.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.domain.data.Photo
import app.wallpaper.modules.base.BaseViewModelFragment
import app.wallpaper.network.Retryable
import app.wallpaper.network.responses.CollectionResponse
import app.wallpaper.network.responses.PagingResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.annotation.ViewModel
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import app.wallpaper.widget.ClickListener
import kotlinx.android.synthetic.main.fragment_home.*

@Layout(R.layout.fragment_home)
@ViewModel(HomeViewModel::class)
class HomeFragment : BaseViewModelFragment<HomeViewModel>() {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var collectionAdapter: UnsplashCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUnsplashCollections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photoAdapter = PhotoAdapter(object : Retryable {
            override fun retry() {
                viewModel.retry()
            }
        })

        photoAdapter.clickListener = object : ClickListener<Photo> {
            override fun onItemClick(item: Photo) {
                navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToPhotoDetailsFragment(
                        item
                    )
                )
            }
        }

        srlHome.setOnRefreshListener { viewModel.refresh() }
        rvPhotos.adapter = photoAdapter
        rvPhotos.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rvPhotos.addItemDecoration(MarginItemDecoration(4.dp, 0.dp, RecyclerView.VERTICAL))

        collectionAdapter = UnsplashCollectionAdapter()
        rvUnsplashCollections.adapter = collectionAdapter
        rvUnsplashCollections.layoutManager =
            LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvUnsplashCollections.addItemDecoration(
            MarginItemDecoration(
                0.dp,
                4.dp,
                RecyclerView.HORIZONTAL
            )
        )

        observeData()
    }

    private fun observeData() {
        with(viewModel) {
            collectionsLiveData.observe(viewLifecycleOwner, Observer { handleCollectionsLoad(it) })
            data.observe(viewLifecycleOwner, Observer { photoAdapter.submitList(it) })
            getInitialLoadState().observe(viewLifecycleOwner, Observer { handleInitialLoad(it) })
            getRangeLoadState().observe(viewLifecycleOwner, Observer { handleRangeLoad(it) })
        }
    }

    private fun handleRangeLoad(response: PagingResponse) {
        photoAdapter.updateResponse(response.status)
    }

    private fun handleInitialLoad(response: PagingResponse) {
        when (response.status) {
            ResponseStatus.SUCCESS -> {
                if (srlHome.isRefreshing) srlHome.isRefreshing = false
                loadingHome.onSuccess()
                rvPhotos.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                if (srlHome.isRefreshing) srlHome.isRefreshing = false
                rvPhotos.visibility = View.GONE
                loadingHome.onError(
                    response.error?.message
                        ?: getString(R.string.Api_Call_Default_Error_Message)
                ) { viewModel.retry() }
            }
            ResponseStatus.LOADING -> {
                if (photoAdapter.currentList == null || photoAdapter.currentList!!.isEmpty()) {
                    rvPhotos.visibility = View.GONE
                    loadingHome.onLoading()
                }
            }
        }
    }

    private fun handleCollectionsLoad(response: CollectionResponse) {
        when (response.status) {
            ResponseStatus.SUCCESS -> {
                collectionAdapter.updateData(response.data)
                rvUnsplashCollections.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                rvUnsplashCollections.visibility = View.GONE
            }
            ResponseStatus.LOADING -> {
                rvUnsplashCollections.visibility = View.GONE
            }
        }
    }
}
