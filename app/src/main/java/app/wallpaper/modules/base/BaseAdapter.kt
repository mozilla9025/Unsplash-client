package app.wallpaper.modules.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    lateinit var data: List<T>

    protected fun updateData(data: List<T>) {}
    protected fun add(item: T) {}
    protected fun remove(item: T) {}
}