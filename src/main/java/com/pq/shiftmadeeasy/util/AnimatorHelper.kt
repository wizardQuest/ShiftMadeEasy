package com.pq.shiftmadeeasy.util

import android.view.animation.Animation
import android.view.animation.RotateAnimation

fun getRotationAnimation(fromDegrees: Float, toDegrees: Float): RotateAnimation {
    val animator = RotateAnimation(
        fromDegrees, toDegrees,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    animator.apply {
        duration = 250
        repeatCount = 0
        fillAfter = true
    }
    return animator
}