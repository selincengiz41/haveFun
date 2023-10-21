package com.selincengiz.havefun.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.data.model.Category
import com.selincengiz.havefun.data.model.Event
import com.selincengiz.havefun.databinding.ItemCategoryBinding
import com.selincengiz.havefun.databinding.ItemEventBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class EventAdapter (private val itemListener: ItemEventListener , private val db: FirebaseFirestore) :
    ListAdapter<Event, EventAdapter.EventViewHolder>(CategoryDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemListener,db
        )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) =
        holder.bind(getItem(position))


    class EventViewHolder(
        private val binding: ItemEventBinding,
        private val listener: ItemEventListener,
         private val db: FirebaseFirestore
    ) :
        RecyclerView.ViewHolder(binding.root) {

        var allSrc=""
        fun bind(event: Event) = with(binding) {

            categoryFirebase(event.type, success = {
                ivCategory.loadUrl(it)
            }, fail = {
                ivCategory.loadUrl(allSrc)
            })

            tvTitle.text = event.title
            tvDate.text=event.date
            event.price?.let {
                tvPrice.visibility=View.VISIBLE
                tvPrice.text=it.toString()+" ₺"
            }?: kotlin.run {
                tvPrice.visibility=View.GONE
            }

            root.setOnClickListener {
                listener.onClickedEvent(event.id!!)
            }
        }

        fun categoryFirebase(category :String?, success: (String?) -> Unit, fail: () -> Unit){

            db.collection("categories").get().addOnSuccessListener {
                    documents ->
                var isThere=false
                for (document in documents) {
                    val txt= document.get("text") as String?
                    val src= document.get("urlHome") as String?

                    if (txt.equals(category)){
                        success(src)
                        isThere=true
                    }

                    if(txt.equals("All")){
                        src?.let{
                            allSrc=it
                        }

                    }
                }

                if (!isThere){
                    fail()
                }
            }



        }

    }

    class CategoryDiffCallBack() : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

    }


}

interface ItemEventListener {
    fun onClickedEvent(event: String)
}
