package com.casecode.data.repository

import android.graphics.Bitmap
import com.casecode.data.utils.AppDispatchers
import com.casecode.data.utils.Dispatcher
import com.casecode.domain.repository.DeleteImage
import com.casecode.domain.repository.ImageRepository
import com.casecode.domain.repository.ReplaceImage
import com.casecode.domain.repository.UploadImage
import com.casecode.domain.utils.Resource
import com.casecode.pos.data.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Implementation of the [ImageRepository] interface that handles image-related operations using Firebase Storage.
 *
 * @property firebaseStorage FirebaseStorage instance for accessing Firebase Storage.
 * @property ioDispatcher CoroutineDispatcher for performing operations in the background.
 */
class ImageRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    @Dispatcher(AppDispatchers.IO) val ioDispatcher: CoroutineDispatcher,
) : ImageRepository {

    private val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    /**
     * Uploads an image to Firebase Storage.
     *
     * @param bitmap Bitmap image to upload.
     * @param imageName Name of the image.
     * @return [UploadImage] representing the result of the upload operation.
     */
    override suspend fun uploadImage(bitmap: Bitmap, imageName: String): UploadImage {
        return withContext(ioDispatcher) {
            try {
                // Loading state before starting the deletion operation
                Resource.Loading

                // Get a reference to the Firebase Storage location
                val storageRef =
                    firebaseStorage.getReference("item/images/$currentUserId/$imageName")

                // Convert bitmap to byte array
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                suspendCoroutine { continuation ->
                    // Upload image to Firebase Storage
                    storageRef.putBytes(data).addOnSuccessListener {
                        // Get download URL
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            continuation.resume(Resource.success(downloadUrl))
                        }.addOnFailureListener { downloadUrlFailure ->
                            Timber.tag(TAG).e(downloadUrlFailure)

                            val errorMessage = when (downloadUrlFailure) {
                                is UnknownHostException -> R.string.download_url_failure_network
                                else -> R.string.download_url_failure_generic
                            }
                            continuation.resume(Resource.error(errorMessage))
                        }
                    }.addOnFailureListener { uploadFailure ->
                        Timber.tag(TAG).e(uploadFailure)

                        val errorMessage = when (uploadFailure) {
                            is UnknownHostException -> R.string.upload_image_failure_network
                            else -> R.string.upload_image_failure_generic
                        }
                        continuation.resume(Resource.error(errorMessage))
                    }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                Resource.error(R.string.upload_image_failure_generic)
            }
        }
    }

    /**
     * Replaces an existing image in Firebase Storage with a new one.
     *
     * @param bitmap Bitmap image to replace with.
     * @param imageUrl URL of the existing image to replace.
     * @return [ReplaceImage] representing the result of the replacement operation.
     */
    override suspend fun replaceImage(bitmap: Bitmap, imageUrl: String): ReplaceImage {
        return withContext(ioDispatcher) {
            try {
                // Loading state before starting the deletion operation
                Resource.Loading

                // Convert bitmap to byte array
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                // Get a reference to the Firebase Storage location using the existing image URL
                val storageRef = firebaseStorage.getReferenceFromUrl(imageUrl)

                suspendCoroutine { continuation ->
                    // Replace image to Firebase Storage
                    storageRef.putBytes(data).addOnSuccessListener {
                        // Get download URL
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            continuation.resume(Resource.success(downloadUrl))
                        }.addOnFailureListener { downloadUrlFailure ->
                            Timber.tag(TAG).e(downloadUrlFailure)

                            val errorMessage = when (downloadUrlFailure) {
                                is UnknownHostException -> R.string.download_url_failure_network
                                else -> R.string.download_url_failure_generic
                            }
                            continuation.resume(Resource.error(errorMessage))
                        }
                    }.addOnFailureListener { replaceFailure ->
                        Timber.tag(TAG).e(replaceFailure)

                        val errorMessage = when (replaceFailure) {
                            is UnknownHostException -> R.string.replace_image_failure_network
                            else -> R.string.replace_image_failure_generic
                        }
                        continuation.resume(Resource.error(errorMessage))
                    }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                Resource.error(R.string.replace_image_failure_generic)
            }
        }
    }

    /**
     * Deletes an image from Firebase Storage.
     *
     * @param imageUrl URL of the image to delete.
     * @return [DeleteImage] representing the result of the deletion operation.
     */
    override suspend fun deleteImage(imageUrl: String): DeleteImage {
        return withContext(ioDispatcher) {
            try {
                // Loading state before starting the deletion operation
                Resource.Loading

                // Get a reference to the Firebase Storage location using the image URL
                val storageRef = firebaseStorage.getReferenceFromUrl(imageUrl)

                suspendCoroutine { continuation ->
                    // Delete the image from Firebase Storage
                    storageRef.delete().addOnSuccessListener {
                        continuation.resume(Resource.success(true))
                    }.addOnFailureListener { deleteFailure ->
                        Timber.tag(TAG).e(deleteFailure)

                        val errorMessage = when (deleteFailure) {
                            is UnknownHostException -> R.string.delete_image_failure_network
                            else -> R.string.delete_image_failure_generic
                        }
                        continuation.resume(Resource.error(errorMessage))
                    }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                Resource.error(R.string.delete_image_failure_generic)
            }
        }
    }

    companion object {
        private val TAG = ImageRepositoryImpl::class.java.simpleName
    }

}