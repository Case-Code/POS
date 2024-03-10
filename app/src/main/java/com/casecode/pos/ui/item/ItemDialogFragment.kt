package com.casecode.pos.ui.item

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.casecode.domain.model.users.Item
import com.casecode.pos.R
import com.casecode.pos.databinding.DialogItemBinding
import com.casecode.pos.utils.CaptureCustomActivity
import com.casecode.pos.viewmodel.ItemsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

/**
 * A dialog fragment for adding or updating an item.
 *
 * @property isUpdate Flag indicating whether the dialog is for updating an existing item.
 * @property item The item to be updated (if any).
 */
class ItemDialogFragment(
    private val isUpdate: Boolean = false,
    private val item: Item? = null
) : DialogFragment() {

    // View binding for the dialog layout
    private var _binding: DialogItemBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance
    private val viewModel: ItemsViewModel by activityViewModels()

    /**
     * Creates the dialog with the dialog layout.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        _binding = DialogItemBinding.inflate(layoutInflater)
        return builder.setView(binding.root).create()
    }

    /**
     * Inflates the dialog layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    /**
     * Initializes the views and sets up click listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setupViews()
    }

    /**
     * Sets up the views based on whether it's an add or update operation.
     */
    private fun setupViews() {
        // Set title and button text based on add or update operation
        val titleResId = if (isUpdate) R.string.update_item else R.string.add_item
        val buttonTextResId = if (isUpdate) R.string.update_item else R.string.add_item

        // Populate item details if it's an update operation
        item?.let { item ->
            with(binding) {
                textItemTitle.text = getString(titleResId)
                buttonItem.text = getString(buttonTextResId)

                // Load image using Coil
                imvItem.load(item.imageUrl) {
                    placeholder(R.drawable.outline_image_24)
                    error(R.drawable.outline_hide_image_24)
                }
                tilBarcode.isEnabled = false
                imageButtonScanBarcode.visibility = View.GONE
                tilBarcode.editText?.setText(item.sku)
                tilName.editText?.setText(item.name)
                tilPrice.editText?.setText(item.price.toString())
                tilQuantity.editText?.setText(item.quantity.toString())
            }
        }

        // Set click listener for image view to capture image
        binding.imvItem.setOnClickListener { captureImageFromCameraOrGallery() }

        // Set click listener for image view to scan barcode
        binding.imageButtonScanBarcode.setOnClickListener { scanCode() }

        // Set click listener for item button to add or update item
        binding.buttonItem.setOnClickListener {
            if (isValidItemInput()) {
                // Get item and bitmap from ViewModel
                val itemFromDialog = viewModel.getItem()
                val bitmap = viewModel.getBitmap()

                // Upload image and add or update item based on conditions
                itemFromDialog?.let { dialogItem ->
                    if (bitmap != null) {
                        if (item == null) {
                            viewModel.uploadImageAndAddItem(bitmap, dialogItem)
                        } else {
                            viewModel.uploadImageAndUpdateItem(bitmap, dialogItem)
                        }
                    } else {
                        if (item == null) {
                            viewModel.addItem(dialogItem)
                        } else {
                            viewModel.updateItem(dialogItem)
                        }
                    }
                }
                dismiss()
            }
        }
    }

    /**
     * Clears the view binding instance when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Handles the result of image capture or selection.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE_OR_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data

            // Set image URI to image view
            binding.imvItem.setImageURI(imageUri)
        }
    }

    /**
     * Initiates the capture of an image from camera or gallery.
     */
    private fun captureImageFromCameraOrGallery() {
        // Create intents for capturing image from camera and picking from gallery
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        // Check if there are applications available to handle the camera and gallery intents
        val packageManager = requireActivity().packageManager
        val cameraActivities = takePicture.resolveActivity(packageManager)
        val galleryActivities = pickPhoto.resolveActivity(packageManager)

        // Create a chooser intent to let the user select between camera and gallery
        val chooserIntent = Intent.createChooser(Intent(), getString(R.string.select_image))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickPhoto))

        // Start the activity for result if both camera and gallery are available
        if (cameraActivities != null && galleryActivities != null) {
            startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE_OR_PICK)
        } else {
            // Display a message if no camera or gallery apps are found
            Toast.makeText(
                requireContext(),
                getString(R.string.no_camera_or_gallery_apps_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Validates the input fields for item details.
     *
     * @return `true` if the input is valid, `false` otherwise.
     */
    private fun isValidItemInput(): Boolean {
        // Get input values from text fields
        val name = binding.tilName.editText?.text.toString().trim()
        val price = binding.tilPrice.editText?.text.toString().trim()
        val quantity = binding.tilQuantity.editText?.text.toString().trim()
        val barcode = binding.tilBarcode.editText?.text.toString().trim()

        // Check if any required field is empty and show error messages if necessary
        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() || barcode.isEmpty()) {
            if (name.isEmpty()) {
                binding.tilName.error = getString(R.string.name_is_required)
            }
            if (price.isEmpty()) {
                binding.tilPrice.error = getString(R.string.price_is_required)
            }
            if (quantity.isEmpty()) {
                binding.tilQuantity.error = getString(R.string.quantity_is_required)
            }
            if (barcode.isEmpty()) {
                binding.tilBarcode.error = getString(R.string.barcode_is_required)
            }
            return false
        }

        // Create the item object
        val item = Item(
            name = name,
            price = price.toDouble(),
            quantity = quantity.toDouble(),
            sku = barcode,
            unitOfMeasurement = null,
            imageUrl = null
        )

        // Set bitmap to ViewModel if available
        val bitmapDrawable = binding.imvItem.drawable as? BitmapDrawable
        val bitmap = bitmapDrawable?.bitmap
        if (bitmap != null) {
            // If bitmap is not null, set it to the item
            viewModel.setBitmap(bitmap)
        }

        // Set the item to the ViewModel
        viewModel.setItem(item)

        return true
    }

    /**
     * Clears the ViewModel when the dialog is dismissed.
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // Reset ViewModel data
        viewModel.clearData()
    }

    private fun scanCode() {
        val options = ScanOptions().apply {
            setPrompt("Scan a barcode")
            setBeepEnabled(false)
            setOrientationLocked(true)
            captureActivity = CaptureCustomActivity::class.java
        }
        barLauncher.launch(options)
    }

    private val barLauncher = registerForActivityResult(ScanContract()) { result ->
        result.contents?.let {
            // Handle the scanned barcode result
            // For example, set it to a text input field
            binding.tilBarcode.editText?.setText(it)
            // Simulate pressing "Done"
            binding.tilBarcode.editText?.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }

    /**
     * Companion object containing constants and a factory method to create instances of [ItemDialogFragment].
     */
    companion object {
        const val REQUEST_IMAGE_CAPTURE_OR_PICK = 0
        const val ITEM_DIALOG_FRAGMENT = "itemDialogFragment"

        /**
         * Factory method to create a new instance of [ItemDialogFragment].
         *
         * @param isUpdate Flag indicating whether the dialog is for updating an existing item.
         * @param item The item to be updated (if any).
         * @return A new instance of [ItemDialogFragment].
         */
        fun newInstance(isUpdate: Boolean = false, item: Item? = null): ItemDialogFragment {
            return ItemDialogFragment(isUpdate, item)
        }
    }
}