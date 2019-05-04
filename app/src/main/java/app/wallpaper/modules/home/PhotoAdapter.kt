package app.wallpaper.modules.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.data.Photo
import app.wallpaper.modules.base.BaseViewHolder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter(var data: List<Photo>?) : RecyclerView.Adapter<PhotoAdapter.PhotoItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        return PhotoItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        data?.get(holder.adapterPosition)?.let { photo ->
            holder.bind(photo)
        }
    }

    internal fun updateData(data: List<Photo>?) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class PhotoItemViewHolder(itemView: View) : BaseViewHolder<Photo>(itemView) {
        override fun bind(item: Photo) {

            GlideApp.with(itemView)
                    .load(item.user.avatar.medium)
                    .into(itemView.iv_avatar)

            GlideApp.with(itemView)
                    .load(item.urls.regular)
                    .placeholder(ColorDrawable(Color.parseColor(item.color)))
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(itemView.iv_image)

            itemView.tv_name.text = item.user.name
        }
    }
}