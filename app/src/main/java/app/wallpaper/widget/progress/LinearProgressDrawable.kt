package app.wallpaper.widget.progress

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import app.wallpaper.R
import app.wallpaper.util.ColorUtils
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.extentions.f
import app.wallpaper.util.extentions.l

class LinearProgressDrawable private constructor(progressPercent: Float,
                                                 secondaryProgressPercent: Float,
                                                 private var mMaxLineWidth: Float,
                                                 private var mMaxLineWidthPercent: Float,
                                                 private var mMinLineWidth: Float,
                                                 private var mMinLineWidthPercent: Float,
                                                 private var mStrokeSize: Float,
                                                 private var mVerticalAlign: Int,
                                                 private var mStrokeColors: IntArray?,
                                                 private var mStrokeSecondaryColor: Int,
                                                 private var mReverse: Boolean,
                                                 private var mTravelDuration: Int,
                                                 private var mTransformDuration: Int,
                                                 private var mKeepDuration: Int,
                                                 private var mTransformInterpolator: Interpolator?,
                                                 private var mProgressMode: Int,
                                                 private var mInAnimationDuration: Int,
                                                 private var mOutAnimationDuration: Int) : Drawable(), Animatable {

    private var mLastUpdateTime: Long = 0
    private var mLastProgressStateTime: Long = 0
    private var mLastRunStateTime: Long = 0

    private var mProgressState: Int = 0

    private var mRunState = RUN_STATE_STOPPED

    private val mPaint: Paint
    private var mStartLine: Float = 0.toFloat()
    private var mLineWidth: Float = 0.toFloat()
    private var mStrokeColorIndex: Int = 0
    private var mAnimTime: Float = 0.toFloat()

    private val mPath: Path
    private var mPathEffect: DashPathEffect? = null

    var progress: Float = 0.toFloat()
        set(percent) {
            var percent = percent
            percent = Math.min(1f, Math.max(0f, percent))
            if (progress != percent) {
                field = percent
                if (isRunning)
                    invalidateSelf()
                else if (progress != 0f)
                    start()
            }
        }
    var secondaryProgress: Float = 0.toFloat()
        set(percent) {
            var percent = percent
            percent = Math.min(1f, Math.max(0f, percent))
            if (secondaryProgress != percent) {
                field = percent
                if (isRunning)
                    invalidateSelf()
                else if (secondaryProgress != 0f)
                    start()
            }
        }

    private val indeterminateStrokeColor: Int
        get() {
            if (mProgressState != PROGRESS_STATE_KEEP_SHRINK || mStrokeColors!!.size == 1)
                return mStrokeColors!![mStrokeColorIndex]

            val value = Math.max(0f, Math.min(1f, (SystemClock.uptimeMillis() - mLastProgressStateTime) as Float / mKeepDuration))
            val prev_index = if (mStrokeColorIndex == 0) mStrokeColors!!.size - 1 else mStrokeColorIndex - 1

            return ColorUtils.getMiddleColor(mStrokeColors!![prev_index], mStrokeColors!![mStrokeColorIndex], value)
        }

    private val pathEffect: PathEffect?
        get() {
            if (mPathEffect == null)
                mPathEffect = DashPathEffect(floatArrayOf(0.1f, (mStrokeSize * 2)), 0f)

            return mPathEffect
        }

    private val queryStrokeColor: Int
        get() = ColorUtils.getColor(mStrokeColors!![0], mAnimTime)

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    var progressMode: Int
        get() = mProgressMode
        set(mode) {
            if (mProgressMode != mode) {
                mProgressMode = mode
                invalidateSelf()
            }
        }

    override fun isRunning(): Boolean = mRunState != RUN_STATE_STOPPED

    private val mUpdater = Runnable { update() }

    init {
        progress = progressPercent
        secondaryProgress = secondaryProgressPercent

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND

        mPath = Path()
    }

    fun applyStyle(context: Context, resId: Int) {
        val a = context.obtainStyledAttributes(resId, R.styleable.LinearProgressDrawable)

        var strokeColor = 0
        var strokeColorDefined = false
        var strokeColors: IntArray? = null

        var i = 0
        val count = a.indexCount
        while (i < count) {
            val attr = a.getIndex(i)

            if (attr == R.styleable.LinearProgressDrawable_pv_progress)
                progress = a.getFloat(attr, 0f)
            else if (attr == R.styleable.LinearProgressDrawable_pv_secondaryProgress)
                secondaryProgress = a.getFloat(attr, 0f)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_maxLineWidth) {
                val value = a.peekValue(attr)
                if (value.type == TypedValue.TYPE_FRACTION) {
                    mMaxLineWidthPercent = a.getFraction(attr, 1, 1, 0.75f)
                    mMaxLineWidth = 0f
                } else {
                    mMaxLineWidth = a.getDimensionPixelSize(attr, 0).f
                    mMaxLineWidthPercent = 0f
                }
            } else if (attr == R.styleable.LinearProgressDrawable_lpd_minLineWidth) {
                val value = a.peekValue(attr)
                if (value.type == TypedValue.TYPE_FRACTION) {
                    mMinLineWidthPercent = a.getFraction(attr, 1, 1, 0.25f)
                    mMinLineWidth = 0f
                } else {
                    mMinLineWidth = a.getDimensionPixelSize(attr, 0).f
                    mMinLineWidthPercent = 0f
                }
            } else if (attr == R.styleable.LinearProgressDrawable_lpd_strokeSize)
                mStrokeSize = a.getDimensionPixelSize(attr, 0).f
            else if (attr == R.styleable.LinearProgressDrawable_lpd_verticalAlign)
                mVerticalAlign = a.getInteger(attr, 0)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_strokeColor) {
                strokeColor = a.getColor(attr, 0)
                strokeColorDefined = true
            } else if (attr == R.styleable.LinearProgressDrawable_lpd_strokeColors) {
                val ta = context.resources.obtainTypedArray(a.getResourceId(attr, 0))
                strokeColors = IntArray(ta.length())
                for (j in 0 until ta.length())
                    strokeColors[j] = ta.getColor(j, 0)
                ta.recycle()
            } else if (attr == R.styleable.LinearProgressDrawable_lpd_strokeSecondaryColor)
                mStrokeSecondaryColor = a.getColor(attr, 0)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_reverse)
                mReverse = a.getBoolean(attr, false)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_travelDuration)
                mTravelDuration = a.getInteger(attr, 0)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_transformDuration)
                mTransformDuration = a.getInteger(attr, 0)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_keepDuration)
                mKeepDuration = a.getInteger(attr, 0)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_transformInterpolator)
                mTransformInterpolator = AnimationUtils.loadInterpolator(context, a.getResourceId(attr, 0))
            else if (attr == R.styleable.LinearProgressDrawable_pv_progressMode)
                mProgressMode = a.getInteger(attr, 0)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_inAnimDuration)
                mInAnimationDuration = a.getInteger(attr, 0)
            else if (attr == R.styleable.LinearProgressDrawable_lpd_outAnimDuration)
                mOutAnimationDuration = a.getInteger(attr, 0)
            i++
        }

        a.recycle()

        if (strokeColors != null)
            mStrokeColors = strokeColors
        else if (strokeColorDefined)
            mStrokeColors = intArrayOf(strokeColor)

        if (mStrokeColorIndex >= mStrokeColors!!.size)
            mStrokeColorIndex = 0

        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        when (mProgressMode) {
            ProgressView.MODE_DETERMINATE -> drawDeterminate(canvas)
            ProgressView.MODE_INDETERMINATE -> drawIndeterminate(canvas)
            ProgressView.MODE_BUFFER -> drawBuffer(canvas)
            ProgressView.MODE_QUERY -> drawQuery(canvas)
        }
    }

    private fun drawLinePath(canvas: Canvas, x1: Float, y1: Float, x2: Float, y2: Float, paint: Paint) {
        mPath.reset()
        mPath.moveTo(x1, y1)
        mPath.lineTo(x2, y2)
        canvas.drawPath(mPath, paint)
    }

    private fun drawDeterminate(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width()
        var size: Float = 0f

        if (mRunState == RUN_STATE_STARTING)
            size = mStrokeSize * Math.min(mInAnimationDuration.l, SystemClock.uptimeMillis() - mLastRunStateTime) / mInAnimationDuration
        else if (mRunState == RUN_STATE_STOPPING)
            size = mStrokeSize * Math.max(0, mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime) / mOutAnimationDuration
        else if (mRunState != RUN_STATE_STOPPED)
            size = mStrokeSize

        if (size > 0) {
            var y = 0f
            val lineWidth = width * progress

            when (mVerticalAlign) {
                ALIGN_TOP -> y = size / 2
                ALIGN_CENTER -> y = bounds.height() / 2f
                ALIGN_BOTTOM -> y = bounds.height() - size / 2
            }

            mPaint.strokeWidth = size
            mPaint.style = Paint.Style.STROKE

            if (progress != 1f) {
                mPaint.color = mStrokeSecondaryColor

                if (mReverse)
                    canvas.drawLine(0.f, y, width - lineWidth, y, mPaint)
                else
                    canvas.drawLine(lineWidth, y, width.f, y, mPaint)
            }

            if (progress != 0f) {
                mPaint.color = mStrokeColors!![0]
                if (mReverse)
                    drawLinePath(canvas, width - lineWidth, y, width.toFloat(), y, mPaint)
                else
                    drawLinePath(canvas, 0f, y, lineWidth, y, mPaint)
            }
        }
    }

    private fun drawIndeterminate(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width()
        var size = 0f

        when {
            mRunState == RUN_STATE_STARTING -> size = mStrokeSize * Math.min(mInAnimationDuration.l, SystemClock.uptimeMillis() - mLastRunStateTime) / mInAnimationDuration
            mRunState == RUN_STATE_STOPPING -> size = mStrokeSize * Math.max(0, mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime) / mOutAnimationDuration
            mRunState != RUN_STATE_STOPPED -> size = mStrokeSize
        }

        if (size > 0) {
            var y = 0f

            when (mVerticalAlign) {
                ALIGN_TOP -> y = size / 2
                ALIGN_CENTER -> y = bounds.height() / 2f
                ALIGN_BOTTOM -> y = bounds.height() - size / 2
            }

            mPaint.strokeWidth = size
            mPaint.style = Paint.Style.STROKE

            val endLine = offset(mStartLine, mLineWidth, width.toFloat())

            if (mReverse) {
                if (endLine <= mStartLine) {
                    mPaint.color = mStrokeSecondaryColor
                    if (endLine > 0)
                        canvas.drawLine(0.f, y, endLine, y, mPaint)
                    if (mStartLine < width)
                        canvas.drawLine(mStartLine, y, width.f, y, mPaint)

                    mPaint.color = indeterminateStrokeColor
                    drawLinePath(canvas, endLine, y, mStartLine, y, mPaint)
                } else {
                    mPaint.color = mStrokeSecondaryColor
                    canvas.drawLine(mStartLine, y, endLine, y, mPaint)

                    mPaint.color = indeterminateStrokeColor
                    mPath.reset()

                    if (mStartLine > 0) {
                        mPath.moveTo(0.f, y)
                        mPath.lineTo(mStartLine, y)
                    }
                    if (endLine < width) {
                        mPath.moveTo(endLine, y)
                        mPath.lineTo(width.f, y)
                    }

                    canvas.drawPath(mPath, mPaint)
                }
            } else {
                if (endLine >= mStartLine) {
                    mPaint.color = mStrokeSecondaryColor
                    if (mStartLine > 0)
                        canvas.drawLine(0.f, y, mStartLine, y, mPaint)
                    if (endLine < width)
                        canvas.drawLine(endLine, y, width.f, y, mPaint)

                    mPaint.color = indeterminateStrokeColor
                    drawLinePath(canvas, mStartLine, y, endLine, y, mPaint)
                } else {
                    mPaint.color = mStrokeSecondaryColor
                    canvas.drawLine(endLine, y, mStartLine, y, mPaint)

                    mPaint.color = indeterminateStrokeColor
                    mPath.reset()

                    if (endLine > 0) {
                        mPath.moveTo(0.f, y)
                        mPath.lineTo(endLine, y)
                    }
                    if (mStartLine < width) {
                        mPath.moveTo(mStartLine, y)
                        mPath.lineTo(width.f, y)
                    }

                    canvas.drawPath(mPath, mPaint)
                }
            }
        }
    }

    private fun drawBuffer(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width()
        var size = 0f

        when {
            mRunState == RUN_STATE_STARTING -> size = mStrokeSize * Math.min(mInAnimationDuration.l, SystemClock.uptimeMillis() - mLastRunStateTime) / mInAnimationDuration
            mRunState == RUN_STATE_STOPPING -> size = mStrokeSize * Math.max(0, mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime) / mOutAnimationDuration
            mRunState != RUN_STATE_STOPPED -> size = mStrokeSize
        }

        if (size > 0) {
            var y = 0f
            val lineWidth = width * progress
            val secondaryLineWidth = width * secondaryProgress

            when (mVerticalAlign) {
                ALIGN_TOP -> y = size / 2
                ALIGN_CENTER -> y = bounds.height() / 2f
                ALIGN_BOTTOM -> y = bounds.height() - size / 2
            }

            mPaint.style = Paint.Style.STROKE

            if (progress != 1f) {
                mPaint.strokeWidth = size
                mPaint.color = mStrokeSecondaryColor
                mPaint.pathEffect = null

                if (mReverse)
                    drawLinePath(canvas, width - secondaryLineWidth, y, width - lineWidth, y, mPaint)
                else
                    drawLinePath(canvas, secondaryLineWidth, y, lineWidth, y, mPaint)

                mPaint.strokeWidth = mLineWidth
                mPaint.pathEffect = pathEffect
                val offset = mStrokeSize * 2 - mStartLine

                if (mReverse)
                    drawLinePath(canvas, -offset, y, width - secondaryLineWidth, y, mPaint)
                else
                    drawLinePath(canvas, width + offset, y, secondaryLineWidth, y, mPaint)
            }

            if (progress != 0f) {
                mPaint.strokeWidth = size
                mPaint.color = mStrokeColors!![0]
                mPaint.pathEffect = null

                if (mReverse)
                    drawLinePath(canvas, width - lineWidth, y, width.toFloat(), y, mPaint)
                else
                    drawLinePath(canvas, 0f, y, lineWidth, y, mPaint)
            }
        }
    }

    private fun drawQuery(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width()
        var size = 0f

        when {
            mRunState == RUN_STATE_STARTING -> size = mStrokeSize * Math.min(mInAnimationDuration.l, SystemClock.uptimeMillis() - mLastRunStateTime) / mInAnimationDuration
            mRunState == RUN_STATE_STOPPING -> size = mStrokeSize * Math.max(0, mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime) / mOutAnimationDuration
            mRunState != RUN_STATE_STOPPED -> size = mStrokeSize
        }

        if (size > 0) {
            var y = 0f

            when (mVerticalAlign) {
                ALIGN_TOP -> y = size / 2
                ALIGN_CENTER -> y = bounds.height() / 2f
                ALIGN_BOTTOM -> y = bounds.height() - size / 2
            }

            mPaint.strokeWidth = size
            mPaint.style = Paint.Style.STROKE

            if (progress != 1f) {
                mPaint.color = mStrokeSecondaryColor
                canvas.drawLine(0.f, y, width.f, y, mPaint)

                if (mAnimTime < 1f) {
                    val endLine = Math.max(0f, Math.min(width.f, mStartLine + mLineWidth))
                    mPaint.color = queryStrokeColor
                    drawLinePath(canvas, mStartLine, y, endLine, y, mPaint)
                }
            }

            if (progress != 0f) {
                val lineWidth = width * progress
                mPaint.color = mStrokeColors!![0]

                if (mReverse)
                    drawLinePath(canvas, width - lineWidth, y, width.toFloat(), y, mPaint)
                else
                    drawLinePath(canvas, 0f, y, lineWidth, y, mPaint)
            }

        }
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter) {
        mPaint.colorFilter = cf
    }

    //Animation: based on http://cyrilmottier.com/2012/11/27/actionbar-on-the-move/

    private fun resetAnimation() {
        mLastUpdateTime = SystemClock.uptimeMillis()
        mLastProgressStateTime = mLastUpdateTime
        when (mProgressMode) {
            ProgressView.MODE_INDETERMINATE -> {
                mStartLine = (if (mReverse) bounds.width() else 0).toFloat()
                mStrokeColorIndex = 0
                mLineWidth = (if (mReverse) -mMinLineWidth else mMinLineWidth).toFloat()
                mProgressState = PROGRESS_STATE_STRETCH
            }
            ProgressView.MODE_BUFFER -> mStartLine = 0f
            ProgressView.MODE_QUERY -> {
                mStartLine = (if (!mReverse) bounds.width() else 0).toFloat()
                mStrokeColorIndex = 0
                mLineWidth = (if (!mReverse) -mMaxLineWidth else mMaxLineWidth).toFloat()
            }
        }
    }

    override fun start() {
        start(mInAnimationDuration > 0)
    }

    override fun stop() {
        stop(mOutAnimationDuration > 0)
    }

    private fun start(withAnimation: Boolean) {
        if (isRunning)
            return

        if (withAnimation) {
            mRunState = RUN_STATE_STARTING
            mLastRunStateTime = SystemClock.uptimeMillis()
        }

        resetAnimation()

        scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION)
        invalidateSelf()
    }

    private fun stop(withAnimation: Boolean) {
        if (!isRunning)
            return

        if (withAnimation) {
            mLastRunStateTime = SystemClock.uptimeMillis()

            if (mRunState == RUN_STATE_STARTED) {
                scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION)
                invalidateSelf()
            }
            mRunState = RUN_STATE_STOPPING
        } else {
            mRunState = RUN_STATE_STOPPED
            unscheduleSelf(mUpdater)
            invalidateSelf()
        }
    }

    override fun scheduleSelf(what: Runnable, `when`: Long) {
        if (mRunState == RUN_STATE_STOPPED)
            mRunState = if (mInAnimationDuration > 0) RUN_STATE_STARTING else RUN_STATE_RUNNING
        super.scheduleSelf(what, `when`)
    }

    private fun update() {
        when (mProgressMode) {
            ProgressView.MODE_DETERMINATE -> updateDeterminate()
            ProgressView.MODE_INDETERMINATE -> updateIndeterminate()
            ProgressView.MODE_BUFFER -> updateBuffer()
            ProgressView.MODE_QUERY -> updateQuery()
        }
    }

    private fun updateDeterminate() {
        val curTime = SystemClock.uptimeMillis()

        if (mRunState == RUN_STATE_STARTING) {
            if (curTime - mLastRunStateTime > mInAnimationDuration) {
                mRunState = RUN_STATE_STARTED
                return
            }
        } else if (mRunState == RUN_STATE_STOPPING) {
            if (curTime - mLastRunStateTime > mOutAnimationDuration) {
                stop(false)
                return
            }
        }

        if (isRunning)
            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION)

        invalidateSelf()
    }

    private fun offset(pos: Float, offset: Float, max: Float): Float {
        var pos = pos
        pos += offset
        if (pos > max)
            return pos - max
        return if (pos < 0) max + pos else pos
    }

    private fun updateIndeterminate() {
        val bounds = bounds
        val width = bounds.width()

        val curTime = SystemClock.uptimeMillis()
        var travelOffset = (curTime - mLastUpdateTime).toFloat() * width / mTravelDuration
        if (mReverse)
            travelOffset = -travelOffset
        mLastUpdateTime = curTime

        when (mProgressState) {
            PROGRESS_STATE_STRETCH -> if (mTransformDuration <= 0) {
                mLineWidth = if (mMinLineWidth == 0f) width * mMinLineWidthPercent else mMinLineWidth
                if (mReverse)
                    mLineWidth = -mLineWidth
                mStartLine = offset(mStartLine, travelOffset, width.toFloat())
                mProgressState = PROGRESS_STATE_KEEP_STRETCH
                mLastProgressStateTime = curTime
            } else {
                val value = (curTime - mLastProgressStateTime) / mTransformDuration.toFloat()
                val maxWidth: Float = if (mMaxLineWidth == 0f) width * mMaxLineWidthPercent else mMaxLineWidth
                val minWidth: Float = if (mMinLineWidth == 0f) width * mMinLineWidthPercent else mMinLineWidth

                mStartLine = offset(mStartLine, travelOffset, width.toFloat())
                mLineWidth = mTransformInterpolator!!.getInterpolation(value) * (maxWidth - minWidth) + minWidth
                if (mReverse)
                    mLineWidth = -mLineWidth

                if (value > 1f) {
                    mLineWidth = if (mReverse) -maxWidth else maxWidth
                    mProgressState = PROGRESS_STATE_KEEP_STRETCH
                    mLastProgressStateTime = curTime
                }
            }
            PROGRESS_STATE_KEEP_STRETCH -> {
                mStartLine = offset(mStartLine, travelOffset, width.toFloat())

                if (curTime - mLastProgressStateTime > mKeepDuration) {
                    mProgressState = PROGRESS_STATE_SHRINK
                    mLastProgressStateTime = curTime
                }
            }
            PROGRESS_STATE_SHRINK -> if (mTransformDuration <= 0) {
                mLineWidth = if (mMinLineWidth == 0f) width * mMinLineWidthPercent else mMinLineWidth
                if (mReverse)
                    mLineWidth = -mLineWidth
                mStartLine = offset(mStartLine, travelOffset, width.toFloat())
                mProgressState = PROGRESS_STATE_KEEP_SHRINK
                mLastProgressStateTime = curTime
                mStrokeColorIndex = (mStrokeColorIndex + 1) % mStrokeColors!!.size
            } else {
                val value = (curTime - mLastProgressStateTime) / mTransformDuration.toFloat()
                val maxWidth = if (mMaxLineWidth == 0f) width * mMaxLineWidthPercent else mMaxLineWidth
                val minWidth = if (mMinLineWidth == 0f) width * mMinLineWidthPercent else mMinLineWidth

                var newLineWidth = (1f - mTransformInterpolator!!.getInterpolation(value)) * (maxWidth - minWidth) + minWidth
                if (mReverse)
                    newLineWidth = -newLineWidth

                mStartLine = offset(mStartLine, travelOffset + mLineWidth - newLineWidth, width.toFloat())
                mLineWidth = newLineWidth

                if (value > 1f) {
                    mLineWidth = if (mReverse) -minWidth else minWidth
                    mProgressState = PROGRESS_STATE_KEEP_SHRINK
                    mLastProgressStateTime = curTime
                    mStrokeColorIndex = (mStrokeColorIndex + 1) % mStrokeColors!!.size
                }
            }
            PROGRESS_STATE_KEEP_SHRINK -> {
                mStartLine = offset(mStartLine, travelOffset, width.toFloat())

                if (curTime - mLastProgressStateTime > mKeepDuration) {
                    mProgressState = PROGRESS_STATE_STRETCH
                    mLastProgressStateTime = curTime
                }
            }
        }

        if (mRunState == RUN_STATE_STARTING) {
            if (curTime - mLastRunStateTime > mInAnimationDuration)
                mRunState = RUN_STATE_RUNNING
        } else if (mRunState == RUN_STATE_STOPPING) {
            if (curTime - mLastRunStateTime > mOutAnimationDuration) {
                stop(false)
                return
            }
        }

        if (isRunning)
            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION)

        invalidateSelf()
    }

    private fun updateBuffer() {
        val curTime = SystemClock.uptimeMillis()
        val maxDistance = (mStrokeSize * 2)
        mStartLine += maxDistance * (curTime - mLastUpdateTime).toFloat() / mTravelDuration
        while (mStartLine > maxDistance)
            mStartLine -= maxDistance
        mLastUpdateTime = curTime

        when (mProgressState) {
            PROGRESS_STATE_STRETCH -> if (mTransformDuration <= 0) {
                mProgressState = PROGRESS_STATE_KEEP_STRETCH
                mLastProgressStateTime = curTime
            } else {
                val value = (curTime - mLastProgressStateTime) / mTransformDuration.toFloat()
                mLineWidth = mTransformInterpolator!!.getInterpolation(value) * mStrokeSize

                if (value > 1f) {
                    mLineWidth = mStrokeSize
                    mProgressState = PROGRESS_STATE_KEEP_STRETCH
                    mLastProgressStateTime = curTime
                }
            }
            PROGRESS_STATE_KEEP_STRETCH -> if (curTime - mLastProgressStateTime > mKeepDuration) {
                mProgressState = PROGRESS_STATE_SHRINK
                mLastProgressStateTime = curTime
            }
            PROGRESS_STATE_SHRINK -> if (mTransformDuration <= 0) {
                mProgressState = PROGRESS_STATE_KEEP_SHRINK
                mLastProgressStateTime = curTime
            } else {
                val value = (curTime - mLastProgressStateTime) / mTransformDuration.toFloat()
                mLineWidth = (1f - mTransformInterpolator!!.getInterpolation(value)) * mStrokeSize

                if (value > 1f) {
                    mLineWidth = 0f
                    mProgressState = PROGRESS_STATE_KEEP_SHRINK
                    mLastProgressStateTime = curTime
                }
            }
            PROGRESS_STATE_KEEP_SHRINK -> if (curTime - mLastProgressStateTime > mKeepDuration) {
                mProgressState = PROGRESS_STATE_STRETCH
                mLastProgressStateTime = curTime
            }
        }

        if (mRunState == RUN_STATE_STARTING) {
            if (curTime - mLastRunStateTime > mInAnimationDuration)
                mRunState = RUN_STATE_RUNNING
        } else if (mRunState == RUN_STATE_STOPPING) {
            if (curTime - mLastRunStateTime > mOutAnimationDuration) {
                stop(false)
                return
            }
        }

        if (isRunning)
            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION)

        invalidateSelf()
    }

    private fun updateQuery() {
        val curTime = SystemClock.uptimeMillis()
        mAnimTime = (curTime - mLastProgressStateTime).toFloat() / mTravelDuration
        val requestUpdate = mRunState == RUN_STATE_STOPPING || progress == 0f || mAnimTime < 1f

        if (mAnimTime > 1f) {
            mLastProgressStateTime = Math.round(curTime - (mAnimTime - 1f) * mTravelDuration).l
            mAnimTime -= 1f
        }

        if (requestUpdate && mRunState != RUN_STATE_STOPPING) {
            val bounds = bounds
            val width = bounds.width()

            val maxWidth = if (mMaxLineWidth == 0f) width * mMaxLineWidthPercent else mMaxLineWidth
            val minWidth = if (mMinLineWidth == 0f) width * mMinLineWidthPercent else mMinLineWidth
            mLineWidth = mTransformInterpolator!!.getInterpolation(mAnimTime) * (minWidth - maxWidth) + maxWidth
            if (mReverse)
                mLineWidth = -mLineWidth

            mStartLine = if (mReverse) mTransformInterpolator!!.getInterpolation(mAnimTime) * (width + minWidth)
            else (1f - mTransformInterpolator!!.getInterpolation(mAnimTime)) * (width + minWidth) - minWidth
        }

        if (mRunState == RUN_STATE_STARTING) {
            if (curTime - mLastRunStateTime > mInAnimationDuration)
                mRunState = RUN_STATE_RUNNING
        } else if (mRunState == RUN_STATE_STOPPING) {
            if (curTime - mLastRunStateTime > mOutAnimationDuration) {
                stop(false)
                return
            }
        }

        if (isRunning) {
            if (requestUpdate)
                scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION)
            else if (mRunState == RUN_STATE_RUNNING)
                mRunState = RUN_STATE_STARTED
        }

        invalidateSelf()
    }

    class Builder {
        private var mProgressPercent = 0f
        private var mSecondaryProgressPercent = 0f
        private var mMaxLineWidth: Float = 0f
        private var mMaxLineWidthPercent: Float = 0f
        private var mMinLineWidth: Float = 0f
        private var mMinLineWidthPercent: Float = 0f
        private var mStrokeSize: Float = 8f
        private var mVerticalAlign = ALIGN_BOTTOM
        private var mStrokeColors: IntArray? = null
        private var mStrokeSecondaryColor: Int = 0
        private var mReverse = false
        private var mTravelDuration = 1000
        private var mTransformDuration = 800
        private var mKeepDuration = 200
        private var mTransformInterpolator: Interpolator? = null
        private var mProgressMode = ProgressView.MODE_INDETERMINATE
        private var mInAnimationDuration = 400
        private var mOutAnimationDuration = 400

        constructor()

        constructor(context: Context, defStyleRes: Int) : this(context, null, 0, defStyleRes)

        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.LinearProgressDrawable, defStyleAttr, defStyleRes)
            var resId: Int

            progressPercent(a.getFloat(R.styleable.LinearProgressDrawable_pv_progress, 0f))
            secondaryProgressPercent(a.getFloat(R.styleable.LinearProgressDrawable_pv_secondaryProgress, 0f))

            var value = a.peekValue(R.styleable.LinearProgressDrawable_lpd_maxLineWidth)
            when {
                value == null -> maxLineWidth(0.75f)
                value.type == TypedValue.TYPE_FRACTION -> maxLineWidth(a.getFraction(R.styleable.LinearProgressDrawable_lpd_maxLineWidth, 1, 1, 0.75f))
                else -> maxLineWidth(a.getDimensionPixelSize(R.styleable.LinearProgressDrawable_lpd_maxLineWidth, 0))
            }

            value = a.peekValue(R.styleable.LinearProgressDrawable_lpd_minLineWidth)
            when {
                value == null -> minLineWidth(0.25f)
                value.type == TypedValue.TYPE_FRACTION -> minLineWidth(a.getFraction(R.styleable.LinearProgressDrawable_lpd_minLineWidth, 1, 1, 0.25f))
                else -> minLineWidth(a.getDimensionPixelSize(R.styleable.LinearProgressDrawable_lpd_minLineWidth, 0))
            }

            strokeSize(a.getDimensionPixelSize(R.styleable.LinearProgressDrawable_lpd_strokeSize, 4.dp))
            verticalAlign(a.getInteger(R.styleable.LinearProgressDrawable_lpd_verticalAlign, ALIGN_BOTTOM))
            strokeColors(a.getColor(R.styleable.LinearProgressDrawable_lpd_strokeColor, Color.WHITE))
            resId = a.getResourceId(R.styleable.LinearProgressDrawable_lpd_strokeColors, 0)
            if (resId != 0) {
                val ta = context.resources.obtainTypedArray(resId)
                val colors = IntArray(ta.length())
                for (j in 0 until ta.length())
                    colors[j] = ta.getColor(j, 0)
                ta.recycle()
                strokeColors(*colors)
            }
            strokeSecondaryColor(a.getColor(R.styleable.LinearProgressDrawable_lpd_strokeSecondaryColor, 0))
            reverse(a.getBoolean(R.styleable.LinearProgressDrawable_lpd_reverse, false))
            travelDuration(a.getInteger(R.styleable.LinearProgressDrawable_lpd_travelDuration, context.resources.getInteger(android.R.integer.config_longAnimTime)))
            transformDuration(a.getInteger(R.styleable.LinearProgressDrawable_lpd_transformDuration, context.resources.getInteger(android.R.integer.config_mediumAnimTime)))
            keepDuration(a.getInteger(R.styleable.LinearProgressDrawable_lpd_keepDuration, context.resources.getInteger(android.R.integer.config_shortAnimTime)))
            resId = a.getResourceId(R.styleable.LinearProgressDrawable_lpd_transformInterpolator, 0)
            if (resId != 0)
                transformInterpolator(AnimationUtils.loadInterpolator(context, resId))
            progressMode(a.getInteger(R.styleable.LinearProgressDrawable_pv_progressMode, ProgressView.MODE_INDETERMINATE))
            inAnimDuration(a.getInteger(R.styleable.LinearProgressDrawable_lpd_inAnimDuration, context.resources.getInteger(android.R.integer.config_mediumAnimTime)))
            outAnimDuration(a.getInteger(R.styleable.LinearProgressDrawable_lpd_outAnimDuration, context.resources.getInteger(android.R.integer.config_mediumAnimTime)))

            a.recycle()
        }

        fun build(): LinearProgressDrawable {
            if (mStrokeColors == null)
                mStrokeColors = intArrayOf(-0xff6601)

            if (mTransformInterpolator == null)
                mTransformInterpolator = DecelerateInterpolator()

            return LinearProgressDrawable(mProgressPercent,
                    mSecondaryProgressPercent,
                    mMaxLineWidth,
                    mMaxLineWidthPercent,
                    mMinLineWidth,
                    mMinLineWidthPercent,
                    mStrokeSize,
                    mVerticalAlign,
                    mStrokeColors,
                    mStrokeSecondaryColor,
                    mReverse,
                    mTravelDuration,
                    mTransformDuration,
                    mKeepDuration,
                    mTransformInterpolator,
                    mProgressMode,
                    mInAnimationDuration,
                    mOutAnimationDuration)
        }

        fun secondaryProgressPercent(percent: Float): Builder {
            mSecondaryProgressPercent = percent
            return this
        }

        fun progressPercent(percent: Float): Builder {
            mProgressPercent = percent
            return this
        }

        fun maxLineWidth(width: Int): Builder {
            mMaxLineWidth = width.f
            return this
        }

        fun maxLineWidth(percent: Float): Builder {
            mMaxLineWidthPercent = Math.max(0f, Math.min(1f, percent))
            mMaxLineWidth = 0f
            return this
        }

        fun minLineWidth(width: Int): Builder {
            mMinLineWidth = width.f
            return this
        }

        fun minLineWidth(percent: Float): Builder {
            mMinLineWidthPercent = Math.max(0f, Math.min(1f, percent))
            mMinLineWidth = 0f
            return this
        }

        fun strokeSize(strokeSize: Int): Builder {
            mStrokeSize = strokeSize.f
            return this
        }

        fun verticalAlign(align: Int): Builder {
            mVerticalAlign = align
            return this
        }

        fun strokeColors(vararg strokeColors: Int): Builder {
            mStrokeColors = strokeColors
            return this
        }

        fun strokeSecondaryColor(color: Int): Builder {
            mStrokeSecondaryColor = color
            return this
        }

        @JvmOverloads
        fun reverse(reverse: Boolean = true): Builder {
            mReverse = reverse
            return this
        }

        fun travelDuration(duration: Int): Builder {
            mTravelDuration = duration
            return this
        }

        fun transformDuration(duration: Int): Builder {
            mTransformDuration = duration
            return this
        }

        fun keepDuration(duration: Int): Builder {
            mKeepDuration = duration
            return this
        }

        fun transformInterpolator(interpolator: Interpolator): Builder {
            mTransformInterpolator = interpolator
            return this
        }

        fun progressMode(mode: Int): Builder {
            mProgressMode = mode
            return this
        }

        fun inAnimDuration(duration: Int): Builder {
            mInAnimationDuration = duration
            return this
        }

        fun outAnimDuration(duration: Int): Builder {
            mOutAnimationDuration = duration
            return this
        }

    }

    companion object {
        private const val FRAME_DURATION = (1000 / 60).toLong()

        private const val PROGRESS_STATE_STRETCH = 0
        private const val PROGRESS_STATE_KEEP_STRETCH = 1
        private const val PROGRESS_STATE_SHRINK = 2
        private const val PROGRESS_STATE_KEEP_SHRINK = 3
        private const val RUN_STATE_STOPPED = 0
        private const val RUN_STATE_STARTING = 1
        private const val RUN_STATE_STARTED = 2
        private const val RUN_STATE_RUNNING = 3
        private const val RUN_STATE_STOPPING = 4

        const val ALIGN_TOP = 0
        const val ALIGN_CENTER = 1
        const val ALIGN_BOTTOM = 2
    }
}