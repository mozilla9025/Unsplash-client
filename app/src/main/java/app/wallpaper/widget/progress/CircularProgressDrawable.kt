package app.wallpaper.widget.progress

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import app.wallpaper.R
import app.wallpaper.util.ColorUtils
import app.wallpaper.util.extentions.dp
import app.wallpaper.util.extentions.f

class CircularProgressDrawable private constructor(private var mPadding: Int,
                                                   private var mInitialAngle: Float,
                                                   progressPercent: Float,
                                                   secondaryProgressPercent: Float,
                                                   private var mMaxSweepAngle: Float,
                                                   private var mMinSweepAngle: Float,
                                                   private var mStrokeSize: Int,
                                                   private var mStrokeColors: IntArray?,
                                                   private var mStrokeSecondaryColor: Int,
                                                   private var mReverse: Boolean,
                                                   private var mRotateDuration: Int,
                                                   private var mTransformDuration: Int,
                                                   private var mKeepDuration: Int,
                                                   private var mTransformInterpolator: Interpolator?,
                                                   private var mProgressMode: Int,
                                                   private var mInAnimationDuration: Int,
                                                   private var mInStepPercent: Float,
                                                   private var mInColors: IntArray?,
                                                   private var mOutAnimationDuration: Int) : Drawable(), Animatable {

    private var mLastUpdateTime: Long = 0
    private var mLastProgressStateTime: Long = 0
    private var mLastRunStateTime: Long = 0

    private var mProgressState: Int = 0

    private var mRunState = RUN_STATE_STOPPED

    private val mPaint: Paint
    private val mRect: RectF
    private var mStartAngle: Float = 0.toFloat()
    private var mSweepAngle: Float = 0.toFloat()
    private var mStrokeColorIndex: Int = 0
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

            val value = Math.max(0f, Math.min(1f, (SystemClock.uptimeMillis() - mLastProgressStateTime).toFloat() / mKeepDuration))
            val prev_index = if (mStrokeColorIndex == 0) mStrokeColors!!.size - 1 else mStrokeColorIndex - 1

            return ColorUtils.getMiddleColor(mStrokeColors!![prev_index], mStrokeColors!![mStrokeColorIndex], value)
        }

    var progressMode: Int
        get() = mProgressMode
        set(mode) {
            if (mProgressMode != mode) {
                mProgressMode = mode
                invalidateSelf()
            }
        }

    private val mUpdater = Runnable { update() }

    init {
        progress = progressPercent
        secondaryProgress = secondaryProgressPercent

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND

        mRect = RectF()
    }

    fun applyStyle(context: Context, resId: Int) {
        val a = context.obtainStyledAttributes(resId, R.styleable.CircularProgressDrawable)

        var strokeColor = 0
        var strokeColorDefined = false
        var strokeColors: IntArray? = null

        var i = 0
        val count = a.indexCount
        while (i < count) {
            val attr = a.getIndex(i)

            when (attr) {
                R.styleable.CircularProgressDrawable_cpd_padding -> mPadding = a.getDimensionPixelSize(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_initialAngle -> mInitialAngle = a.getInteger(attr, 0).toFloat()
                R.styleable.CircularProgressDrawable_pv_progress -> progress = a.getFloat(attr, 0f)
                R.styleable.CircularProgressDrawable_pv_secondaryProgress -> secondaryProgress = a.getFloat(attr, 0f)
                R.styleable.CircularProgressDrawable_cpd_maxSweepAngle -> mMaxSweepAngle = a.getInteger(attr, 0).toFloat()
                R.styleable.CircularProgressDrawable_cpd_minSweepAngle -> mMinSweepAngle = a.getInteger(attr, 0).toFloat()
                R.styleable.CircularProgressDrawable_cpd_strokeSize -> mStrokeSize = a.getDimensionPixelSize(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_strokeColor -> {
                    strokeColor = a.getColor(attr, 0)
                    strokeColorDefined = true
                }
                R.styleable.CircularProgressDrawable_cpd_strokeColors -> {
                    val ta = context.resources.obtainTypedArray(a.getResourceId(attr, 0))
                    strokeColors = IntArray(ta.length())
                    for (j in 0 until ta.length())
                        strokeColors[j] = ta.getColor(j, 0)
                    ta.recycle()
                }
                R.styleable.CircularProgressDrawable_cpd_strokeSecondaryColor -> mStrokeSecondaryColor = a.getColor(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_reverse -> mReverse = a.getBoolean(attr, false)
                R.styleable.CircularProgressDrawable_cpd_rotateDuration -> mRotateDuration = a.getInteger(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_transformDuration -> mTransformDuration = a.getInteger(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_keepDuration -> mKeepDuration = a.getInteger(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_transformInterpolator -> mTransformInterpolator = AnimationUtils.loadInterpolator(context, a.getResourceId(attr, 0))
                R.styleable.CircularProgressDrawable_pv_progressMode -> mProgressMode = a.getInteger(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_inAnimDuration -> mInAnimationDuration = a.getInteger(attr, 0)
                R.styleable.CircularProgressDrawable_cpd_inStepColors -> {
                    val ta = context.resources.obtainTypedArray(a.getResourceId(attr, 0))
                    mInColors = IntArray(ta.length())
                    for (j in 0 until ta.length())
                        mInColors!![j] = ta.getColor(j, 0)
                    ta.recycle()
                }
                R.styleable.CircularProgressDrawable_cpd_inStepPercent -> mInStepPercent = a.getFloat(attr, 0f)
                R.styleable.CircularProgressDrawable_cpd_outAnimDuration -> mOutAnimationDuration = a.getInteger(attr, 0)
            }
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
        }
    }

    private fun drawDeterminate(canvas: Canvas) {
        val bounds = bounds
        var radius = 0f
        var size = 0f

        if (mRunState == RUN_STATE_STARTING) {
            size = mStrokeSize.toFloat() * Math.min(mInAnimationDuration.toLong(), SystemClock.uptimeMillis() - mLastRunStateTime) / mInAnimationDuration
            if (size > 0)
                radius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2 - mStrokeSize * 2 + size) / 2f
        } else if (mRunState == RUN_STATE_STOPPING) {
            size = mStrokeSize.toFloat() * Math.max(0, mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime) / mOutAnimationDuration
            if (size > 0)
                radius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2 - mStrokeSize * 2 + size) / 2f
        } else if (mRunState != RUN_STATE_STOPPED) {
            size = mStrokeSize.toFloat()
            radius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2 - mStrokeSize) / 2f
        }

        if (radius > 0) {
            val x = (bounds.left + bounds.right) / 2f
            val y = (bounds.top + bounds.bottom) / 2f

            mPaint.strokeWidth = size
            mPaint.style = Paint.Style.STROKE

            when (progress) {
                1f -> {
                    mPaint.color = mStrokeColors!![0]
                    canvas.drawCircle(x, y, radius, mPaint)
                }
                0f -> {
                    mPaint.color = mStrokeSecondaryColor
                    canvas.drawCircle(x, y, radius, mPaint)
                }
                else -> {
                    val sweepAngle = (if (mReverse) -360 else 360) * progress

                    mRect.set(x - radius, y - radius, x + radius, y + radius)
                    mPaint.color = mStrokeSecondaryColor
                    canvas.drawArc(mRect, mStartAngle + sweepAngle, (if (mReverse) -360 else 360) - sweepAngle, false, mPaint)

                    mPaint.color = mStrokeColors!![0]
                    canvas.drawArc(mRect, mStartAngle, sweepAngle, false, mPaint)
                }
            }
        }
    }

    private fun drawIndeterminate(canvas: Canvas) {
        if (mRunState == RUN_STATE_STARTING) {
            val bounds = bounds
            val x = (bounds.left + bounds.right) / 2f
            val y = (bounds.top + bounds.bottom) / 2f
            val maxRadius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2) / 2f

            val stepTime = 1f / (mInStepPercent * (mInColors!!.size + 2) + 1)
            val time = (SystemClock.uptimeMillis() - mLastRunStateTime).toFloat() / mInAnimationDuration
            val steps = time / stepTime

            var outerRadius = 0f
            var innerRadius = 0f

            for (i in Math.floor(steps.toDouble()).toInt() downTo 0) {
                innerRadius = outerRadius
                outerRadius = Math.min(1f, (steps - i) * mInStepPercent) * maxRadius

                if (i >= mInColors!!.size)
                    continue

                if (innerRadius == 0f) {
                    mPaint.color = mInColors!![i]
                    mPaint.style = Paint.Style.FILL
                    canvas.drawCircle(x, y, outerRadius, mPaint)
                } else if (outerRadius > innerRadius) {
                    val radius = (innerRadius + outerRadius) / 2
                    mRect.set(x - radius, y - radius, x + radius, y + radius)

                    mPaint.strokeWidth = outerRadius - innerRadius
                    mPaint.style = Paint.Style.STROKE
                    mPaint.color = mInColors!![i]

                    canvas.drawCircle(x, y, radius, mPaint)
                } else
                    break
            }

            if (mProgressState == PROGRESS_STATE_HIDE) {
                if (steps >= 1 / mInStepPercent || time >= 1) {
                    resetAnimation()
                    mProgressState = PROGRESS_STATE_STRETCH
                }
            } else {
                val radius = maxRadius - mStrokeSize / 2f

                mRect.set(x - radius, y - radius, x + radius, y + radius)
                mPaint.strokeWidth = mStrokeSize.toFloat()
                mPaint.style = Paint.Style.STROKE
                mPaint.color = indeterminateStrokeColor

                canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
            }
        } else if (mRunState == RUN_STATE_STOPPING) {
            val size = mStrokeSize.toFloat() * Math.max(0, mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime) / mOutAnimationDuration

            if (size > 0) {
                val bounds = bounds
                val radius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2 - mStrokeSize * 2 + size) / 2f
                val x = (bounds.left + bounds.right) / 2f
                val y = (bounds.top + bounds.bottom) / 2f

                mRect.set(x - radius, y - radius, x + radius, y + radius)
                mPaint.strokeWidth = size
                mPaint.style = Paint.Style.STROKE
                mPaint.color = indeterminateStrokeColor

                canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
            }
        } else if (mRunState != RUN_STATE_STOPPED) {
            val bounds = bounds
            val radius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2 - mStrokeSize) / 2f
            val x = (bounds.left + bounds.right) / 2f
            val y = (bounds.top + bounds.bottom) / 2f

            mRect.set(x - radius, y - radius, x + radius, y + radius)
            mPaint.strokeWidth = mStrokeSize.f
            mPaint.style = Paint.Style.STROKE
            mPaint.color = indeterminateStrokeColor

            canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    //Animation: based on http://cyrilmottier.com/2012/11/27/actionbar-on-the-move/

    private fun resetAnimation() {
        mLastUpdateTime = SystemClock.uptimeMillis()
        mLastProgressStateTime = mLastUpdateTime
        mStartAngle = mInitialAngle
        mStrokeColorIndex = 0
        mSweepAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle
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

        resetAnimation()

        if (withAnimation) {
            mRunState = RUN_STATE_STARTING
            mLastRunStateTime = SystemClock.uptimeMillis()
            mProgressState = PROGRESS_STATE_HIDE
        }

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

    override fun isRunning(): Boolean {
        return mRunState != RUN_STATE_STOPPED
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
        }
    }

    private fun updateDeterminate() {
        val curTime = SystemClock.uptimeMillis()
        var rotateOffset = (curTime - mLastUpdateTime) * 360f / mRotateDuration
        if (mReverse)
            rotateOffset = -rotateOffset
        mLastUpdateTime = curTime

        mStartAngle += rotateOffset

        if (mRunState == RUN_STATE_STARTING) {
            if (curTime - mLastRunStateTime > mInAnimationDuration) {
                mRunState = RUN_STATE_RUNNING
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

    private fun updateIndeterminate() {
        //update animation
        val curTime = SystemClock.uptimeMillis()
        var rotateOffset = (curTime - mLastUpdateTime) * 360f / mRotateDuration
        if (mReverse)
            rotateOffset = -rotateOffset
        mLastUpdateTime = curTime

        when (mProgressState) {
            PROGRESS_STATE_STRETCH -> if (mTransformDuration <= 0) {
                mSweepAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle
                mProgressState = PROGRESS_STATE_KEEP_STRETCH
                mStartAngle += rotateOffset
                mLastProgressStateTime = curTime
            } else {
                val value = (curTime - mLastProgressStateTime) / mTransformDuration.toFloat()
                val maxAngle = if (mReverse) -mMaxSweepAngle else mMaxSweepAngle
                val minAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle

                mStartAngle += rotateOffset
                mSweepAngle = mTransformInterpolator!!.getInterpolation(value) * (maxAngle - minAngle) + minAngle

                if (value > 1f) {
                    mSweepAngle = maxAngle
                    mProgressState = PROGRESS_STATE_KEEP_STRETCH
                    mLastProgressStateTime = curTime
                }
            }
            PROGRESS_STATE_KEEP_STRETCH -> {
                mStartAngle += rotateOffset

                if (curTime - mLastProgressStateTime > mKeepDuration) {
                    mProgressState = PROGRESS_STATE_SHRINK
                    mLastProgressStateTime = curTime
                }
            }
            PROGRESS_STATE_SHRINK -> if (mTransformDuration <= 0) {
                mSweepAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle
                mProgressState = PROGRESS_STATE_KEEP_SHRINK
                mStartAngle += rotateOffset
                mLastProgressStateTime = curTime
                mStrokeColorIndex = (mStrokeColorIndex + 1) % mStrokeColors!!.size
            } else {
                val value = (curTime - mLastProgressStateTime) / mTransformDuration.toFloat()
                val maxAngle = if (mReverse) -mMaxSweepAngle else mMaxSweepAngle
                val minAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle

                val newSweepAngle = (1f - mTransformInterpolator!!.getInterpolation(value)) * (maxAngle - minAngle) + minAngle
                mStartAngle += rotateOffset + mSweepAngle - newSweepAngle
                mSweepAngle = newSweepAngle

                if (value > 1f) {
                    mSweepAngle = minAngle
                    mProgressState = PROGRESS_STATE_KEEP_SHRINK
                    mLastProgressStateTime = curTime
                    mStrokeColorIndex = (mStrokeColorIndex + 1) % mStrokeColors!!.size
                }
            }
            PROGRESS_STATE_KEEP_SHRINK -> {
                mStartAngle += rotateOffset

                if (curTime - mLastProgressStateTime > mKeepDuration) {
                    mProgressState = PROGRESS_STATE_STRETCH
                    mLastProgressStateTime = curTime
                }
            }
        }

        if (mRunState == RUN_STATE_STARTING) {
            if (curTime - mLastRunStateTime > mInAnimationDuration) {
                mRunState = RUN_STATE_RUNNING
                if (mProgressState == PROGRESS_STATE_HIDE) {
                    resetAnimation()
                    mProgressState = PROGRESS_STATE_STRETCH
                }
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

    class Builder {
        private var mPadding: Int = 0
        private var mInitialAngle: Float = 0.toFloat()
        private var mProgressPercent: Float = 0.toFloat()
        private var mSecondaryProgressPercent: Float = 0.toFloat()
        private var mMaxSweepAngle: Float = 0.toFloat()
        private var mMinSweepAngle: Float = 0.toFloat()
        private var mStrokeSize: Int = 0
        private var mStrokeColors: IntArray? = null
        private var mStrokeSecondaryColor: Int = 0
        private var mReverse: Boolean = false
        private var mRotateDuration: Int = 0
        private var mTransformDuration: Int = 0
        private var mKeepDuration: Int = 0
        private var mTransformInterpolator: Interpolator? = null
        private var mProgressMode: Int = 0
        private var mInStepPercent: Float = 0.toFloat()
        private var mInColors: IntArray? = null
        private var mInAnimationDuration: Int = 0
        private var mOutAnimationDuration: Int = 0

        constructor()

        constructor(context: Context, defStyleRes: Int) : this(context, null, 0, defStyleRes)

        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressDrawable, defStyleAttr, defStyleRes)
            var resId: Int

            padding(a.getDimensionPixelSize(R.styleable.CircularProgressDrawable_cpd_padding, 0))
            initialAngle(a.getInteger(R.styleable.CircularProgressDrawable_cpd_initialAngle, 0).toFloat())
            progressPercent(a.getFloat(R.styleable.CircularProgressDrawable_pv_progress, 0f))
            secondaryProgressPercent(a.getFloat(R.styleable.CircularProgressDrawable_pv_secondaryProgress, 0f))
            maxSweepAngle(a.getInteger(R.styleable.CircularProgressDrawable_cpd_maxSweepAngle, 270).toFloat())
            minSweepAngle(a.getInteger(R.styleable.CircularProgressDrawable_cpd_minSweepAngle, 1).toFloat())
            strokeSize(a.getDimensionPixelSize(R.styleable.CircularProgressDrawable_cpd_strokeSize, 4.dp))
            strokeColors(a.getColor(R.styleable.CircularProgressDrawable_cpd_strokeColor, Color.WHITE))
            resId = a.getResourceId(R.styleable.CircularProgressDrawable_cpd_strokeColors, 0)
            if (resId != 0) {
                val ta = context.resources.obtainTypedArray(resId)
                val colors = IntArray(ta.length())
                for (j in 0 until ta.length())
                    colors[j] = ta.getColor(j, 0)
                ta.recycle()
                strokeColors(*colors)
            }
            strokeSecondaryColor(a.getColor(R.styleable.CircularProgressDrawable_cpd_strokeSecondaryColor, 0))
            reverse(a.getBoolean(R.styleable.CircularProgressDrawable_cpd_reverse, false))
            rotateDuration(a.getInteger(R.styleable.CircularProgressDrawable_cpd_rotateDuration, context.resources.getInteger(android.R.integer.config_longAnimTime)))
            transformDuration(a.getInteger(R.styleable.CircularProgressDrawable_cpd_transformDuration, context.resources.getInteger(android.R.integer.config_mediumAnimTime)))
            keepDuration(a.getInteger(R.styleable.CircularProgressDrawable_cpd_keepDuration, context.resources.getInteger(android.R.integer.config_shortAnimTime)))
            resId = a.getResourceId(R.styleable.CircularProgressDrawable_cpd_transformInterpolator, 0)
            if (resId != 0)
                transformInterpolator(AnimationUtils.loadInterpolator(context, resId))
            progressMode(a.getInteger(R.styleable.CircularProgressDrawable_pv_progressMode, ProgressView.MODE_INDETERMINATE))
            inAnimDuration(a.getInteger(R.styleable.CircularProgressDrawable_cpd_inAnimDuration, context.resources.getInteger(android.R.integer.config_mediumAnimTime)))
            resId = a.getResourceId(R.styleable.CircularProgressDrawable_cpd_inStepColors, 0)
            if (resId != 0) {
                val ta = context.resources.obtainTypedArray(resId)
                val colors = IntArray(ta.length())
                for (j in 0 until ta.length())
                    colors[j] = ta.getColor(j, 0)
                ta.recycle()
                inStepColors(*colors)
            }
            inStepPercent(a.getFloat(R.styleable.CircularProgressDrawable_cpd_inStepPercent, 0.5f))
            outAnimDuration(a.getInteger(R.styleable.CircularProgressDrawable_cpd_outAnimDuration, context.resources.getInteger(android.R.integer.config_mediumAnimTime)))
            a.recycle()
        }

        fun build(): CircularProgressDrawable {
            if (mStrokeColors == null)
                mStrokeColors = intArrayOf(-0xff6601)

            if (mInColors == null && mInAnimationDuration > 0)
                mInColors = intArrayOf(-0x4a2b01, -0x211504, -0x50002)

            if (mTransformInterpolator == null)
                mTransformInterpolator = DecelerateInterpolator()

            return CircularProgressDrawable(mPadding, mInitialAngle, mProgressPercent, mSecondaryProgressPercent, mMaxSweepAngle, mMinSweepAngle, mStrokeSize, mStrokeColors, mStrokeSecondaryColor, mReverse, mRotateDuration, mTransformDuration, mKeepDuration, mTransformInterpolator, mProgressMode, mInAnimationDuration, mInStepPercent, mInColors, mOutAnimationDuration)
        }

        fun padding(padding: Int): Builder {
            mPadding = padding
            return this
        }

        fun initialAngle(angle: Float): Builder {
            mInitialAngle = angle
            return this
        }

        fun progressPercent(percent: Float): Builder {
            mProgressPercent = percent
            return this
        }

        fun secondaryProgressPercent(percent: Float): Builder {
            mSecondaryProgressPercent = percent
            return this
        }

        fun maxSweepAngle(angle: Float): Builder {
            mMaxSweepAngle = angle
            return this
        }

        fun minSweepAngle(angle: Float): Builder {
            mMinSweepAngle = angle
            return this
        }

        fun strokeSize(strokeSize: Int): Builder {
            mStrokeSize = strokeSize
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

        fun rotateDuration(duration: Int): Builder {
            mRotateDuration = duration
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

        fun inStepPercent(percent: Float): Builder {
            mInStepPercent = percent
            return this
        }

        fun inStepColors(vararg colors: Int): Builder {
            mInColors = colors
            return this
        }

        fun outAnimDuration(duration: Int): Builder {
            mOutAnimationDuration = duration
            return this
        }

    }

    companion object {
        private const val FRAME_DURATION = (1000 / 60).toLong()

        private const val PROGRESS_STATE_HIDE = -1
        private const val PROGRESS_STATE_STRETCH = 0
        private const val PROGRESS_STATE_KEEP_STRETCH = 1
        private const val PROGRESS_STATE_SHRINK = 2
        private const val PROGRESS_STATE_KEEP_SHRINK = 3

        private const val RUN_STATE_STOPPED = 0
        private const val RUN_STATE_STARTING = 1
        private const val RUN_STATE_STARTED = 2
        private const val RUN_STATE_RUNNING = 3
        private const val RUN_STATE_STOPPING = 4
    }
}