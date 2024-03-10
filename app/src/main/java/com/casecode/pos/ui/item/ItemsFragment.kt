package com.casecode.pos.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.casecode.domain.model.users.Item
import com.casecode.pos.R
import com.casecode.pos.adapter.ItemInteractionAdapter
import com.casecode.pos.databinding.FragmentItemsBinding
import com.casecode.pos.utils.setupToast
import com.casecode.pos.viewmodel.ItemsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying a list of items.
 */
@AndroidEntryPoint
class ItemsFragment : Fragment() {

    private lateinit var binding: FragmentItemsBinding
    private val viewModel: ItemsViewModel by activityViewModels()
    private val itemInteractionAdapter = ItemInteractionAdapter(
        onItemClick = { item -> showItemDialog(isUpdate = true, item = item) },
        onItemLongClick = { item -> deleteItem(item = item) },
        onPrintButtonClick = { item -> showQRCodeDialog(item = item) },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        setupRecyclerView()
        binding.floatingAddItem.setOnClickListener { showItemDialog() }
        initObserve()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_items_fragment, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { itemInteractionAdapter.filterItems(it) }
                    return true
                }
            },
        )

        // Hide the action_main_profile menu item
        val profileMenuItem = menu.findItem(R.id.action_main_profile)
        profileMenuItem.isVisible = false
    }

    private fun setupRecyclerView() {
        binding.recyclerItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = itemInteractionAdapter
        }
    }

    private fun initObserve() {
        // is loading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        // show message for user
        setupToast()

        observeFetchItems()
    }

    private fun observeFetchItems() {
        // is success
        viewModel.items.observe(viewLifecycleOwner) { result ->
            itemInteractionAdapter.submitList(result.toMutableList())
        }
        // is empty
        viewModel.isEmptyFetchItems.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                showEmptyView()
                hideRecyclerView()
            } else {
                hideEmptyView()
                showRecyclerView()
            }
        }
        // is error
        viewModel.isErrorFetchItems.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                showEmptyView()
                hideRecyclerView()
            } else {
                hideEmptyView()
                showRecyclerView()
            }
        }
    }

    private fun showItemDialog(isUpdate: Boolean = false, item: Item? = null) {
        val newInstance = ItemDialogFragment.newInstance(isUpdate, item)
        newInstance.show(childFragmentManager, ItemDialogFragment.ITEM_DIALOG_FRAGMENT)
    }

    private fun deleteItem(item: Item) {
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_item_title)
            .setMessage(R.string.delete_item_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteImageAndDeleteItem(item)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun showQRCodeDialog(item: Item) {
        val action = ItemsFragmentDirections.actionItemsFragmentToQRCodeDialogFragment(item)
        findNavController().navigate(action)
    }

    private fun setupToast() {
        binding.root.setupToast(viewLifecycleOwner, viewModel.userMessage, Snackbar.LENGTH_LONG)
    }

    private fun showRecyclerView() {
        binding.recyclerItems.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding.recyclerItems.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showEmptyView() {
        binding.emptyView.root.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        binding.emptyView.root.visibility = View.GONE
    }

    companion object {
        private val TAG = ItemsFragment::class.java.simpleName
    }

}
