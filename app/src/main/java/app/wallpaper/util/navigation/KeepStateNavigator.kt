package app.wallpaper.util.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import app.wallpaper.util.extentions.logd
import java.util.*

@Navigator.Name("keep_state_fragment") // `keep_state_fragment` is used in navigation xml
class KeepStateNavigator(
        private val context: Context,
        private val manager: FragmentManager, // Should pass childFragmentManager.
        private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    private val path: LinkedList<Fragment> = LinkedList()
    var currentFragmentListener: CurrentFragmentListener? = null

    override fun navigate(
            destination: Destination,
            args: Bundle?,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
    ): NavDestination? {

        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        var commitTransaction = true
        val fragmentExists: Boolean

        var fragment: Fragment? = manager.findFragmentByTag(tag)

        fragmentExists = fragment != null

        if (!fragmentExists) {
            val className = destination.className
            fragment = instantiateFragment(context, manager, className, args)
        }

        if (path.isNotEmpty()) {
            commitTransaction = path.last != fragment
            if (!commitTransaction) {
                currentFragmentListener?.onCurrentFragmentSelected(fragment!!)
            }
        }

        if (commitTransaction) {
            val currentFragment = manager.primaryNavigationFragment

            if (currentFragment != null) {
                transaction.hide(currentFragment)
            }

            if (!fragmentExists) {
                transaction.add(containerId, fragment!!, tag)
            } else {
                transaction.show(fragment!!)
            }

            if (path.size >= PATH_HISTORY)
                path.removeFirst()

            path.add(fragment)

            transaction.run {
                setPrimaryNavigationFragment(fragment)
                setReorderingAllowed(true)
                commit()
            }
        }

        logd("${destination.className}")

        return destination
    }

    interface CurrentFragmentListener {
        fun onCurrentFragmentSelected(selectedFragment: Fragment)
    }

    companion object {
        private const val PATH_HISTORY = 5
    }
}
