package com.casecode.domain.repository

import android.graphics.Bitmap
import com.casecode.domain.utils.Resource

typealias UploadImage = Resource<String>
typealias ReplaceImage = Resource<String>
typealias DeleteImage = Resource<Boolean>

/**
 * An interface defining methods for image operations such as uploading, replacing, and deleting images.
 */
interface ImageRepository {
    /**
     * Uploads an image represented by [bitmap] with the given [imageName].
     *
     * @param bitmap The bitmap image to be uploaded.
     * @param imageName The name to be assigned to the uploaded image.
     * @return A [UploadImage] resource containing the URL of the uploaded image.
     */
    suspend fun uploadImage(bitmap: Bitmap, imageName: String): UploadImage

    /**
     * Replaces an existing image with the image represented by [bitmap].
     *
     * @param bitmap The bitmap image to replace the existing image.
     * @param imageUrl The URL of the existing image to be replaced.
     * @return A [ReplaceImage] resource containing the URL of the replaced image.
     */
    suspend fun replaceImage(bitmap: Bitmap, imageUrl: String): ReplaceImage

    /**
     * Deletes the image associated with the given [imageUrl].
     *
     * @param imageUrl The URL of the image to be deleted.
     * @return A [DeleteImage] resource indicating the success or failure of the deletion operation.
     */
    suspend fun deleteImage(imageUrl: String): DeleteImage
}