package com.casecode.domain.usecase

import com.casecode.domain.model.users.Item
import com.casecode.domain.repository.DeleteItem
import com.casecode.domain.repository.ItemRepository
import javax.inject.Inject

/**
 * Use case class for item-related operations.
 *
 * @property itemRepository The repository responsible for handling item operations.
 * @constructor Creates an [ItemUseCase] with the provided [itemRepository].
 */
class ItemUseCase @Inject constructor(private val itemRepository: ItemRepository) {
    /**
     * Retrieves items associated with the specified user ID [uid].
     *
     * @param uid The user ID for which items are to be retrieved.
     * @return A [GetItem] resource containing the retrieved items.
     */
    suspend fun getItems() = itemRepository.getItems()

    /**
     * Adds a new item to the repository.
     *
     * @param item The item to be added.
     * @return An [AddItem] resource indicating the success or failure of the addition operation.
     */
    suspend fun addItem(item: Item) = itemRepository.addItem(item)

    /**
     * Updates an existing item in the repository.
     *
     * @param item The updated item.
     * @return An [UpdateItem] resource indicating the success or failure of the update operation.
     */
    suspend fun updateItem(item: Item) = itemRepository.updateItem(item)

    /**
     * Deletes an existing item from the repository.
     *
     * @param item The item to be deleted.
     * @return A [DeleteItem] resource indicating the success or failure of the deletion operation.
     */
    suspend fun deleteItem(item: Item) = itemRepository.deleteItem(item)
}