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
import app.wallpaper.data.Collection
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.network.responses.CollectionResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import app.wallpaper.widget.LoadingView
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

        adapter = CollectionsAdapter(null)
        rvCollections.adapter = adapter
        rvCollections.layoutManager = LinearLayoutManager(context!!)
        rvCollections.addItemDecoration(MarginItemDecoration(4.dp, 0.dp))

        observeData()
        viewModel.getCollections()
        return view
    }

    private fun observeData() {
        viewModel.photosLiveData.observe(viewLifecycleOwner, Observer { response -> handleResponse(response) })
    }

    private fun handleResponse(response: CollectionResponse?) {
        when (response?.status) {
            ResponseStatus.SUCCESS -> onSuccess(response.data!!)
            ResponseStatus.LOADING -> onLoading()
            ResponseStatus.FAILURE -> onFailure(response.error?.message!!, object : LoadingView.OnRetryClickListener {
                override fun onRetryClicked() {
                    viewModel.getCollections()
                }
            })
        }
    }

    private fun onSuccess(data: List<Collection>) {
        loadingView.onSuccess()
        adapter.updateData(data)
        rvCollections.visibility = View.VISIBLE
    }

    private fun onLoading() {
        loadingView.onLoading()
        rvCollections.visibility = View.GONE
    }

    private fun onFailure(error: String?, retryCallback: LoadingView.OnRetryClickListener?) {
        loadingView.onError(error
                ?: getString(R.string.Api_Call_Default_Error_Message), retryCallback)
        rvCollections.visibility = View.GONE
    }

}