package app.wallpaper.modules.likes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment
import butterknife.ButterKnife

class LikesFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_likes, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

}
