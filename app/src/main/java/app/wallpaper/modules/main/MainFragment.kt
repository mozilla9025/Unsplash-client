package app.wallpaper.modules.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import app.wallpaper.modules.base.BaseFragment
import butterknife.ButterKnife
import androidx.navigation.ui.NavigationUI
import app.wallpaper.R
import app.wallpaper.util.navigation.KeepStateNavigator
import butterknife.BindView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFragment : BaseFragment() {

    @BindView(R.id.view_bottom_navigation)
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        unbinder = ButterKnife.bind(this, view)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.main_fragment_nav_host)
        val navController = Navigation.findNavController(navHostFragment!!.requireView())

        val navigator = KeepStateNavigator(context!!, navHostFragment.childFragmentManager,
                R.id.main_fragment_nav_host)

        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.nav_bottom)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        return view
    }
}