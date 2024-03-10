package com.casecode.data.repository

import com.casecode.domain.utils.ITEMS_COLLECTION_PATH
import com.casecode.domain.utils.Resource
import com.casecode.domain.utils.USERS_COLLECTION_PATH
import com.casecode.testing.repository.TestItemRepository
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class ItemRepositoryImplTest {

    private val auth: FirebaseAuth = mockk()
    private val firestore: FirebaseFirestore = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private val itemRepositoryImpl = ItemRepositoryImpl(auth, firestore, testDispatcher)

    private val uid = "TcEKYcw5Mg7BqznusM1x"

    @Before
    fun setup() {
        every { auth.currentUser?.uid } returns uid
    }

    @Test
    fun getItems_whenUserIdIsInValid_returnListOfItems() = runTest {
        every {
            firestore.collection(USERS_COLLECTION_PATH).document(uid)
                .collection(ITEMS_COLLECTION_PATH).get()
                .addOnSuccessListener(capture(slot<OnSuccessListener<QuerySnapshot>>()))
                .addOnFailureListener(capture(slot<OnFailureListener>()))
        } answers {
            slot<OnSuccessListener<QuerySnapshot>>().captured.onSuccess(null)
            mockk<Task<QuerySnapshot>>()
        }

        itemRepositoryImpl.getItems()

    }


    private val testItemRepository: TestItemRepository = TestItemRepository()

    @Test
    fun getItems_whenUserIdIsValid_returnListOfItems() = runTest {

        val expectedResult = testItemRepository.getItems("00")

        assertThat(expectedResult, `is`(Resource.Success(testItemRepository.fakeItems)))

    }

    @Test
    fun getItems_whenItemListChange_returnListOfItems() = runTest {

        val expectedResult = testItemRepository.getItems("00")

        assertThat(expectedResult, `is`(Resource.Success(testItemRepository.fakeItems)))

    }
}