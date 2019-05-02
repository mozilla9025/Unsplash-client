package app.wallpaper.modules.home.collections

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.data.Collection
import app.wallpaper.modules.base.BaseViewHolder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_collection_album.view.*

class CollectionsAdapter(private var data: List<Collection>?) : RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_collection_album, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        data?.get(holder.adapterPosition)?.let { collection ->
            holder.bind(collection)
        }
    }

    fun updateData(data: List<Collection>?) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CollectionViewHolder(itemView: View) : BaseViewHolder<Collection>(itemView) {
        override fun bind(item: Collection) {
            GlideApp.with(itemView)
                    .load(item.previews[0].urls.regular)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.iv_preview1)

            GlideApp.with(itemView)
                    .load(item.previews[1].urls.regular)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.iv_preview2)

            GlideApp.with(itemView)
                    .load(item.previews[2].urls.regular)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.iv_preview3)

            itemView.tv_collection_name.text = item.title
            itemView.tv_count_and_author.text = "${item.totalPhotos} photos by ${item.user.name}"

        }
    }
}