package app.wallpaper.modules.collections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import app.wallpaper.R
import app.wallpaper.modules.base.SelectableFragment
import app.wallpaper.util.annotation.Layout

@Layout(R.layout.fragment_nav_collections)
class CollectionsNavFragment : SelectableFragment() {

    private var navHostFragment: Fragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navHostFragment = childFragmentManager.findFragmentById(R.id.collections_nav_host)
        val navController = Navigation.findNavController(navHostFragment!!.view!!)

        val navigator = FragmentNavigator(
                context!!,
                navHostFragment!!.childFragmentManager,
                R.id.collections_nav_host
        )

        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.nav_collections)
    }

    override fun onFragmentSelected() {
        navHostFragment?.childFragmentManager?.fragments!!.forEach {
            if (it is SelectableFragment)
                it.onFragmentSelected()
        }
    }
}