package app.wallpaper.modules.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.data.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_photo.view.*

class HomeAdapter(var data: List<Photo>?) :
        RecyclerView.Adapter<HomeAdapter.PhotoItemViewHolder>() {

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

    inner class PhotoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(photo: Photo) {
            Glide.with(itemView)
                    .load(photo.urls.regular)
                    .apply(RequestOptions()
                            .placeholder(ColorDrawable(Color.parseColor(photo.color))))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.iv_image)
        }
    }
}