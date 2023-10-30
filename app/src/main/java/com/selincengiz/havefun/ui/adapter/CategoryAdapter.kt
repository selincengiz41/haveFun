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

class CategoryAdapter(private val itemListener: ItemCategoryListener,private val page:String) :
    ListAdapter<ApiCategory, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemListener,page
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(getItem(position), position)

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val listener: ItemCategoryListener,
        private val page:String
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val colors = arrayOf(
            Color.parseColor("#F0635A"),
            Color.parseColor("#F59762"),
            Color.parseColor("#29D697"),
            Color.parseColor("#46CDFB"),
            Color.parseColor("#FFCD6C"),
            Color.parseColor("#EB5757"),
            Color.parseColor("#3D56F0"),

            )

        fun bind(category: ApiCategory, position: Int) = with(binding) {

            if (category.name == "Custom") {
                tvCategory.text = "All"
            } else {
                tvCategory.text = category.name

            }

            root.setOnClickListener {
                listener.onClicked(category.categoryId!!)
            }

            when(page){
                "Map"->{
                    color=  Color.parseColor("#FFFFFFFF")
                    ivCategory.loadUrl(category.url,colors.get(position))
                    colorText=Color.parseColor("#8A8D9F")
                }
                "Home"->{
                    color=colors.get(position)
                    ivCategory.loadUrl(category.url, Color.parseColor("#FFFFFFFF"))
                    colorText=Color.parseColor("#FFFFFFFF")
                }
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
    fun onClicked(category: Int)
}
