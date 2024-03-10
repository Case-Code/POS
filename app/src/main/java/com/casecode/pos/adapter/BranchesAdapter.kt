package com.casecode.pos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.casecode.domain.model.users.Branch
import com.casecode.pos.R
import com.casecode.pos.base.BaseAdapter
import com.casecode.pos.base.BaseViewHolder
import com.casecode.pos.databinding.ItemBranchBinding
import timber.log.Timber

class BranchesAdapter(val itemClick: (Branch) -> Unit) : BaseAdapter<Branch>(DiffCallback) {

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Branch]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Branch>() {
        override fun areItemsTheSame(oldItem: Branch, newItem: Branch): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Branch, newItem: Branch): Boolean {
            return oldItem.branchName.equals(newItem.branchName) && oldItem.phoneNumber.equals(
                newItem.phoneNumber
            )
        }
    }

    inner class BranchesViewHolder(binding: ItemBranchBinding) :
        BaseViewHolder<ItemBranchBinding, Branch>(binding) {
        init {
            binding.apply {
                itemView.setOnClickListener {

                    branch?.run {
                        itemClick(this)
                    }
                }
            }
        }

        override fun bind(element: Branch) {
            super.bind(element)
            Timber.e("element = $element")
            binding.branch = element
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<out ViewDataBinding, Branch> {
        return BranchesViewHolder(
            ItemBranchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_branch
    }

    /**
     *  parent list is immutable and override here to use mutableList.
     */
    override fun submitList(list: MutableList<Branch>?) {
        super.submitList(
            list?.let { ArrayList(it) })
    }
}