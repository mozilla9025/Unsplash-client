package app.wallpaper.modules.home.collections

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.wallpaper.R
import app.wallpaper.data.Collection
import app.wallpaper.modules.base.BaseViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_collection.view.*

class CollectionsAdapter(private var data: List<Collection>?) : RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_collection, parent, false))
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
            Glide.with(itemView)
                    .load(item.coverPhoto.urls.regular)
                    .apply(RequestOptions()
                            .placeholder(ColorDrawable(Color.parseColor(item.coverPhoto.color))))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.iv_cover)
        }
    }
}