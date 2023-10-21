package com.selincengiz.havefun.common
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest
import android.content.Context as Context1

class ColorizeTransformation(private val color: Int) : BitmapTransformation() {


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("colorize".toByteArray())
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return bitmap
    }
}
