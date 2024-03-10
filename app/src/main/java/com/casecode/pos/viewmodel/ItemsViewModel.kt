package com.casecode.pos.viewmodel

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.casecode.domain.model.users.Item
import com.casecode.domain.usecase.ImageUseCase
import com.casecode.domain.usecase.ItemUseCase
import com.casecode.domain.utils.Resource
import com.casecode.pos.base.BaseViewModel
import com.casecode.pos.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for managing item-related data and operations.
 *
 * @property itemUseCase The use case for item-related operations.
 * @property imageUseCase The use case for image-related operations.
 * @constructor Creates an [ItemsViewModel] with the provided [itemUseCase] and [imageUseCase].
 */
@HiltViewModel
class ItemsViewModel
@Inject
constructor(
    private val itemUseCase: ItemUseCase,
    private val imageUseCase: ImageUseCase,
) : BaseViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    private val _item = MutableLiveData<Item?>()
    val item: LiveData<Item?> = _item

    private val _bitmap = MutableLiveData<Bitmap?>()
    private val bitmap: LiveData<Bitmap?> = _bitmap

    private val _isEmptyFetchItems: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEmptyFetchItems: LiveData<Boolean> get() = _isEmptyFetchItems

    private val _isErrorFetchItems: MutableLiveData<Boolean> = MutableLiveData(false)
    val isErrorFetchItems: LiveData<Boolean> get() = _isErrorFetchItems


    private val _userMessage: MutableLiveData<Event<Int>> = MutableLiveData()
    val userMessage get() = _userMessage

    init {
        fetchItems()
    }

    fun uploadImageAndAddItem(bitmap: Bitmap, item: Item) {
        viewModelScope.launch {
            when (val uploadImage = imageUseCase.uploadImage(
                bitmap = bitmap,
                imageName =
                item.sku,
            )) {
                is Resource.Loading -> showProgress()
                is Resource.Error -> {
                    hideProgress()
                    showSnackbarMessage(uploadImage.message as Int)
                }

                is Resource.Success -> {
                    val imageUrl = uploadImage.data
                    // Now you can use the imageUrl as needed, e.g., updating the item object
                    item.imageUrl = imageUrl

                    // Now you can add the item
                    addItem(item)
                }

                else -> {}
            }
        }
    }

    fun uploadImageAndUpdateItem(bitmap: Bitmap, item: Item) {
        viewModelScope.launch {
            when (val uploadImage =
                imageUseCase.uploadImage(bitmap = bitmap, imageName = item.sku)) {
                is Resource.Loading -> showProgress()
                is Resource.Error -> {
                    hideProgress()
                    showSnackbarMessage(uploadImage.message as Int)
                }

                is Resource.Success -> {
                    val imageUrl = uploadImage.data
                    // Now you can use the imageUrl as needed, e.g., updating the item object
                    item.imageUrl = imageUrl

                    // Now you can add the item
                    updateItem(item)
                }

                else -> {}
            }
        }
    }

    fun deleteImageAndDeleteItem(item: Item) {
        viewModelScope.launch {
            imageUseCase.deleteImage(imageUrl = item.imageUrl.toString()).collect { deleteImage ->
                when (deleteImage) {
                    is Resource.Loading -> showProgress()
                    is Resource.Error -> {
                        hideProgress()
                        showSnackbarMessage(deleteImage.message as Int)
                    }

                    is Resource.Success -> {
                        // Image deleted successfully, now delete the item
                        deleteItem(item)
                    }

                    else -> {}
                }
            }
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch { handleResponse(itemUseCase.addItem(item)) }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch { handleResponse(itemUseCase.updateItem(item)) }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch { handleResponse(itemUseCase.deleteItem(item)) }
    }

    private fun handleResponse(result: Resource<Any>) {
        when (result) {
            is Resource.Loading -> showProgress()
            is Resource.Error -> {
                hideProgress()
                showSnackbarMessage(result.message as Int)
            }

            is Resource.Success -> {
                hideProgress()
                showSnackbarMessage(result.data as Int)
            }

            else -> {}
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _userMessage.value = Event(message)
    }

    private fun fetchItems() {
        viewModelScope.launch {
            itemUseCase.getItems().collect {
                when (val result = it) {
                    is Resource.Empty -> {
                        _isEmptyFetchItems.value = true
                        hideProgress()
                    }

                    is Resource.Error -> {
                        _isErrorFetchItems.value = true
                        showSnackbarMessage(result.message as Int)
                        hideProgress()
                    }

                    is Resource.Loading -> {
                        showProgress()
                    }

                    is Resource.Success -> {
                        _isEmptyFetchItems.value = false
                        _isErrorFetchItems.value = false
                        _items.value = result.data
                        hideProgress()
                    }
                }
            }
        }
    }

    fun setItem(item: Item) {
        _item.value = item
    }

    fun setBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun getItem() = item.value

    fun getBitmap() = bitmap.value

    /**
     * Define clearData method to reset ViewModel data
     */
    fun clearData() {
        _item.value = null
        _bitmap.value = null
    }

    companion object {
        private val TAG = ItemsViewModel::class.java.simpleName
    }
}