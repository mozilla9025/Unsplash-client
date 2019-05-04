package app.wallpaper.modules.collections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.data.Collection
import app.wallpaper.modules.base.BaseViewHolder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.include_collection_name.view.*
import kotlinx.android.synthetic.main.item_collection.view.*
import kotlinx.android.synthetic.main.item_collection_album.view.*

class CollectionsAdapter(private var data: List<Collection>?) : RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder>() {

    companion object {
        private const val SINGLE_IMAGE: Int = 0
        private const val MULTIPLE_IMAGES: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val layout = when (viewType) {
            MULTIPLE_IMAGES -> R.layout.item_collection_album
            else -> R.layout.item_collection
        }
        return CollectionViewHolder(LayoutInflater.from(parent.context)
                .inflate(layout, parent, false), viewType)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        data?.get(holder.adapterPosition)?.let { collection ->
            holder.bind(collection)
        }
    }

    override fun getItemViewType(position: Int): Int {
        data?.get(position)?.let { item ->
            return if (item.previews.count() < 3)
                SINGLE_IMAGE
            else
                MULTIPLE_IMAGES
        }
        return SINGLE_IMAGE
    }

    fun updateData(data: List<Collection>?) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CollectionViewHolder(itemView: View, viewType: Int) : BaseViewHolder<Collection>(itemView) {

        var type: Int? = null

        init {
            this.type = viewType
        }

        override fun bind(item: Collection) {
            when (type) {
                SINGLE_IMAGE -> bindSingleImage(item)
                MULTIPLE_IMAGES -> bindMultipleImages(item)
            }

            itemView.tv_collection_name.text = item.title
            itemView.tv_count_and_author.text = String.format(itemView.context.getString(R.string.CollectionsAdapter_Count_and_Author_format),
                    item.totalPhotos.toString(), item.user.name)
        }

        private fun bindSingleImage(item: Collection) {
            GlideApp.with(itemView)
                    .load(item.coverPhoto.urls.regular)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.iv_cover)
        }

        private fun bindMultipleImages(item: Collection) {
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
        }
    }
}