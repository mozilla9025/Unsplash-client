package app.wallpaper.modules.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.domain.data.Photo
import app.wallpaper.modules.base.BaseAdapter
import app.wallpaper.modules.base.BaseViewHolder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_related_photo.view.*

class RelatedPhotosAdapter(clickListener: (Photo) -> Unit) : BaseAdapter<Photo>(clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedPhotoViewHolder {
        return RelatedPhotoViewHolder(LayoutInflater.from(parent.context!!)
                .inflate(R.layout.item_related_photo, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Photo>, position: Int) {
        data?.get(holder.adapterPosition)?.let {
            holder.bind(it)
        }
    }

    inner class RelatedPhotoViewHolder(itemView: View) : BaseViewHolder<Photo>(itemView) {

        override fun bind(item: Photo) {
            itemView.setOnClickListener { clickListener!!.invoke(item) }

            GlideApp.with(itemView)
                    .load(item.urls.regular)
                    .placeholder(ColorDrawable(Color.parseColor(item.color!!)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.ivRelated)
        }
    }
}