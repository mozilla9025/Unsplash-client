package app.wallpaper.modules.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.modules.base.BaseViewModelFragment
import app.wallpaper.network.responses.CollectionResponse
import app.wallpaper.network.responses.PagingResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.ViewModel
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*

@ViewModel(HomeViewModel::class)
class HomeFragment : BaseViewModelFragment<HomeViewModel>(R.layout.fragment_home) {

    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter({
            viewModel.retry()
        }, {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPhotoDetailsFragment(it))
        })
    }
    private val collectionAdapter: UnsplashCollectionAdapter by lazy {
        UnsplashCollectionAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUnsplashCollections()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        srlHome.setOnRefreshListener { viewModel.refresh() }
        rvPhotos.adapter = photoAdapter
        rvPhotos.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvPhotos.addItemDecoration(MarginItemDecoration(4.dp, 0.dp, RecyclerView.VERTICAL))

        rvUnsplashCollections.adapter = collectionAdapter
        rvUnsplashCollections.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rvUnsplashCollections.addItemDecoration(MarginItemDecoration(0.dp, 4.dp, RecyclerView.HORIZONTAL))

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
                loadingHome.onError(response.error?.message
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
