package com.selincengiz.havefun.viewAssets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import androidx.core.animation.doOnEnd


class AnimationImageView : androidx.appcompat.widget.AppCompatImageView, View.OnTouchListener {
    private val defaultScale = 1.0f
    private val clickScale = 0.8f
    private var downRawX = 0f
    private var downRawY = 0f
    private var dX = 0f
    private var dY = 0f

    private var isClicked = false


    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
    }


    private fun animateButton(scale: Float) {
        val scaleX = ObjectAnimator.ofFloat(this, "scaleX", scale)
        val scaleY = ObjectAnimator.ofFloat(this, "scaleY", scale)
        val animatorSet = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 250
        }
        animatorSet.start()

    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        val action = motionEvent.action
        return if (action == MotionEvent.ACTION_DOWN) {
            animateButton(clickScale)
            downRawX = motionEvent.rawX
            downRawY = motionEvent.rawY
            dX = view.x - downRawX
            dY = view.y - downRawY
            true // Consumed

            true // Consumed
        } else if (action == MotionEvent.ACTION_UP) {
            animateButton(defaultScale) // Varsayılan boyutuna geri dön
            val upRawX = motionEvent.rawX
            val upRawY = motionEvent.rawY
            val upDX = upRawX - downRawX
            val upDY = upRawY - downRawY
            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                performClick()
            } else { // A drag
                true // Consumed
            }
        } else {
            super.onTouchEvent(motionEvent)
        }
    }


    companion object {
        private const val CLICK_DRAG_TOLERANCE =
            10f // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    }

}