package com.selincengiz.havefun.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.data.model.ApiCategory
import com.selincengiz.havefun.databinding.ItemCategoryBinding

class CategoryAdapter(private val itemListener: ItemCategoryListener) :
    ListAdapter<ApiCategory, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemListener
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(getItem(position), position)

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val listener: ItemCategoryListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val colors = arrayOf(
            Color.parseColor("#6672FF"),
            Color.parseColor("#D8C8C6"),
            Color.parseColor("#B19CD9"),
            Color.parseColor("#B0E0E6"),
            Color.parseColor("#D8BFD8"),
            Color.parseColor("#87CEEB"),
            Color.parseColor("#E6E6FA"),

            )

        fun bind(category: ApiCategory, position: Int) = with(binding) {

            color = colors.get(0)
            ivCategory.loadUrl(category.url, Color.parseColor("#FFFFFFFF"))
            if (category.name == "Custom") {
                tvCategory.text = "All"
            } else {
                tvCategory.text = category.name

            }

            root.setOnClickListener {
                listener.onClicked(category.name!!)
            }
        }

    }

    class CategoryDiffCallBack() : DiffUtil.ItemCallback<ApiCategory>() {
        override fun areItemsTheSame(oldItem: ApiCategory, newItem: ApiCategory): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: ApiCategory, newItem: ApiCategory): Boolean {
            return oldItem == newItem
        }

    }


}

interface ItemCategoryListener {
    fun onClicked(category: String)
}
