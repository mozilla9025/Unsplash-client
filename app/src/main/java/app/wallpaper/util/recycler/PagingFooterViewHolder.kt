package app.wallpaper.util.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.wallpaper.R
import app.wallpaper.modules.base.BaseViewHolder
import app.wallpaper.network.responses.ResponseStatus
import kotlinx.android.synthetic.main.item_loading_footer.view.*

class PagingFooterViewHolder(itemView: View, private val retryCallback: () -> Unit) : BaseViewHolder<ResponseStatus>(itemView) {

    override fun bind(item: ResponseStatus) {
        when (item) {
            ResponseStatus.SUCCESS -> {
                itemView.tv_error.visibility = View.GONE
                itemView.btn_retry.visibility = View.GONE
                itemView.progress_view.visibility = View.GONE
            }
            ResponseStatus.LOADING -> {
                itemView.tv_error.visibility = View.INVISIBLE
                itemView.btn_retry.visibility = View.INVISIBLE
                itemView.progress_view.visibility = View.VISIBLE
            }
            ResponseStatus.FAILURE -> {
                itemView.progress_view.visibility = View.INVISIBLE
                itemView.tv_error.text = itemView.context.getString(R.string.Api_Call_Default_Error_Message)
                itemView.tv_error.visibility = View.VISIBLE

                if (retryCallback == null) {
                    itemView.btn_retry.visibility = View.GONE
                } else {
                    itemView.btn_retry.visibility = View.VISIBLE
                    itemView.btn_retry.setOnClickListener { retryCallback.invoke() }
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PagingFooterViewHolder =
                PagingFooterViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_loading_footer, parent, false), retry)
    }
}