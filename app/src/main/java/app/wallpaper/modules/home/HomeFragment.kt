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
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import app.wallpaper.widget.ClickListener
import app.wallpaper.widget.progress.LoadingView
import app.wallpaper.widget.progress.swiperefresh.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @BindView(R.id.rv_photos)
    lateinit var rvPhotos: RecyclerView
    @BindView(R.id.rv_unsplash_collections)
    lateinit var rvCollections: RecyclerView
    @BindView(R.id.loading_home)
    lateinit var loadingView: LoadingView
    @BindView(R.id.srl_home)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var collectionAdapter: UnsplashCollectionAdapter

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        unbinder = ButterKnife.bind(this, view)

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
                view?.findNavController()?.navigate(R.id.action_global_photoDetailsFragment, args)
            }
        }

        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        rvPhotos.adapter = photoAdapter
        rvPhotos.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rvPhotos.addItemDecoration(MarginItemDecoration(4.dp, 0.dp, RecyclerView.VERTICAL))

        collectionAdapter = UnsplashCollectionAdapter()
        rvCollections.adapter = collectionAdapter
        rvCollections.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvCollections.addItemDecoration(MarginItemDecoration(0.dp, 4.dp, RecyclerView.HORIZONTAL))

        observeData()

        viewModel.getUnsplashCollections()
        return view
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
                if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
                loadingView.onSuccess()
                rvPhotos.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
                rvPhotos.visibility = View.GONE
                loadingView.onError(response.error?.message
                        ?: getString(R.string.Api_Call_Default_Error_Message), object : LoadingView.OnRetryClickListener {
                    override fun onRetryClicked() {
                        viewModel.retry()
                    }
                })
            }
            ResponseStatus.LOADING -> {
                if (photoAdapter.currentList == null || photoAdapter.currentList!!.isEmpty()) {
                    rvPhotos.visibility = View.GONE
                    loadingView.onLoading()
                }
            }
        }
    }

    private fun handleCollectionsLoad(response: CollectionResponse) {
        when (response.status) {
            ResponseStatus.SUCCESS -> {
                collectionAdapter.updateData(response.data)
                rvCollections.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                rvCollections.visibility = View.GONE
            }
            ResponseStatus.LOADING -> {
                rvCollections.visibility = View.GONE
            }
        }
    }
}
