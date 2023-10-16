package com.selincengiz.havefun.data.model

import android.location.Address
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Event
 (val id :String?,
  val title:String?,
  val date:String?,
  val type:String?,
  val personLimit:Long?,
  val locationTitle:String?,
  val adress: com.selincengiz.havefun.data.model.Address?,
  val info: CommunicationInfo?,
  val price:Double?


     )

