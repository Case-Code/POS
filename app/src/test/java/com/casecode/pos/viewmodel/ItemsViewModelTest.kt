package com.casecode.pos.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.casecode.domain.model.users.Item
import com.casecode.domain.usecase.ImageUseCase
import com.casecode.domain.usecase.ItemUseCase
import com.casecode.domain.utils.Resource
import com.casecode.testing.util.CoroutinesTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ItemsViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    // Executes coroutines deterministically.
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    // Mock dependencies
    private val itemUseCase: ItemUseCase = mockk()
    private val imageUseCase: ImageUseCase = mockk()
    private lateinit var viewModel: ItemsViewModel

    @Before
    fun setup() {
        coEvery { itemUseCase.getItems("nRoH5pcgCsbMIHVa6tmN3wkEPFL2") } returns Resource.Empty()
        viewModel = ItemsViewModel(itemUseCase, imageUseCase)
    }

    @Test
    fun `addItem when input valid than return success`() = runTest {
        // Given
        val item = Item("item #1", 0.0, 0.0, "1234567890000", "EA", "www.image.com")
        val expectedResult =
            Resource.Success("Success message") // Replace with your actual expected result
        coEvery { itemUseCase.addItem(item) } returns expectedResult

        // When
        viewModel.addItem(item)

        // Then
        // Ensure that the expected success state is emitted
        assert(viewModel.itemActionState.value == expectedResult)
        // You may also verify any other relevant behaviors here
    }

    @Test
    fun `addItem when input valid than return error`() = runTest {
        // Given
        val item = Item("item #1", 0.0, 0.0, "1234567890000", "EA", "www.image.com")
        val errorMessage = "Error message"
        val expectedResult = Resource.Error<String>(errorMessage)
        coEvery { itemUseCase.addItem(item) } returns expectedResult

        // When
        viewModel.addItem(item)

        // Then
        // Ensure that the expected error state is emitted
        assert(viewModel.itemActionState.value == expectedResult)
        // You may also verify any other relevant behaviors here
    }

    // Add more tests for other scenarios like loading, etc.

}