package com.casecode.data.repository

import com.casecode.data.utils.AppDispatchers
import com.casecode.data.utils.Dispatcher
import com.casecode.domain.model.users.Item
import com.casecode.domain.repository.AddItem
import com.casecode.domain.repository.AuthService
import com.casecode.domain.repository.DeleteItem
import com.casecode.domain.repository.ItemRepository
import com.casecode.domain.repository.Items
import com.casecode.domain.repository.UpdateItem
import com.casecode.domain.utils.ITEMS_COLLECTION_PATH
import com.casecode.domain.utils.Resource
import com.casecode.domain.utils.USERS_COLLECTION_PATH
import com.casecode.pos.data.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Implementation of the [ItemRepository] interface for handling item-related operations using Firestore.
 *
 * @property firestore Reference to Firestore for storing and retrieving items.
 * @property ioDispatcher Coroutine dispatcher for performing operations asynchronously on IO-bound threads.
 * @constructor Creates an [ItemRepositoryImpl] with the provided [firestore] and [ioDispatcher].
 */
class ItemRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @Dispatcher(AppDispatchers.IO) val ioDispatcher: CoroutineDispatcher,
) : ItemRepository, AuthService {
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val currentUser: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth -> this.trySend(auth.currentUser) }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }.flowOn(ioDispatcher)

    override fun getItems(): Flow<Items> =
        callbackFlow<Resource<List<Item>>> {
            trySend(Resource.Loading)

            val itemMutableList = mutableListOf<Item>()

            // Create a Firestore query to retrieve all items for the given UID
            val query = getItemCollectionRef(currentUserId)


            // Query Firestore to retrieve all items for the given UID
            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle the error
                    Timber.tag(TAG).e(error)
                    trySend(Resource.error(R.string.error_fetching_items))
                    return@addSnapshotListener
                }

                // Clear the list before updating it with new data
                itemMutableList.clear()

                // Loop through the query results and convert each document to an Item object
                for (document in snapshot?.documents ?: emptyList()) {
                    val item = document.toObject(Item::class.java)
                    item?.let {
                        // Retrieve the imageUrl from the document data
                        val imageUrl = document.getString("image_url")
                        val unitOfMeasurement = document.getString("unit_of_measurement")
                        // Set the retrieved (imageUrl, unitOfMeasurement) to the Item object
                        it.imageUrl = imageUrl
                        it.unitOfMeasurement = unitOfMeasurement
                        itemMutableList.add(it)
                    }
                }

                if (itemMutableList.isEmpty()) {
                    trySend(Resource.empty())
                } else {
                    trySend(Resource.success(itemMutableList))
                }
            }

            // Close the listener when the flow is cancelled
            awaitClose { listenerRegistration.remove() }
        }.flowOn(ioDispatcher)

    override suspend fun addItem(item: Item): AddItem {
        return withContext(ioDispatcher) {
            try {
                // Loading state before starting the deletion operation
                Resource.Loading

                suspendCoroutine { continuation ->
                    val itemMap =
                        mapOf(
                            "name" to item.name,
                            "price" to item.price,
                            "quantity" to item.quantity,
                            "sku" to item.sku,
                            "unit_of_measurement" to item.unitOfMeasurement,
                            "image_url" to item.imageUrl,
                        )

                    val sku = item.sku
                    getItemDocumentRef(uid = currentUserId, sku = sku).set(itemMap as Map<*, *>)
                        .addOnSuccessListener {
                            continuation.resume(Resource.Success(data = R.string.item_added_successfully))
                        }.addOnFailureListener { failure ->
                            Timber.tag(TAG).e(failure)

                            val errorMessage = when (failure) {
                                is UnknownHostException -> R.string.add_item_failure_network
                                else -> R.string.add_item_failure_generic
                            }
                            continuation.resume(Resource.error(errorMessage))
                        }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                Resource.error(R.string.add_item_failure_generic)
            }
        }
    }

    override suspend fun updateItem(item: Item): UpdateItem {
        return withContext(ioDispatcher) {
            try {
                // Loading state before starting the update operation
                Resource.Loading

                suspendCoroutine { continuation ->
                    val itemMap =
                        mapOf(
                            "name" to item.name,
                            "price" to item.price,
                            "quantity" to item.quantity,
                            "sku" to item.sku,
                            "unit_of_measurement" to item.unitOfMeasurement,
                            "image_url" to item.imageUrl,
                        )

                    val sku = item.sku
                    getItemDocumentRef(uid = currentUserId, sku = sku).set(itemMap as Map<*, *>)
                        .addOnSuccessListener {
                            continuation.resume(Resource.Success(data = R.string.item_updated_successfully))
                        }.addOnFailureListener { failure ->
                            Timber.tag(TAG).e(failure)

                            val errorMessage = when (failure) {
                                is UnknownHostException -> R.string.update_item_failure_network
                                else -> R.string.update_item_failure_generic
                            }
                            continuation.resume(Resource.error(errorMessage))
                        }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                Resource.error(R.string.update_item_failure_generic)
            }
        }
    }

    override suspend fun deleteItem(item: Item): DeleteItem {
        return withContext(ioDispatcher) {
            try {
                // Loading state before starting the deletion operation
                Resource.Loading

                suspendCoroutine { continuation ->
                    getItemDocumentRef(uid = currentUserId, sku = item.sku).delete()
                        .addOnSuccessListener {
                            continuation.resume(Resource.success(R.string.item_deleted_successfully))
                        }.addOnFailureListener { failure ->
                            Timber.tag(TAG).e(failure)

                            val errorMessage = when (failure) {
                                is UnknownHostException -> R.string.delete_item_failure_network
                                else -> R.string.delete_item_failure_generic
                            }
                            continuation.resume(Resource.error(errorMessage))
                        }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                Resource.error(R.string.delete_item_failure_generic)
            }
        }
    }

    /**
     * Retrieves a reference to the Firestore document corresponding to the given user ID and SKU.
     *
     * @param uid The user ID.
     * @param sku The SKU (stock keeping unit) of the item.
     * @return The Firestore document reference.
     */
    private fun getItemDocumentRef(
        uid: String,
        sku: String,
    ): DocumentReference {
        val itemsCollection =
            firestore.collection(USERS_COLLECTION_PATH)
                .document(uid)
                .collection(ITEMS_COLLECTION_PATH)
        return itemsCollection.document(sku)
    }

    /**
     * Retrieves a reference to the Firestore collection of items for the given user ID.
     *
     * @param uid The user ID.
     * @return The Firestore collection reference.
     */
    private fun getItemCollectionRef(uid: String): CollectionReference =
        firestore.collection(USERS_COLLECTION_PATH).document(uid)
            .collection(ITEMS_COLLECTION_PATH)

    companion object {
        private val TAG = ItemRepositoryImpl::class.java.simpleName
    }
}