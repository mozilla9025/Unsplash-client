package app.wallpaper.util.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val vertical: Int,
                           private val horizontal: Int,
                           private val orientation: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        with(outRect) {
            if (orientation == RecyclerView.VERTICAL) {
                if (parent.getChildAdapterPosition(view) == 0)
                    top = vertical

                left = horizontal
                right = horizontal
                bottom = vertical
            } else {
                if (parent.getChildAdapterPosition(view) == 0)
                    left = horizontal

                top = vertical
                right = horizontal
                bottom = vertical
            }
        }
    }
}