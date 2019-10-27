package app.wallpaper.modules.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import app.wallpaper.R
import app.wallpaper.modules.base.BaseFragment
import app.wallpaper.util.annotation.Layout
import app.wallpaper.util.navigation.KeepStateNavigator

@Layout(R.layout.fragment_nav_home)
class HomeNavFragment : BaseFragment() {

    private var navHostFragment: Fragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navHostFragment = childFragmentManager.findFragmentById(R.id.home_nav_host)
        val navController = Navigation.findNavController(navHostFragment!!.view!!)

        val navigator = KeepStateNavigator(
            context!!,
            navHostFragment!!.childFragmentManager,
            R.id.home_nav_host
        )

        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(navController.navInflater.inflate(R.navigation.nav_home), arguments)
    }
}