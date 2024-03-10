package com.casecode.testing.repository

import com.casecode.domain.model.users.Item
import com.casecode.domain.repository.AddItem
import com.casecode.domain.repository.DeleteItem
import com.casecode.domain.repository.ItemRepository
import com.casecode.domain.repository.Items
import com.casecode.domain.repository.UpdateItem
import kotlinx.coroutines.flow.Flow

class TestItemRepository : ItemRepository {

    var fakeItems = arrayListOf(
        Item("item #1", 1.0, 2.0, "1234567899090", "EA", "www.image1.png"),
        Item("item #2", 3.0, 4.0, "1555567899090", "EA", "www.image2.png"),
    )

    fun setItem(itemsArrayList: ArrayList<Item>) {
        fakeItems.addAll(itemsArrayList)
    }

    override fun getItems(): Flow<Items> {
        TODO("Not yet implemented")
    }

    override suspend fun addItem(item: Item): AddItem {
        TODO("Not yet implemented")
    }

    override suspend fun updateItem(item: Item): UpdateItem {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(item: Item): DeleteItem {
        TODO("Not yet implemented")
    }

}