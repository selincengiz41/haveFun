package com.selincengiz.havefun.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.selincengiz.havefun.R
import com.selincengiz.havefun.common.Extensions.loadUrl
import com.selincengiz.havefun.data.model.Category
import com.selincengiz.havefun.databinding.ItemCategoryBinding

class CategoryAdapter(private val itemListener: ItemCategoryListener) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemListener
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(getItem(position))

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val listener: ItemCategoryListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) = with(binding) {

            ivCategory.loadUrl(category.url)
            tvCategory.text = category.text

            root.setOnClickListener {
                listener.onClicked(category.text!!)
            }
        }

    }

    class CategoryDiffCallBack() : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }


}

interface ItemCategoryListener {
    fun onClicked(category: String)
}
