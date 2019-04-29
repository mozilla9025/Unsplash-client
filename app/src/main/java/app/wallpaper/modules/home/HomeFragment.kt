package app.wallpaper.modules.home

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
import app.wallpaper.network.responses.PhotoResponse
import app.wallpaper.network.responses.ResponseStatus
import butterknife.BindView
import butterknife.ButterKnife

class HomeFragment : BaseFragment() {

    @BindView(R.id.rv_main)
    lateinit var rvPhotos: RecyclerView

    private lateinit var adapter: HomeAdapter

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        unbinder = ButterKnife.bind(this, view)

        adapter = HomeAdapter(null)
        rvPhotos.adapter = adapter
        rvPhotos.layoutManager = LinearLayoutManager(context!!)
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
