package com.selincengiz.havefun.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.data.model.ApiCategory
import com.selincengiz.havefun.data.model.ApiEvents
import com.selincengiz.havefun.databinding.ItemEventBinding

class EventAdapter(
    private val itemListener: ItemEventListener
) :
    ListAdapter<ApiEvents, EventAdapter.EventViewHolder>(CategoryDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemListener
        )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) =
        holder.bind(getItem(position))


    class EventViewHolder(
        private val binding: ItemEventBinding,
        private val listener: ItemEventListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ApiEvents) = with(binding) {

            ivCategory.loadUrl(event.urlHome)

            tvTitle.text = event.title
            tvDate.text = event.date
            event.price?.let {
                tvPrice.visibility = View.VISIBLE
                tvPrice.text = it.toString() + " ₺"
            } ?: kotlin.run {
                tvPrice.visibility = View.GONE
            }

            root.setOnClickListener {
                listener.onClickedEvent(event.id)
            }
        }

    }

    class CategoryDiffCallBack() : DiffUtil.ItemCallback<ApiEvents>() {
        override fun areItemsTheSame(oldItem: ApiEvents, newItem: ApiEvents): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiEvents, newItem: ApiEvents): Boolean {
            return oldItem == newItem
        }

    }


}

interface ItemEventListener {
    fun onClickedEvent(event: Int)
}
