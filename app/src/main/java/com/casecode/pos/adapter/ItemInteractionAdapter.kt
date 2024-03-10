package com.casecode.pos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.casecode.domain.model.users.Item
import com.casecode.pos.R
import com.casecode.pos.base.BaseAdapter
import com.casecode.pos.base.BaseViewHolder
import com.casecode.pos.databinding.ItemItemBinding
import java.util.Locale

class ItemInteractionAdapter(
    val onItemClick: (Item) -> Unit,
    val onItemLongClick: (Item) -> Unit,
    val onPrintButtonClick: (Item) -> Unit
) : BaseAdapter<Item>(DiffCallback), Filterable {

    private var originalItems: List<Item> = emptyList()
    private var filteredItems: List<Item> = emptyList()

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Item]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    inner class ItemInteractionViewHolder(binding: ItemItemBinding) :
        BaseViewHolder<ItemItemBinding, Item>(binding) {

        init {
            binding.root.setOnClickListener { binding.item?.let { onItemClick(it) } }

            // Handle long-click events
            binding.root.setOnLongClickListener {
                binding.item?.let { item -> handleItemLongClick(item) }
                true // Return true to consume the long-click event
            }

            binding.imageButtonPrintQrCode.setOnClickListener {
                binding.item?.let { item -> onPrintButtonClick(item) }
            }
        }

        override fun bind(element: Item) {
            super.bind(element)

            binding.item = element

            binding.imageButtonPrintQrCode.setOnClickListener { onPrintButtonClick(element) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<out ViewDataBinding, Item> {
        return ItemInteractionViewHolder(
            ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_item
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<out ViewDataBinding, Item>,
        position: Int
    ) {
        if (holder is ItemInteractionViewHolder) {
            val item = filteredItems[position]
            holder.bind(item)
        }
    }

    // Define a function to handle long-click events
    private fun handleItemLongClick(item: Item): Boolean {
        // Invoke the long-click listener with the clicked item
        onItemLongClick.invoke(item)
        return true // Return true to consume the long-click event
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.lowercase(Locale.getDefault())
                val filterResults = FilterResults()
                if (queryString.isNullOrBlank()) {
                    filterResults.values = originalItems
                } else {
                    val filteredList = originalItems.filter { item ->
                        item.name.lowercase(Locale.getDefault()).contains(queryString) ||
                                item.sku.lowercase(Locale.getDefault()).contains(queryString)
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as? List<Item> ?: emptyList()
                notifyDataSetChanged() // Notify data set changed
            }
        }
    }

    fun filterItems(query: String) {
        filter.filter(query)
    }

    override fun submitList(list: MutableList<Item>?) {
        originalItems = list ?: emptyList()
        filterItems("") // Reset filter initially
    }
}