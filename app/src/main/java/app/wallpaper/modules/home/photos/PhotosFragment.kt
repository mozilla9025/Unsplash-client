package app.wallpaper.modules.home.photos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.modules.home.HomeViewModel
import app.wallpaper.network.responses.PhotoResponse
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.MarginItemDecoration
import app.wallpaper.util.extentions.dp
import butterknife.BindView
import butterknife.ButterKnife

class PhotosFragment : BaseFragment() {

    @BindView(R.id.rv_photos)
    lateinit var rvPhotos: RecyclerView

    private lateinit var adapter: PhotoAdapter

    private val viewModel: PhotosViewModel by lazy {
        ViewModelProviders.of(this).get(PhotosViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_photos, container, false)
        unbinder = ButterKnife.bind(this, view)

        adapter = PhotoAdapter(null)
        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = LinearLayoutManager(context!!)
        rvPhotos.addItemDecoration(MarginItemDecoration(8.dp, 0.dp))
        observeData()

        viewModel.getPhotos()
        return view
    }

    private fun observeData() {
        viewModel.photosLiveData.observe(this, Observer<PhotoResponse> { res -> handlePhotoLoaded(res) })
    }

    private fun handlePhotoLoaded(response: PhotoResponse) {
        when (response.status) {
            ResponseStatus.SUCCESS -> adapter.updateData(response.data)
            ResponseStatus.FAILURE -> Toast.makeText(context, response.error?.toString(), Toast.LENGTH_SHORT).show()
            ResponseStatus.LOADING -> Log.i("FRAGMENT", "loading")
        }
    }
}