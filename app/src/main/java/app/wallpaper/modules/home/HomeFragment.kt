package app.wallpaper.modules.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.modules.home.collections.CollectionsFragment
import app.wallpaper.modules.home.photos.PhotosFragment
import app.wallpaper.util.viewpager.ViewPagerAdapter
import butterknife.BindView
import butterknife.ButterKnife

class HomeFragment : BaseFragment() {

    @BindView(R.id.vp_main)
    lateinit var viewPager: ViewPager

    private lateinit var vpAdapter: ViewPagerAdapter
    private lateinit var photosFragment: PhotosFragment
    private lateinit var categoriesFragment: CollectionsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        unbinder = ButterKnife.bind(this, view)

        photosFragment = PhotosFragment()
        categoriesFragment = CollectionsFragment()

        vpAdapter = ViewPagerAdapter(childFragmentManager)
        vpAdapter.apply {
            addFragment(photosFragment, "photos")
            addFragment(categoriesFragment, "categories")
        }
        viewPager.adapter = vpAdapter
        return view
    }

}
