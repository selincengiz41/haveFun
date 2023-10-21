package com.selincengiz.havefun.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.Marker
import com.selincengiz.havefun.R
import java.util.Calendar
import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions

import com.bumptech.glide.load.resource.bitmap.TransformationUtils


object Extensions {
    fun ImageView.loadUrl(url: Uri?) {

        Glide.with(this.context).load(url).circleCrop().into(this)

    }

    fun ImageView.loadUrl(url: String?) {

        Glide.with(this.context).load(url).into(this)

    }

    fun ImageView.loadUrl(url: String?,targetColor:Int) {

        Glide.with(this.context).load(url).apply(RequestOptions.bitmapTransform(ColorizeTransformation(targetColor))).into(this)

    }

    fun Fragment.showDatePicker(
        calendar: Calendar,
        onDateSelected: (Int, Int, Int) -> Unit
    ) {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                onDateSelected(year, month, day)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun Fragment.showTimePicker(
        calendar: Calendar,
        onTimeSelected: (Int, Int) -> Unit
    ) {
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                onTimeSelected(hour, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }


}