package app.wallpaper.util.recycler

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PrefetchLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    companion object {
        const val DEFAULT_EXTRA_LAYOUT_SPACE: Int = 600
    }

    private var extraLayoutSpace: Int = -1

    constructor(context: Context,
                extraSpace: Int) : this(context) {
        this.extraLayoutSpace = extraSpace
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        if (extraLayoutSpace > 0) {
            return extraLayoutSpace
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE
    }
}