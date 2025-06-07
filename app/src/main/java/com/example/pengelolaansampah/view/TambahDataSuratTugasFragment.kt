package com.example.pengelolaansampah.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.FragmentTambahDataSuratTugasBinding
import com.example.pengelolaansampah.model.SuratTugasModel
import com.example.pengelolaansampah.viewmodel.SampahViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat

@AndroidEntryPoint
class TambahDataSuratTugasFragment : Fragment() {

    private var _binding: FragmentTambahDataSuratTugasBinding? = null
    private val binding get() = _binding!!

    private val sampahViewModel: SampahViewModel by activityViewModels()

    private val crewOptions = arrayOf("Rendi", "Dedi", "Adam", "Rade", "Ridwan ", "Angel", "Ica", "Dinda", "Clara", "Dimas")
    private lateinit var crewSpinners: List<AutoCompleteTextView>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahDataSuratTugasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crewSpinners = listOf(
            binding.spinnerCrew1,
            binding.spinnerCrew2,
            binding.spinnerCrew3,
            binding.spinnerCrew4,
            binding.spinnerCrew5
        )

        setRequiredAsterisk(binding.labelNoKendaraan, "No.Kendaraan *")
        setRequiredAsterisk(binding.labelJenisKendaraan, "Jenis Kendaraan *")
        setRequiredAsterisk(binding.labelJenisPengirimPage3, "Jenis Pengirim *")
        setRequiredAsterisk(binding.labelCrew1, "Crew 1 *")
        setRequiredAsterisk(binding.labelCrew2, "Crew 2 *")
        setRequiredAsterisk(binding.labelCrew3, "Crew 3 *")
        setRequiredAsterisk(binding.labelCrew4, "Crew 4 *")
        setRequiredAsterisk(binding.labelCrew5, "Crew 5 *")

        setupSpinners()

        binding.btnSimpan.setOnClickListener {
            saveSuratTugasData()
        }

        binding.btnBatal.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.step1.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step1.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))


        binding.line12.setBackgroundResource(R.color.green_primary)

        binding.step2.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step2.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

        binding.line23.setBackgroundResource(R.color.green_primary)

        binding.step3.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step3.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

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
        (binding.spinnerJenisKendaraan as? AutoCompleteTextView)?.setText(jenisKendaraanOptions[0], false)

        val jenisPengirimOptionsPage3 = arrayOf("Pemerintah", "Swasta", "Perorangan", "Lainnya")
        val adapterJenisPengirimPage3 = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, jenisPengirimOptionsPage3)
        (binding.spinnerJenisPengirimPage3 as? AutoCompleteTextView)?.setAdapter(adapterJenisPengirimPage3)
        (binding.spinnerJenisPengirimPage3 as? AutoCompleteTextView)?.setText(jenisPengirimOptionsPage3[0], false)

        val namaPengemudiOptions = arrayOf("Asep", "Arjuna", "Arif", "Dedi", "Ari")
        val adapterNamaPengemudi = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, namaPengemudiOptions)
        (binding.spinnerNamaPengemudi as? AutoCompleteTextView)?.setAdapter(adapterNamaPengemudi)
        (binding.spinnerNamaPengemudi as? AutoCompleteTextView)?.setText(namaPengemudiOptions[0], false)

        initializeCrewSpinners()

        crewSpinners.forEach { spinner ->
            (spinner as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
                updateCrewAdapters()
            }

            (spinner as? AutoCompleteTextView)?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    spinner.post { updateCrewAdapters() }
                }
            })
        }

    }

    private fun initializeCrewSpinners() {
        val initialSelectedCrews = mutableSetOf<String>()

        crewSpinners.forEachIndexed { index, spinner ->
            val currentSpinner = spinner

            var crewToAssign: String? = null

            if (index < 5 && index < crewOptions.size) {
                val predefinedCrew = crewOptions[index]
                crewToAssign = predefinedCrew
                initialSelectedCrews.add(predefinedCrew)
            } else {

                currentSpinner.setText("", false)
            }

            if (currentSpinner != null && crewToAssign != null) {
                currentSpinner.setText(crewToAssign, false)
            } else if (currentSpinner != null && index >= 5) {
                currentSpinner.setText("", false)
            }
        }

        updateCrewAdapters()
    }
    private fun updateCrewAdapters() {
        val selectedCrews = mutableSetOf<String>()

        crewSpinners.forEach { spinner ->
            val selectedText = spinner.text.toString()
            if (selectedText.isNotEmpty() && crewOptions.contains(selectedText)) {
                selectedCrews.add(selectedText)
            }
        }

        crewSpinners.forEach { currentSpinner ->
            val currentSelectedText = currentSpinner.text.toString()

            val availableOptions = crewOptions.filter { it !in selectedCrews || it == currentSelectedText }.toMutableList()

            val newAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, availableOptions)
            currentSpinner.setAdapter(newAdapter)

            if (currentSelectedText.isNotEmpty() && !availableOptions.contains(currentSelectedText)) {
                currentSpinner.setText("", false)
            }
        }
    }

    private fun saveSuratTugasData() {
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

        if (sampahViewModel.currentRitasiId == 0L) {
            Toast.makeText(requireContext(), "ID Ritasi dari form sebelumnya tidak ditemukan. Harap isi ulang Form 2.", Toast.LENGTH_LONG).show()
            isValid = false
            parentFragmentManager.popBackStack()
            return
        }

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

        val crews = listOf(crew1, crew2, crew3, crew4, crew5)

        val validSelectedCrews = mutableSetOf<String>()
        var crewValidationPassed = true
        crewSpinners.forEachIndexed { index, spinner ->
            val crewValue = spinner.text.toString().trim()
            val til = when(index) {
                0 -> binding.tilCrew1
                1 -> binding.tilCrew2
                2 -> binding.tilCrew3
                3 -> binding.tilCrew4
                4 -> binding.tilCrew5
                else -> null
            }

            if (crewValue.isEmpty()) {
                til?.error = "Crew tidak boleh kosong"
                crewValidationPassed = false
            } else if (validSelectedCrews.contains(crewValue)) {
                til?.error = "Nama crew sudah dipilih"
                crewValidationPassed = false
            } else {
                til?.error = null
                validSelectedCrews.add(crewValue)
            }
        }
        if (!crewValidationPassed) {
            isValid = false
        }


        if (!isValid) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data yang wajib diisi.", Toast.LENGTH_SHORT).show()
            return
        }

        val kendaraanDanKru = SuratTugasModel(
            idRitasi = sampahViewModel.currentRitasiId,
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

        sampahViewModel.insertKendaraanDanKru(kendaraanDanKru)

        Toast.makeText(requireContext(), "Data Surat Tugas berhasil disimpan!", Toast.LENGTH_SHORT).show()

        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}