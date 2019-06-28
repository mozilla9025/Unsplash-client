package app.wallpaper.modules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.domain.data.Photo
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.network.Retryable
import app.wallpaper.network.responses.CollectionResponse
import app.wallpaper.network.responses.PagingResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import app.wallpaper.widget.ClickListener
import app.wallpaper.widget.progress.LoadingView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@Layout(R.layout.fragment_home)
class HomeFragment : BaseFragment() {

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var collectionAdapter: UnsplashCollectionAdapter

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photoAdapter = PhotoAdapter(object : Retryable {
            override fun retry() {
                viewModel.retry()
            }
        })

        photoAdapter.clickListener = object : ClickListener<Photo> {
            override fun onItemClick(item: Photo) {
                val args = Bundle().apply {
                    putParcelable("photo", item)
                }
                view.findNavController().navigate(R.id.action_global_photoDetailsFragment, args)
            }
        }

        srlHome.setOnRefreshListener { viewModel.refresh() }
        rvPhotos.adapter = photoAdapter
        rvPhotos.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rvPhotos.addItemDecoration(MarginItemDecoration(4.dp, 0.dp, RecyclerView.VERTICAL))

        collectionAdapter = UnsplashCollectionAdapter()
        rvUnsplashCollections.adapter = collectionAdapter
        rvUnsplashCollections.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvUnsplashCollections.addItemDecoration(MarginItemDecoration(0.dp, 4.dp, RecyclerView.HORIZONTAL))

        observeData()

        viewModel.getUnsplashCollections()
    }

    private fun observeData() {
        viewModel.collectionsLiveData.observe(viewLifecycleOwner, Observer { handleCollectionsLoad(it) })
        viewModel.getInitialLoadState().observe(viewLifecycleOwner, Observer { handleInitialLoad(it) })
        viewModel.getRangeLoadState().observe(viewLifecycleOwner, Observer { handleRangeLoad(it) })
        viewModel.data.observe(viewLifecycleOwner, Observer {
            photoAdapter.submitList(it)
        })
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
                        ?: getString(R.string.Api_Call_Default_Error_Message), object : LoadingView.OnRetryClickListener {
                    override fun onRetryClicked() {
                        viewModel.retry()
                    }
                })
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
