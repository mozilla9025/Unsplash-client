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
import app.wallpaper.data.Photo
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.network.responses.PhotoResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.recycler.MarginItemDecoration
import app.wallpaper.widget.LoadingView
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

        adapter = PhotoAdapter(null)
        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rvPhotos.addItemDecoration(MarginItemDecoration(4.dp, 0.dp))

        observeData()

        viewModel.getPhotos()
        return view
    }

    private fun observeData() {
        viewModel.photosLiveData.observe(viewLifecycleOwner, Observer<PhotoResponse> { res -> handlePhotoLoaded(res) })
    }

    private fun handlePhotoLoaded(response: PhotoResponse) {
        when (response.status) {
            ResponseStatus.SUCCESS -> onSuccess(response.data!!)
            ResponseStatus.FAILURE -> response.error?.message?.let { onFailure(it) }
            ResponseStatus.LOADING -> onLoading()
        }
    }

    private fun onSuccess(data: List<Photo>) {
        loadingView.onSuccess()
        adapter.updateData(data)
        rvPhotos.visibility = View.VISIBLE
    }

    private fun onLoading() {
        loadingView.onLoading()
        rvPhotos.visibility = View.GONE
    }

    private fun onFailure(error: String) {
        loadingView.onError(error)
        rvPhotos.visibility = View.GONE
    }
}
