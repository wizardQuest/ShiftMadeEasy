package com.pq.shiftmadeeasy.color

import androidx.annotation.ColorInt

class Colors {
    companion object {

        //TransparentColor
        @ColorInt
        const val TRANSPARENT = 0

        @ColorInt
        const val BLACK = 0xFF000000.toInt()

        @ColorInt
        const val RED = 0xFFFF0000.toInt()

        @ColorInt
        const val GREEN = 0xFF00FF00.toInt()

        @ColorInt
        const val BLUE = 0xFF0000FF.toInt()

        //With Paid Version => 25 colors - 3(above 3) = 22 colors
        @ColorInt
        const val RED_LIGHT = 0xFFFF00FF.toInt()

        @ColorInt
        const val GREEN_LIME = 0xFF32CD32.toInt()

        @ColorInt
        const val GREEN_LIGHT = 0xFF90EE90.toInt()

        @ColorInt
        const val GLOW_YELLOW = 0xFFF5D300.toInt()

        @ColorInt
        const val GLOW_BLUE = 0xFF08F7FE.toInt()

        @ColorInt
        const val GLOW_PINK = 0xFFFE53BB.toInt()

        @ColorInt
        const val NEON_LIGHT_PINK = 0xFFFFACFC.toInt()

        @ColorInt
        const val NEON_PINK = 0xFF0F148FB.toInt()

        @ColorInt
        const val NEON_BLUE = 0xFF7122FA.toInt()

        @ColorInt
        const val NEON_VIOLET = 0xFF560A86.toInt()

        @ColorInt
        const val AMAZING_BLUE = 0xFF011FFD.toInt()

        @ColorInt
        const val LIGHT_BLUE = 0xFFA5D8F3.toInt()

        @ColorInt
        const val ABSTRACT_BLUE = 0xFF00FECA.toInt()

        @ColorInt
        const val ABSTRACT_YELLOW = 0xFFFDF200.toInt()

        @ColorInt
        const val SMOOTH_ORANGE = 0xFFFF8B8B.toInt()

        @ColorInt
        const val LUXURIOUS_BLUE = 0xFF037A90.toInt()

        @ColorInt
        const val SHARP_GREEN = 0xFF28CF75.toInt()

        @ColorInt
        const val SHARP_ORANGE = 0xFFFE6B35.toInt()

        @ColorInt
        const val RETRO_RED = 0xFFCE0000.toInt()

        @ColorInt
        const val ACRYLIC_RED = 0xFFF21A1D.toInt()

        @ColorInt
        const val BRIGHT_GREEN = 0xFF7FFF00.toInt()

        @ColorInt
        const val RADIANT_BLUE = 0xFF001437.toInt()

        fun getColors(): ArrayList<Int> {
            //if (if premium user)

            //else (if not premium user or ad free user)
            return arrayListOf(
                //Each row 5
                RED, GREEN, BLUE, RED_LIGHT, GREEN_LIME,
                GREEN_LIGHT, GLOW_YELLOW, GLOW_BLUE, GLOW_PINK, NEON_LIGHT_PINK,
                NEON_PINK, NEON_BLUE, NEON_VIOLET, AMAZING_BLUE, LIGHT_BLUE,
                ABSTRACT_BLUE, ABSTRACT_YELLOW, SMOOTH_ORANGE, LUXURIOUS_BLUE, SHARP_GREEN
                //, SHARP_ORANGE, RETRO_RED, ACRYLIC_RED, BRIGHT_GREEN, RADIANT_BLUE
            )
        }
    }
}