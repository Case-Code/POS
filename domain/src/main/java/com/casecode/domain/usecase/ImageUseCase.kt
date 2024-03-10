package com.casecode.domain.usecase

import android.graphics.Bitmap
import com.casecode.domain.repository.DeleteImage
import com.casecode.domain.repository.ImageRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case class for image-related operations.
 *
 * @property imageRepository The repository responsible for handling image operations.
 * @constructor Creates an [ImageUseCase] with the provided [imageRepository].
 */
class ImageUseCase @Inject constructor(private val imageRepository: ImageRepository) {
    /**
     * Uploads an image represented by [bitmap] with the given [imageName].
     *
     * @param bitmap The bitmap image to be uploaded.
     * @param imageName The name to be assigned to the uploaded image.
     * @return A [UploadImage] resource containing the URL of the uploaded image.
     */
    suspend fun uploadImage(bitmap: Bitmap, imageName: String) =
        imageRepository.uploadImage(bitmap, imageName)

    /**
     * Replaces an existing image with the image represented by [bitmap].
     *
     * @param bitmap The bitmap image to replace the existing image.
     * @param imageUrl The URL of the existing image to be replaced.
     * @return A [ReplaceImage] resource containing the URL of the replaced image.
     */
    suspend fun replaceImage(bitmap: Bitmap, imageUrl: String) =
        imageRepository.replaceImage(bitmap, imageUrl)

    /**
     * Deletes the image associated with the given [imageUrl].
     *
     * @param imageUrl The URL of the image to be deleted.
     * @return A [DeleteImage] resource indicating the success or failure of the deletion operation.
     */
    suspend fun deleteImage(imageUrl: String) = flow {
        emit(DeleteImage.loading())

        val resource = if (imageUrl.isEmpty()) {
            DeleteImage.empty()
        } else {
            imageRepository.deleteImage(imageUrl)
        }

        emit(resource)
    }

}
