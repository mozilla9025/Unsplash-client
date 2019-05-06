package app.wallpaper.widget.progress

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import app.wallpaper.R
import kotlinx.android.synthetic.main.view_loading.view.*

class LoadingView @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_loading, this, true)
    }

    fun onError(error: String, retryClickListener: OnRetryClickListener?) {
        tv_error.text = error
        progress_view.visibility = View.GONE
        tv_error.visibility = View.VISIBLE
        if (retryClickListener != null) {
            btn_retry.visibility = View.VISIBLE
            btn_retry.setOnClickListener { retryClickListener.onRetryClicked() }
        } else {
            btn_retry.visibility = View.GONE
        }
        visibility = View.VISIBLE
    }

    fun onLoading() {
        tv_error.visibility = View.GONE
        btn_retry.visibility = View.GONE
        progress_view.visibility = View.VISIBLE
        visibility = View.VISIBLE
    }

    fun onSuccess() {
        visibility = View.GONE
    }

    interface OnRetryClickListener {
        fun onRetryClicked()
    }
}