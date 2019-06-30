package app.wallpaper.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import app.wallpaper.R
import kotlinx.android.synthetic.main.view_icon_textview.view.*

class IconTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var icon: Drawable? = null
        set(value) {
            field = value
            view.ivIcon.setImageDrawable(value)
        }

    var title: CharSequence? = null
        set(value) {
            field = value
            if (title != null) {
                view.tvTitle.visibility = View.VISIBLE
                view.tvTitle.text = value
            } else {
                view.tvTitle.visibility = View.GONE
            }
        }

    var subTitle: CharSequence? = null
        set(value) {
            field = value
            if (value != null) {
                view.tvSubtitle.visibility = View.VISIBLE
                view.tvSubtitle.text = value
            } else {
                view.tvSubtitle.visibility = View.GONE
            }
        }

    private var view: View = inflate(context, R.layout.view_icon_textview, this)

    init {

        val arr = context.obtainStyledAttributes(attrs, R.styleable.IconTextView)

        icon = arr.getDrawable(R.styleable.IconTextView_itv_icon)
        title = arr.getString(R.styleable.IconTextView_itv_title)
        subTitle = arr.getString(R.styleable.IconTextView_itv_subtitle)

        arr.recycle()
    }
}