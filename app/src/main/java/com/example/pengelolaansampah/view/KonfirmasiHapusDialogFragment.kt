package com.example.pengelolaansampah.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.pengelolaansampah.databinding.FragmentKonfirmasiHapusDialogBinding


class KonfirmasiHapusDialogFragment : DialogFragment() {

    private var _binding: FragmentKonfirmasiHapusDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val REQUEST_KEY_DELETE_CONFIRM = "delete_confirmation_request_key"
        const val BUNDLE_KEY_DELETE_RESULT = "delete_result"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKonfirmasiHapusDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnYaHapusSekarang.setOnClickListener {

            setFragmentResult(REQUEST_KEY_DELETE_CONFIRM, bundleOf(BUNDLE_KEY_DELETE_RESULT to true))
            dismiss()
        }

        binding.btnBatal.setOnClickListener {
            setFragmentResult(REQUEST_KEY_DELETE_CONFIRM, bundleOf(BUNDLE_KEY_DELETE_RESULT to false))
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}