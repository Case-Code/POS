package com.casecode.domain.usecase

import com.casecode.domain.model.users.Item
import com.casecode.domain.utils.Resource
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class AddItemUseCaseTest {

    @Test
    fun addProduct_whenProductValid_returnTrue() {
        val product =
            Item(
                name = "product 01",
                price = 10.0,
                quantity = 3.0,
                sku = "000111000",
                unitOfMeasurement = "EA",
                imageUrl = null
            )

//        val addProductUseCase = AddProductUseCase(product)
//
//        assertThat(addProductUseCase `is` (Resource.success(true)))
    }
}