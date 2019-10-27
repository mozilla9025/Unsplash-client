package app.wallpaper.modules.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    var data: List<T>? = null

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    fun updateData(data: List<T>?) {
        this.data = data
        notifyDataSetChanged()
    }

    fun isNullOrEmpty() = data?.isNullOrEmpty() ?: false

    protected fun add(item: T) {}
    protected fun remove(item: T) {}
}