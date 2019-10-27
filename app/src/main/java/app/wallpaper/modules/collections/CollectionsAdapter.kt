package app.wallpaper.modules.collections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.domain.data.PhotoCollection
import app.wallpaper.modules.base.BaseViewHolder
import app.wallpaper.network.Retryable
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.recycler.PagingFooterViewHolder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.include_collection_name.view.*
import kotlinx.android.synthetic.main.item_collection.view.*
import kotlinx.android.synthetic.main.item_collection_album.view.*

class CollectionsAdapter(
    private var retryCallback: Retryable
) : PagedListAdapter<PhotoCollection, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var response: ResponseStatus = ResponseStatus.SUCCESS

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoCollection>() {
            override fun areItemsTheSame(
                oldItem: PhotoCollection,
                newItem: PhotoCollection
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PhotoCollection,
                newItem: PhotoCollection
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }

        private const val SINGLE_IMAGE = 0
        private const val MULTIPLE_IMAGES = 1
        private const val LOADING_FOOTER = 2
    }

    internal fun updateResponse(response: ResponseStatus) {
        if (currentList != null) {
            if (currentList!!.size > 0) {
                val previousState = this.response
                val hadFooter = hasFooter()
                this.response = response
                val hasExtraRow = hasFooter()
                if (hadFooter != hasExtraRow) {
                    if (hadFooter) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState != response) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MULTIPLE_IMAGES -> CollectionViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_collection_album, parent, false), viewType
            )
            SINGLE_IMAGE -> CollectionViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_collection, parent, false), viewType
            )
            LOADING_FOOTER -> PagingFooterViewHolder.create(parent, retryCallback)
            else -> throw IllegalStateException("No matching view holder for type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(holder.adapterPosition)) {
            SINGLE_IMAGE -> getItem(holder.adapterPosition)?.let {
                (holder as CollectionViewHolder).bind(it)
            }
            MULTIPLE_IMAGES -> getItem(holder.adapterPosition)?.let {
                (holder as CollectionViewHolder).bind(it)
            }
            LOADING_FOOTER -> (holder as PagingFooterViewHolder).bind(response)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean = response != ResponseStatus.SUCCESS

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter() && position == itemCount - 1) LOADING_FOOTER
        else getItem(position).let { item ->
            if (item!!.previews.count() < 3)
                SINGLE_IMAGE
            else
                MULTIPLE_IMAGES
        }
    }

    inner class CollectionViewHolder(itemView: View, private var viewType: Int) :
        BaseViewHolder<PhotoCollection>(itemView) {

        override fun bind(item: PhotoCollection) {

            when (viewType) {
                SINGLE_IMAGE -> bindSingleImage(item)
                MULTIPLE_IMAGES -> bindMultipleImages(item)
            }

            itemView.tv_collection_name.text = item.title
            itemView.tv_count_and_author.text = String.format(
                itemView.context.getString(R.string.CollectionsAdapter_Count_and_Author_format),
                item.totalPhotos.toString(), item.user.name
            )
        }

        private fun bindSingleImage(item: PhotoCollection) {
            GlideApp.with(itemView)
                .load(item.coverPhoto.urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.iv_cover)
        }

        private fun bindMultipleImages(item: PhotoCollection) {
            GlideApp.with(itemView)
                .load(item.previews[0].urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.iv_preview1)

            GlideApp.with(itemView)
                .load(item.previews[1].urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.iv_preview2)

            GlideApp.with(itemView)
                .load(item.previews[2].urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.iv_preview3)
        }
    }
}