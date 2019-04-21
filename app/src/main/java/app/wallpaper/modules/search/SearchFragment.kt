package app.wallpaper.modules.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.wallpaper.R
import app.wallpaper.modules.BaseFragment
import butterknife.ButterKnife

class SearchFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

}