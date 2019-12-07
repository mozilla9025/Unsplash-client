package app.wallpaper.modules.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.app.GlideApp
import app.wallpaper.domain.data.Photo
import app.wallpaper.modules.base.BaseViewHolder
import app.wallpaper.network.responses.ResponseStatus
import app.wallpaper.util.recycler.PagingFooterViewHolder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_photo.view.*


class PhotoAdapter(
        private var retryAction: () -> Unit,
        private val clickListener: (Photo) -> Unit
) : PagedListAdapter<Photo, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var response: ResponseStatus = ResponseStatus.SUCCESS

    fun updateResponse(response: ResponseStatus) {
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

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean = response != ResponseStatus.SUCCESS

    override fun getItemViewType(position: Int): Int = if (hasFooter() && position == itemCount - 1) LOADING_FOOTER else PHOTO_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PHOTO_ITEM) PhotoItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false))
        else PagingFooterViewHolder.create(parent, retryAction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(holder.adapterPosition)) {
            PHOTO_ITEM -> getItem(holder.adapterPosition)?.let { photo ->
                (holder as PhotoItemViewHolder).bind(photo)
            }
            LOADING_FOOTER -> (holder as PagingFooterViewHolder).bind(response)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }
        }

        private const val PHOTO_ITEM = 0
        private const val LOADING_FOOTER = 1
    }

    inner class PhotoItemViewHolder(itemView: View) : BaseViewHolder<Photo>(itemView) {
        override fun bind(item: Photo) {

            itemView.setOnClickListener { clickListener.invoke(item) }

            GlideApp.with(itemView)
                    .load(item.user?.avatar?.medium)
                    .into(itemView.iv_avatar)

            GlideApp.with(itemView)
                    .load(item.urls.regular)
                    .placeholder(ColorDrawable(Color.parseColor(item.color)))
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(itemView.iv_image)

            itemView.tv_name.text = item.user?.name
        }
    }
}