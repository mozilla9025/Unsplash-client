package app.wallpaper.modules.home.collections

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
import app.wallpaper.network.responses.CollectionResponse
import app.wallpaper.network.responses.ResponseStatus
import butterknife.BindView
import butterknife.ButterKnife

class CollectionsFragment : BaseFragment() {

    @BindView(R.id.rv_collections)
    lateinit var rvCollections: RecyclerView

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

        observeData()
        viewModel.getCollections()
        return view
    }

    private fun observeData() {
        viewModel.photosLiveData.observe(viewLifecycleOwner, Observer { response -> handleResponse(response) })
    }

    private fun handleResponse(response: CollectionResponse?) {
        when (response?.status) {
            ResponseStatus.SUCCESS -> adapter.updateData(response.data)
            ResponseStatus.LOADING -> Log.i("Collections", "loading")
            ResponseStatus.FAILURE -> Toast.makeText(context!!, response.error?.message, Toast.LENGTH_SHORT).show()
        }
    }
}