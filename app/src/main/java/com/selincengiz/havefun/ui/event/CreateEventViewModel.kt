package com.selincengiz.havefun.ui.event

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.selincengiz.havefun.data.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {

    fun firebaseSave(event: Event, success: () -> Unit, fail: (Exception) -> Unit) {
        db.collection("events").document(event.title?:"")
            .set(event)
            .addOnSuccessListener {
                success()

            }
            .addOnFailureListener {
                fail(it)

            }
    }
}