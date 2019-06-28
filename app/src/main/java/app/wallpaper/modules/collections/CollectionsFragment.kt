package app.wallpaper.modules.collections

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.network.Retryable
import app.wallpaper.network.responses.PagingResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import app.wallpaper.widget.progress.LoadingView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_collections.*
import javax.inject.Inject

@Layout(R.layout.fragment_collections)
class CollectionsFragment : BaseFragment() {

    private lateinit var adapter: CollectionsAdapter

    @Inject
    lateinit var viewModel: CollectionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        srlCollections.setOnRefreshListener { viewModel.refresh() }

        adapter = CollectionsAdapter(object : Retryable {
            override fun retry() {
                viewModel.retry()
            }
        })
        rvCollections.adapter = adapter
        rvCollections.layoutManager = LinearLayoutManager(context!!)
        rvCollections.addItemDecoration(MarginItemDecoration(4.dp, 0.dp, RecyclerView.VERTICAL))

        observeData()
    }

    private fun observeData() {
        viewModel.getInitialLoadState().observe(viewLifecycleOwner, Observer { handleInitialLoad(it) })
        viewModel.getRangeLoadState().observe(viewLifecycleOwner, Observer { handleRangeLoad(it) })
        viewModel.data.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun handleRangeLoad(response: PagingResponse) {
        adapter.updateResponse(response.status)
    }

    private fun handleInitialLoad(response: PagingResponse) {
        when (response.status) {
            ResponseStatus.SUCCESS -> {
                if (srlCollections.isRefreshing)
                    srlCollections.isRefreshing = false

                loadingCollections.onSuccess()
                rvCollections.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                if (srlCollections.isRefreshing)
                    srlCollections.isRefreshing = false

                rvCollections.visibility = View.GONE
                loadingCollections.onError(response.error?.message
                        ?: getString(R.string.Api_Call_Default_Error_Message),
                        object : LoadingView.OnRetryClickListener {
                            override fun onRetryClicked() {
                                viewModel.retry()
                            }
                        })
            }
            ResponseStatus.LOADING -> {
                if (adapter.currentList == null || adapter.currentList!!.isEmpty()) {
                    rvCollections.visibility = View.GONE
                    loadingCollections.onLoading()
                }
            }
        }
    }
}