package com.example.movies.utils

import android.graphics.Bitmap
import android.graphics.Color

class ImageUtils {

    companion object {

        fun getDominantColor(bitmap: Bitmap?): Int {
            val newBitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
            val color = newBitmap.getPixel(0, 0)
            newBitmap.recycle()
            return color
        }

        fun getInvertedColor(color: Int): Int {
            return Color.rgb(255 - Color.red(color),
                255 - Color.green(color),
                255 - Color.blue(color))
        }

        fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
            return darkness >= 0.5
        }

        fun getBlackOrWhiteColor(color: Int): Int {
            val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
            return if (darkness < 0.5) {
                Color.BLACK
            } else {
                Color.WHITE
            }
        }

    }
}