package com.example.pengelolaansampah.view // Pastikan package ini sesuai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.FragmentEditDataLokasiBinding // Akan dihasilkan oleh View Binding
import com.example.pengelolaansampah.model.LokasiSampahModel
import com.example.pengelolaansampah.viewmodel.SampahViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale // Untuk format volume
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat // Untuk mendapatkan warna

@AndroidEntryPoint
class EditDataLokasiFragment : Fragment() {

    private var _binding: FragmentEditDataLokasiBinding? = null
    private val binding get() = _binding!!

    private val sampahViewModel: SampahViewModel by activityViewModels()

    private var idLokasiToEdit: Long = 0L

    private var currentLokasiSampah: LokasiSampahModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDataLokasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRequiredAsterisk(binding.labelLokasiSumberSampah, "Lokasi sumber sampah *")
        setRequiredAsterisk(binding.labelAlamatLokasi, "Alamat lokasi *")
        setRequiredAsterisk(binding.labelJenisPengirim, "Jenis pengirim *")
        setRequiredAsterisk(binding.labelStatusKeterangkutan, "Status keterangkutan *")
        setRequiredAsterisk(binding.labelVolumeTerangkut, "Volume terangkut *")

        arguments?.let {
            idLokasiToEdit = it.getLong("idLokasi", 0L)
            if (idLokasiToEdit == 0L) {
                Toast.makeText(requireContext(), "ID Lokasi tidak valid.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
                return
            }
        } ?: run {
            Toast.makeText(requireContext(), "ID Lokasi tidak ditemukan.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        setupSpinners()

        loadExistingData(idLokasiToEdit)

        binding.btnSimpan.setOnClickListener {
            saveEditedData()
        }

        binding.btnBatal.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupSpinners() {

        val jenisPengirimOptions = arrayOf("Pemerintah", "Swasta", "Perorangan", "Lainnya")
        val adapterJenisPengirim = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, jenisPengirimOptions)
        (binding.spinnerJenisPengirim as? AutoCompleteTextView)?.setAdapter(adapterJenisPengirim)

        val statusKeterangkutanOptions = arrayOf("Belum", "Sudah")
        val adapterStatusKeterangkutan = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, statusKeterangkutanOptions)
        (binding.spinnerStatusKeterangkutan as? AutoCompleteTextView)?.setAdapter(adapterStatusKeterangkutan)
    }

    private fun setRequiredAsterisk(textView: TextView, fullText: String) {
        val asteriskIndex = fullText.indexOf('*')
        if (asteriskIndex != -1) {
            val spannableString = SpannableString(fullText)
            val redColor = ContextCompat.getColor(requireContext(), R.color.red_asterisk)

            spannableString.setSpan(
                ForegroundColorSpan(redColor),
                asteriskIndex,
                asteriskIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannableString
        } else {
            textView.text = fullText
        }
    }

    private fun loadExistingData(id: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val lokasiSampah = sampahViewModel.getLokasiSampahById(id).firstOrNull()

                lokasiSampah?.let { l ->
                    currentLokasiSampah = l

                    binding.etKodeSuratJalan.setText(l.kodeSuratJalan)
                    binding.etLokasiSumberSampah.setText(l.lokasiSumberSampah)
                    binding.etAlamatLokasi.setText(l.alamatLokasi)

                    (binding.spinnerJenisPengirim as? AutoCompleteTextView)?.setText(l.jenisPengirim, false)
                    (binding.spinnerStatusKeterangkutan as? AutoCompleteTextView)?.setText(l.statusKeterangan, false)

                    binding.etVolumeTerangkut.setText(String.format(Locale.US, "%.2f", l.volumeTerangkut))

                } ?: run {
                    Toast.makeText(requireContext(), "Data Lokasi tidak ditemukan.", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun saveEditedData() {
        val existingLokasi = currentLokasiSampah ?: run {
            Toast.makeText(requireContext(), "Gagal menyimpan: Data asli tidak ditemukan.", Toast.LENGTH_SHORT).show()
            return
        }

        val kodeSuratJalan = binding.etKodeSuratJalan.text.toString().trim()
        val lokasiSumberSampah = binding.etLokasiSumberSampah.text.toString().trim()
        val alamatLokasi = binding.etAlamatLokasi.text.toString().trim()
        val jenisPengirim = binding.spinnerJenisPengirim.text.toString().trim()
        val statusKeterangan = binding.spinnerStatusKeterangkutan.text.toString().trim()
        val volumeTerangkutStr = binding.etVolumeTerangkut.text.toString().trim()

        // --- Validasi Input ---
        var isValid = true

        if (lokasiSumberSampah.isEmpty()) {
            binding.tilLokasiSumberSampah.error = "Lokasi sumber sampah tidak boleh kosong"
            isValid = false
        } else {
            binding.tilLokasiSumberSampah.error = null
        }

        if (alamatLokasi.isEmpty()) {
            binding.tilAlamatLokasi.error = "Alamat lokasi tidak boleh kosong"
            isValid = false
        } else {
            binding.tilAlamatLokasi.error = null
        }

        val volumeTerangkut: Double? = try {
            if (volumeTerangkutStr.isEmpty()) {
                binding.tilVolumeTerangkut.error = "Volume terangkut tidak boleh kosong"
                isValid = false
                null
            } else {
                val parsedVolume = volumeTerangkutStr.replace(",", ".").toDouble()
                if (parsedVolume <= 0) {
                    binding.tilVolumeTerangkut.error = "Volume harus lebih dari 0"
                    isValid = false
                    null
                } else {
                    binding.tilVolumeTerangkut.error = null
                    parsedVolume
                }
            }
        } catch (e: NumberFormatException) {
            binding.tilVolumeTerangkut.error = "Volume tidak valid (gunakan angka)"
            isValid = false
            null
        }

        if (!isValid || volumeTerangkut == null) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data yang wajib diisi.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedLokasiSampah = existingLokasi.copy(
            kodeSuratJalan = kodeSuratJalan,
            lokasiSumberSampah = lokasiSumberSampah,
            alamatLokasi = alamatLokasi,
            jenisPengirim = jenisPengirim,
            statusKeterangan = statusKeterangan,
            volumeTerangkut = volumeTerangkut
        )

        sampahViewModel.updateLokasiSampah(updatedLokasiSampah)

        Toast.makeText(requireContext(), "Data Lokasi berhasil diperbarui!", Toast.LENGTH_SHORT).show()

        parentFragmentManager.popBackStack()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}