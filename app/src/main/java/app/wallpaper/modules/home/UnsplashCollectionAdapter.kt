package app.wallpaper.modules.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.wallpaper.R
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.modules.base.BaseAdapter
import app.wallpaper.modules.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_unsplash_collection.view.*

class UnsplashCollectionAdapter : BaseAdapter<PhotoCollection>() {

    override fun onBindViewHolder(holder: BaseViewHolder<PhotoCollection>, position: Int) {
        data?.get(holder.adapterPosition)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashCollectionViewHolder =
            UnsplashCollectionViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_unsplash_collection, parent, false))

    inner class UnsplashCollectionViewHolder(itemView: View) : BaseViewHolder<PhotoCollection>(itemView) {
        override fun bind(item: PhotoCollection) {
            itemView.tv_unsplash_collection.text = item.title
        }
    }
}