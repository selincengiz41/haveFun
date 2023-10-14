package com.selincengiz.havefun.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.selincengiz.havefun.R
import java.util.Calendar

object Extensions {
    fun ImageView.loadUrl(url: Uri?) {

        Glide.with(this.context).load(url).circleCrop().into(this)

    }

    fun ImageView.loadUrl(url: String?) {

        Glide.with(this.context).load(url).into(this)

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