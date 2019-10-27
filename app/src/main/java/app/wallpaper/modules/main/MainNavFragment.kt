package app.wallpaper.modules.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.navigation.KeepStateNavigator
import kotlinx.android.synthetic.main.fragment_main.*

@Layout(R.layout.fragment_main)
class MainNavFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navHostFragment = childFragmentManager.findFragmentById(R.id.main_fragment_nav_host)
        val navController = Navigation.findNavController(navHostFragment!!.view!!)

        val navigator = KeepStateNavigator(
                context!!,
                navHostFragment.childFragmentManager,
                R.id.main_fragment_nav_host
        )

        navigator.currentFragmentListener = object : KeepStateNavigator.CurrentFragmentListener {
            override fun onCurrentFragmentSelected(selectedFragment: Fragment) {

            }
        }

        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.nav_bottom)
        NavigationUI.setupWithNavController(viewBottomNavigation, navController)
    }
}