package com.example.pengelolaansampah.view // Pastikan package ini sesuai

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.FragmentEditDataSuratTugasBinding // Akan dihasilkan oleh View Binding
import com.example.pengelolaansampah.model.SuratTugasModel
import com.example.pengelolaansampah.viewmodel.SampahViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull // Import ini
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditDataSuratTugasFragment : Fragment() {

    private var _binding: FragmentEditDataSuratTugasBinding? = null
    private val binding get() = _binding!!

    private val sampahViewModel: SampahViewModel by activityViewModels()

    private var idKendaraanKruToEdit: Long = 0L

    private var currentKendaraanDanKru: SuratTugasModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditDataSuratTugasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRequiredAsterisk(binding.labelNoKendaraan, "No.Kendaraan *")
        setRequiredAsterisk(binding.labelJenisKendaraan, "Jenis Kendaraan *")
        setRequiredAsterisk(binding.labelJenisPengirimPage3, "Jenis Pengirim *")
        setRequiredAsterisk(binding.labelCrew1, "Crew 1 *")
        setRequiredAsterisk(binding.labelCrew2, "Crew 2 *")
        setRequiredAsterisk(binding.labelCrew3, "Crew 3 *")
        setRequiredAsterisk(binding.labelCrew4, "Crew 4 *")
        setRequiredAsterisk(binding.labelCrew5, "Crew 5 *")

        arguments?.let {
            idKendaraanKruToEdit = it.getLong("idKendaraanKru", 0L)
            if (idKendaraanKruToEdit == 0L) {
                Toast.makeText(requireContext(), "ID Kendaraan & Kru tidak valid.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
                return
            }
        } ?: run {
            Toast.makeText(requireContext(), "ID Kendaraan & Kru tidak ditemukan.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        setupSpinners()

        loadExistingData(idKendaraanKruToEdit)

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

    private fun setupSpinners() {

        val jenisKendaraanOptions = arrayOf("Dumptruck", "Amroll", "Compactor", "Pick Up", "Lainnya")
        val adapterJenisKendaraan = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, jenisKendaraanOptions)
        (binding.spinnerJenisKendaraan as? AutoCompleteTextView)?.setAdapter(adapterJenisKendaraan)


        val jenisPengirimOptionsPage3 = arrayOf("Pemerintah", "Swasta", "Perorangan", "Lainnya")
        val adapterJenisPengirimPage3 = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, jenisPengirimOptionsPage3)
        (binding.spinnerJenisPengirimPage3 as? AutoCompleteTextView)?.setAdapter(adapterJenisPengirimPage3)

        val namaPengemudiOptions = arrayOf("Asep", "Budi", "Cici", "Dedi", "Eko", "Fajar")
        val adapterNamaPengemudi = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, namaPengemudiOptions)
        (binding.spinnerNamaPengemudi as? AutoCompleteTextView)?.setAdapter(adapterNamaPengemudi)

        val crewOptions = arrayOf("Tidak Ada", "Rendi", "Dedi", "Adam", "Raden", "Ridwan", "Gilang", "Hendra")
        val adapterCrew = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, crewOptions)

        (binding.spinnerCrew1 as? AutoCompleteTextView)?.setAdapter(adapterCrew)
        (binding.spinnerCrew2 as? AutoCompleteTextView)?.setAdapter(adapterCrew)
        (binding.spinnerCrew3 as? AutoCompleteTextView)?.setAdapter(adapterCrew)
        (binding.spinnerCrew4 as? AutoCompleteTextView)?.setAdapter(adapterCrew)
        (binding.spinnerCrew5 as? AutoCompleteTextView)?.setAdapter(adapterCrew)
    }

    private fun loadExistingData(id: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                val kendaraanDanKru = sampahViewModel.getKendaraanDanKruById(id).firstOrNull()

                kendaraanDanKru?.let { kdk ->
                    currentKendaraanDanKru = kdk
                    binding.etIdSuratJalan.setText(kdk.idSuratTugas)
                    binding.etNoKendaraan.setText(kdk.nomorKendaraan)

                    (binding.spinnerJenisKendaraan as? AutoCompleteTextView)?.setText(kdk.jenisKendaraan, false)

                    (binding.spinnerNamaPengemudi as? AutoCompleteTextView)?.setText(kdk.namaPengemudi, false)
                    (binding.spinnerCrew1 as? AutoCompleteTextView)?.setText(kdk.crew1, false)
                    (binding.spinnerCrew2 as? AutoCompleteTextView)?.setText(kdk.crew2, false)
                    (binding.spinnerCrew3 as? AutoCompleteTextView)?.setText(kdk.crew3, false)
                    (binding.spinnerCrew4 as? AutoCompleteTextView)?.setText(kdk.crew4, false)
                    (binding.spinnerCrew5 as? AutoCompleteTextView)?.setText(kdk.crew5, false)

                } ?: run {
                    Toast.makeText(requireContext(), "Data Kendaraan & Kru tidak ditemukan.", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun saveEditedData() {
        val existingKdk = currentKendaraanDanKru ?: run {
            Toast.makeText(requireContext(), "Gagal menyimpan: Data asli tidak ditemukan.", Toast.LENGTH_SHORT).show()
            return
        }

        val idSuratTugas = binding.etIdSuratJalan.text.toString().trim()
        val nomorKendaraan = binding.etNoKendaraan.text.toString().trim()
        val jenisKendaraan = binding.spinnerJenisKendaraan.text.toString().trim()
        val namaPengemudi = binding.spinnerNamaPengemudi.text.toString().trim()
        val crew1 = binding.spinnerCrew1.text.toString().trim()
        val crew2 = binding.spinnerCrew2.text.toString().trim()
        val crew3 = binding.spinnerCrew3.text.toString().trim()
        val crew4 = binding.spinnerCrew4.text.toString().trim()
        val crew5 = binding.spinnerCrew5.text.toString().trim()
        var isValid = true

        if (idSuratTugas.isEmpty()) {
            binding.tilIdSuratJalan.error = "ID Surat Jalan tidak boleh kosong"
            isValid = false
        } else {
            binding.tilIdSuratJalan.error = null
        }

        if (nomorKendaraan.isEmpty()) {
            binding.tilNoKendaraan.error = "Nomor Kendaraan tidak boleh kosong"
            isValid = false
        } else {
            binding.tilNoKendaraan.error = null
        }

        if (jenisKendaraan.isEmpty()) {
            binding.tilJenisKendaraan.error = "Jenis Kendaraan tidak boleh kosong"
            isValid = false
        } else {
            binding.tilJenisKendaraan.error = null
        }

        if (namaPengemudi.isEmpty()) {
            binding.tilNamaPengemudi.error = "Nama Pengemudi tidak boleh kosong"
            isValid = false
        } else {
            binding.tilNamaPengemudi.error = null
        }

        if (!isValid) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data yang wajib diisi.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedKendaraanDanKru = existingKdk.copy(
            idSuratTugas = idSuratTugas,
            nomorKendaraan = nomorKendaraan,
            jenisKendaraan = jenisKendaraan,
            namaPengemudi = namaPengemudi,
            crew1 = crew1,
            crew2 = crew2,
            crew3 = crew3,
            crew4 = crew4,
            crew5 = crew5
        )

        sampahViewModel.updateKendaraanDanKru(updatedKendaraanDanKru)

        Toast.makeText(requireContext(), "Data Surat Tugas berhasil diperbarui!", Toast.LENGTH_SHORT).show()

        parentFragmentManager.popBackStack()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}