package app.wallpaper.modules.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.network.Retryable
import app.wallpaper.network.responses.PagingResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import app.wallpaper.widget.progress.LoadingView
import butterknife.BindView
import butterknife.ButterKnife

class CollectionsFragment : BaseFragment() {

    @BindView(R.id.rv_collections)
    lateinit var rvCollections: RecyclerView
    @BindView(R.id.loading_collections)
    lateinit var loadingView: LoadingView

    private lateinit var adapter: CollectionsAdapter

    private val viewModel: CollectionsViewModel by lazy {
        ViewModelProviders.of(this).get(CollectionsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_collections, container, false)
        unbinder = ButterKnife.bind(this, view)

        adapter = CollectionsAdapter(object : Retryable {
            override fun retry() {
                viewModel.retry()
            }
        })
        rvCollections.adapter = adapter
        rvCollections.layoutManager = LinearLayoutManager(context!!)
        rvCollections.addItemDecoration(MarginItemDecoration(4.dp, 0.dp))

        observeData()
        return view
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
                loadingView.onSuccess()
                rvCollections.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                rvCollections.visibility = View.GONE
                loadingView.onError(response.error?.message
                        ?: getString(R.string.Api_Call_Default_Error_Message),
                        object : LoadingView.OnRetryClickListener {
                            override fun onRetryClicked() {
                                viewModel.retry()
                            }
                        })
            }
            ResponseStatus.LOADING -> {
                rvCollections.visibility = View.GONE
                loadingView.onLoading()
            }
        }
    }
}