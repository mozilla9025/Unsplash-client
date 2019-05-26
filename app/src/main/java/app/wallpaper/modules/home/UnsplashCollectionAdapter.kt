package app.wallpaper.modules.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.modules.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_unsplash_collection.view.*

class UnsplashCollectionAdapter : RecyclerView.Adapter<UnsplashCollectionAdapter.UnsplashCollectionViewHolder>() {

    private var data: List<PhotoCollection>? = null

    internal fun updateData(data: List<PhotoCollection>?) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashCollectionViewHolder =
            UnsplashCollectionViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_unsplash_collection, parent, false))

    override fun getItemCount(): Int = data?.count() ?: 0

    override fun onBindViewHolder(holder: UnsplashCollectionViewHolder, position: Int) {
        data?.get(holder.adapterPosition)?.let { holder.bind(it) }
    }

    inner class UnsplashCollectionViewHolder(itemView: View) : BaseViewHolder<PhotoCollection>(itemView) {
        override fun bind(item: PhotoCollection) {
            itemView.tv_unsplash_collection.text = item.title
        }
    }
}