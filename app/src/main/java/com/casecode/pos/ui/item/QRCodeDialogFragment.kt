package com.casecode.pos.ui.item

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.casecode.pos.databinding.DialogQrCodeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QRCodeDialogFragment : DialogFragment() {

    private var _binding: DialogQrCodeBinding? = null
    private val binding: DialogQrCodeBinding
        get() = _binding!!

    private val args: QRCodeDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogQrCodeBinding.inflate(layoutInflater)

        val builder = MaterialAlertDialogBuilder(requireActivity())
        builder.setView(binding.root)
            .setTitle("Print QR code")
            .setNegativeButton("Cancel") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Print") { dialog, which ->
                // Respond to positive button press
            }

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.item = args.item
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}