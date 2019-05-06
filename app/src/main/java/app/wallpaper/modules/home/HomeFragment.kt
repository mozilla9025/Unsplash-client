package app.wallpaper.modules.home

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

class HomeFragment : BaseFragment() {

    @BindView(R.id.rv_photos)
    lateinit var rvPhotos: RecyclerView
    @BindView(R.id.loading_home)
    lateinit var loadingView: LoadingView

    private lateinit var adapter: PhotoAdapter

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        unbinder = ButterKnife.bind(this, view)

        adapter = PhotoAdapter(object : Retryable {
            override fun retry() {
                viewModel.retry()
            }
        })

        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rvPhotos.addItemDecoration(MarginItemDecoration(4.dp, 0.dp))
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
                rvPhotos.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                rvPhotos.visibility = View.GONE
                loadingView.onError(response.error?.message
                        ?: getString(R.string.Api_Call_Default_Error_Message), object : LoadingView.OnRetryClickListener {
                    override fun onRetryClicked() {
                        viewModel.retry()
                    }
                })
            }
            ResponseStatus.LOADING -> {
                rvPhotos.visibility = View.GONE
                loadingView.onLoading()
            }
        }
    }
}
