package app.wallpaper.modules.home.photos

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.data.Photo
import app.wallpaper.modules.base.BaseAdapter
import app.wallpaper.modules.base.BaseViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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

            val placeholder = ColorDrawable(Color.parseColor(item.color))

            Glide.with(itemView)
                    .load(item.urls.regular)
                    .apply(RequestOptions()
                            .placeholder(placeholder)
                            .centerCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(itemView.iv_image)

            Glide.with(itemView)
                    .load(item.user.avatar.medium)
                    .apply(RequestOptions()
                            .placeholder(placeholder)
                            .centerCrop())
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(itemView.iv_avatar)

            itemView.tv_name.text = item.user.name
        }
    }
}