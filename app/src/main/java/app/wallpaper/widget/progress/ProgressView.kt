package app.wallpaper.widget.progress

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import app.wallpaper.R

class ProgressView : View {

    private var autostart = false
    private var circular = true
    private var progressId: Int = 0


    private var progressDrawable: Drawable? = null

    val progressMode: Int
        get() = if (circular)
            (progressDrawable as CircularProgressDrawable).progressMode
        else
            (progressDrawable as LinearProgressDrawable).progressMode

    /**
     * @return The current progress of this view in [0..1] range.
     */
    /**
     * Set the current progress of this view.
     * @param percent The progress value in [0..1] range.
     */
    var progress: Float
        get() = if (circular)
            (progressDrawable as CircularProgressDrawable).progress
        else
            (progressDrawable as LinearProgressDrawable).progress
        set(percent) {
            if (circular)
                (progressDrawable as CircularProgressDrawable).progress = percent
            else
                (progressDrawable as LinearProgressDrawable).progress = percent
        }

    /**
     * @return The current secondary progress of this view in [0..1] range.
     */
    /**
     * Set the current secondary progress of this view.
     * @param percent The progress value in [0..1] range.
     */
    var secondaryProgress: Float
        get() = if (circular)
            (progressDrawable as CircularProgressDrawable).secondaryProgress
        else
            (progressDrawable as LinearProgressDrawable).secondaryProgress
        set(percent) {
            if (circular)
                (progressDrawable as CircularProgressDrawable).secondaryProgress = percent
            else
                (progressDrawable as LinearProgressDrawable).secondaryProgress = percent
        }

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        applyStyle(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun needCreateProgress(circular: Boolean): Boolean {
        if (progressDrawable == null)
            return true

        return if (circular)
            progressDrawable !is CircularProgressDrawable
        else
            progressDrawable !is LinearProgressDrawable
    }

    private fun applyStyle(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, defStyleRes)

        var progressId = 0
        var progressMode = -1
        var progress = -1f
        var secondaryProgress = -1f

        var i = 0
        val count = a.indexCount
        while (i < count) {
            when (val attr = a.getIndex(i)) {
                R.styleable.ProgressView_pv_autostart -> autostart = a.getBoolean(attr, false)
                R.styleable.ProgressView_pv_circular -> circular = a.getBoolean(attr, true)
                R.styleable.ProgressView_pv_progressStyle -> progressId = a.getResourceId(attr, 0)
                R.styleable.ProgressView_pv_progressMode -> progressMode = a.getInteger(attr, 0)
                R.styleable.ProgressView_pv_progress -> progress = a.getFloat(attr, 0f)
                R.styleable.ProgressView_pv_secondaryProgress -> secondaryProgress = a.getFloat(attr, 0f)
            }
            i++
        }

        a.recycle()

        var needStart = false

        if (needCreateProgress(circular)) {
            this.progressId = progressId
            if (this.progressId == 0)
                this.progressId = if (circular) R.style.CircularProgress else R.style.LinearProgress

            needStart = progressDrawable != null && (progressDrawable as Animatable).isRunning
            progressDrawable = if (circular) CircularProgressDrawable.Builder(context, this.progressId).build() else LinearProgressDrawable.Builder(context, this.progressId).build()
            background = progressDrawable
        } else if (this.progressId != progressId) {
            this.progressId = progressId
            if (progressDrawable is CircularProgressDrawable)
                (progressDrawable as CircularProgressDrawable).applyStyle(context, this.progressId)
            else
                (progressDrawable as LinearProgressDrawable).applyStyle(context, this.progressId)
        }

        if (progressMode >= 0) {
            if (progressDrawable is CircularProgressDrawable)
                (progressDrawable as CircularProgressDrawable).progressMode = progressMode
            else
                (progressDrawable as LinearProgressDrawable).progressMode = progressMode
        }

        if (needStart)
            start()
    }

    override fun onVisibilityChanged(@NonNull changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)

        if (changedView !== this)
            return

        if (autostart) {
            if (visibility == GONE || visibility == INVISIBLE)
                stop()
            else
                start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (visibility == VISIBLE && autostart)
            start()

    }

    override fun onDetachedFromWindow() {
        if (autostart)
            stop()

        super.onDetachedFromWindow()
    }

    /**
     * Start showing progress.
     */
    fun start() {
        if (progressDrawable != null)
            (progressDrawable as Animatable).start()
    }

    /**
     * Stop showing progress.
     */
    fun stop() {
        if (progressDrawable != null)
            (progressDrawable as Animatable).stop()
    }

    companion object {
        const val MODE_DETERMINATE = 0
        const val MODE_INDETERMINATE = 1
        const val MODE_BUFFER = 2
        const val MODE_QUERY = 3
    }

}