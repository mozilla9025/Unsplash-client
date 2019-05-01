package app.wallpaper.util.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentManager
import kotlin.collections.ArrayList

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragmentList: MutableList<Pair<Fragment, String>> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position].first
    }

    override fun getCount(): Int {
        return fragmentList.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].second
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(Pair(fragment, title))
    }

    fun clearReference() {
        fragmentList.clear()
    }
}