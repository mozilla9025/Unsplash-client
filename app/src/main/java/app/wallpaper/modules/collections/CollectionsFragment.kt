package app.wallpaper.modules.collections

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.modules.base.BaseViewModelFragment
import app.wallpaper.network.responses.PagingResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.annotation.ViewModel
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import kotlinx.android.synthetic.main.fragment_collections.*

@ViewModel(CollectionsViewModel::class)
class CollectionsFragment : BaseViewModelFragment<CollectionsViewModel>(R.layout.fragment_collections) {

    private val adapter: CollectionsAdapter by lazy {
        CollectionsAdapter { viewModel.retry() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        srlCollections.setOnRefreshListener { viewModel.refresh() }
        rvCollections.run {
            adapter = this@CollectionsFragment.adapter
            layoutManager = LinearLayoutManager(context!!)
            addItemDecoration(MarginItemDecoration(4.dp, 0.dp, RecyclerView.VERTICAL))
        }

        observeData()
    }

    private fun observeData() {
        with(viewModel) {
            getInitialLoadState().observe(
                    viewLifecycleOwner,
                    Observer { handleInitialLoad(it) })
            getRangeLoadState().observe(viewLifecycleOwner, Observer { handleRangeLoad(it) })
            data.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        }
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
                loadingCollections.onError(
                        response.error?.message
                                ?: getString(R.string.Api_Call_Default_Error_Message)
                ) { viewModel.retry() }
            }
            ResponseStatus.LOADING -> {
                if (adapter.itemCount == 0) {
                    rvCollections.visibility = View.GONE
                    loadingCollections.onLoading()
                }
            }
        }
    }
}