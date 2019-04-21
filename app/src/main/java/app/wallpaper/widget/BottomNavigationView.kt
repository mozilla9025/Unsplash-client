package app.wallpaper.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import app.wallpaper.R
import butterknife.ButterKnife
import butterknife.Unbinder

class BottomNavigationView : ConstraintLayout {

    private var unbinder: Unbinder

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)

    init {
        val view = View.inflate(context, R.layout.view_bottom_navigation, this)
        unbinder = ButterKnife.bind(this, view)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        unbinder?.unbind()
    }
}