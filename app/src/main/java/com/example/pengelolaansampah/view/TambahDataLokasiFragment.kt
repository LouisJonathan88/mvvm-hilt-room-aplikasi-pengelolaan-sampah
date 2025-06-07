package com.example.pengelolaansampah.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels // Menggunakan activityViewModels untuk Shared ViewModel
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.FragmentTambahDataLokasiBinding
import com.example.pengelolaansampah.model.LokasiSampahModel
import com.example.pengelolaansampah.viewmodel.SampahViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat // Untuk mendapatkan warna


@AndroidEntryPoint
class TambahDataLokasiFragment : Fragment() {

    private var _binding: FragmentTambahDataLokasiBinding? = null
    private val binding get() = _binding!!

    private val sampahViewModel: SampahViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahDataLokasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRequiredAsterisk(binding.labelLokasiSumberSampah, "Lokasi sumber sampah *")
        setRequiredAsterisk(binding.labelAlamatLokasi, "Alamat lokasi *")
        setRequiredAsterisk(binding.labelJenisPengirim, "Jenis pengirim *")
        setRequiredAsterisk(binding.labelStatusKeterangkutan, "Status keterangkutan *")
        setRequiredAsterisk(binding.labelVolumeTerangkut, "Volume terangkut *")

        val jenisPengirimOptions = arrayOf("Pemerintah", "Swasta", "Perorangan", "Lainnya")
        val adapterJenisPengirim = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, jenisPengirimOptions)
        (binding.spinnerJenisPengirim as? AutoCompleteTextView)?.setAdapter(adapterJenisPengirim)

        (binding.spinnerJenisPengirim as? AutoCompleteTextView)?.setText(jenisPengirimOptions[0], false)

        val statusKeterangkutanOptions = arrayOf("Belum", "Sudah")
        val adapterStatusKeterangkutan = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, statusKeterangkutanOptions)
        (binding.spinnerStatusKeterangkutan as? AutoCompleteTextView)?.setAdapter(adapterStatusKeterangkutan)

        (binding.spinnerStatusKeterangkutan as? AutoCompleteTextView)?.setText(statusKeterangkutanOptions[0], false)

        binding.btnSelanjutnya.setOnClickListener {
            saveLokasiDataAndNavigate()
        }

        binding.btnSebelumnya.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.step1.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step1.setTextColor(resources.getColor(android.R.color.white, null))

        binding.step2.setBackgroundResource(R.drawable.circle_grey_border)
        binding.step2.setTextColor(resources.getColor(R.color.grey_700, null))

        binding.line23.setBackgroundResource(R.color.grey_200)

        binding.step3.setBackgroundResource(R.drawable.circle_grey_border)
        binding.step3.setTextColor(resources.getColor(R.color.grey_700, null))
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

    private fun saveLokasiDataAndNavigate() {

        val kodeSuratJalan = binding.etKodeSuratJalan.text.toString().trim()
        val lokasiSumberSampah = binding.etLokasiSumberSampah.text.toString().trim()
        val alamatLokasi = binding.etAlamatLokasi.text.toString().trim()
        val jenisPengirim = binding.spinnerJenisPengirim.text.toString().trim()
        val statusKeterangan = binding.spinnerStatusKeterangkutan.text.toString().trim()
        val volumeTerangkutStr = binding.etVolumeTerangkut.text.toString().trim()


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

        val lokasiSampah = LokasiSampahModel(
            kodeSuratJalan = kodeSuratJalan,
            lokasiSumberSampah = lokasiSumberSampah,
            alamatLokasi = alamatLokasi,
            jenisPengirim = jenisPengirim,
            statusKeterangan = statusKeterangan,
            volumeTerangkut = volumeTerangkut
        )

        sampahViewModel.insertLokasiSampah(lokasiSampah)

        Toast.makeText(requireContext(), "Data Lokasi berhasil disimpan!", Toast.LENGTH_SHORT).show()

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, TambahDataInfoRitasiFragment())
            .addToBackStack(null)
            .commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}