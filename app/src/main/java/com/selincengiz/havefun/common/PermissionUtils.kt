package com.selincengiz.havefun.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    const val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE
    const val locationFinePermission = Manifest.permission.ACCESS_FINE_LOCATION

    fun Context.checkPermission(permission: String, onGranted: () -> Unit, onDenied: () -> Unit) {

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onGranted()
        } else {
            onDenied()
        }
    }


    fun Activity.shouldShowRationale(permission: String, onGranted: () -> Unit) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            onGranted()
        }

    }
}