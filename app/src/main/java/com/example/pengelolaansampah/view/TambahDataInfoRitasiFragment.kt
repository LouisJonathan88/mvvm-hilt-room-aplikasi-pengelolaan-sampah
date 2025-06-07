package com.example.pengelolaansampah.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.FragmentTambahDataInfoRitasiBinding
import com.example.pengelolaansampah.model.RitasiModel
import com.example.pengelolaansampah.viewmodel.SampahViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat // Untuk mendapatkan warna
import com.google.android.material.R as MaterialR



@AndroidEntryPoint
class TambahDataInfoRitasiFragment : Fragment() {

    private var _binding: FragmentTambahDataInfoRitasiBinding? = null
    private val binding get() = _binding!!

    // Menggunakan activityViewModels() untuk mendapatkan instance ViewModel yang dibagikan
    private val sampahViewModel: SampahViewModel by activityViewModels()

    // Calendar instance untuk DatePickerDialog dan TimePickerDialog
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahDataInfoRitasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRequiredAsterisk(binding.labelTanggal, "Tanggal *")
        setRequiredAsterisk(binding.labelVolumeSampah, "Volume Sampah *")
        setRequiredAsterisk(binding.labelBruto, "Bruto *")
        setRequiredAsterisk(binding.labelTarra, "Tara *")

        binding.etTanggal.setOnClickListener {
            showDatePicker()
        }

        binding.etJamMasuk.setOnClickListener {
            showTimePicker(isJamMasuk = true)
        }

        binding.etJamKeluar.setOnClickListener {
            showTimePicker(isJamMasuk = false)
        }

        binding.etBruto.doAfterTextChanged {
            calculateNetto()
        }
        binding.etTarra.doAfterTextChanged {
            calculateNetto()
        }

        binding.btnSelanjutnya.setOnClickListener {
            saveRitasiDataAndNavigate()
        }

        binding.btnSebelumnya.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.step1.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step2.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step3.setBackgroundResource(R.drawable.circle_grey_border)

        binding.step1.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step1.setTextColor(resources.getColor(android.R.color.white, null))

        binding.step2.setBackgroundResource(R.drawable.circle_green_bg)
        binding.step2.setTextColor(resources.getColor(android.R.color.white, null))

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


    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale("id", "ID"))
                binding.etTanggal.setText(dateFormat.format(calendar.time))
            },
            year, month, day
        )
        datePickerDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        datePickerDialog.show()

        datePickerDialog.setOnShowListener {
            datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.green_primary)
            )
            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.grey_700)
            )
        }

        datePickerDialog.show()
    }

    private fun showTimePicker(isJamMasuk: Boolean) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerDialogTheme,
            { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                val formattedTime = timeFormat.format(calendar.time)

                if (isJamMasuk) {
                    binding.etJamMasuk.setText(formattedTime)
                } else {
                    binding.etJamKeluar.setText(formattedTime)
                }
            },
            hour, minute, true
        )
        timePickerDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        timePickerDialog.show()

        timePickerDialog.setOnShowListener {
            timePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.green_primary)
            )
            timePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.grey_700)
            )
        }

        timePickerDialog.show()
    }


    private fun calculateNetto() {
        val brutoStr = binding.etBruto.text.toString().trim().replace(",", ".")
        val tarraStr = binding.etTarra.text.toString().trim().replace(",", ".")

        val bruto = brutoStr.toDoubleOrNull() ?: 0.0
        val tarra = tarraStr.toDoubleOrNull() ?: 0.0

        val netto = bruto - tarra
        binding.etNettoTonase.setText(String.format(Locale("id", "ID"), "%.2f", netto))
    }



    private fun saveRitasiDataAndNavigate() {
        val idRitasiTpa = binding.etIdRitasiTpa.text.toString().trim()
        val idRitasiTpaInput = binding.etIdRitasiTpa.text.toString().trim()
        val tanggal = binding.etTanggal.text.toString().trim()
        val volumeSampahStr = binding.etVolumeSampah.text.toString().trim()
        val jamMasuk = binding.etJamMasuk.text.toString().trim()
        val jamKeluar = binding.etJamKeluar.text.toString().trim()
        val petugasPencatat = binding.etPetugasPencatat.text.toString().trim()
        val brutoStr = binding.etBruto.text.toString().trim()
        val tarraStr = binding.etTarra.text.toString().trim()
        val nettoStr = binding.etNettoTonase.text.toString().trim()

        var isValid = true

        if (sampahViewModel.currentLokasiId == 0L) {
            Toast.makeText(requireContext(), "ID Lokasi dari form sebelumnya tidak ditemukan. Harap isi ulang Form 1.", Toast.LENGTH_LONG).show()
            isValid = false
            parentFragmentManager.popBackStack()
            return
        }

        if (tanggal.isEmpty()) {
            binding.tilTanggal.error = "Tanggal tidak boleh kosong"
            isValid = false
        } else {
            binding.tilTanggal.error = null
        }

        val volumeSampah: Double? = try {
            if (volumeSampahStr.isEmpty()) {
                binding.tilVolumeSampah.error = "Volume sampah tidak boleh kosong"
                isValid = false
                null
            } else {
                val parsedVolume = volumeSampahStr.replace(",", ".").toDouble()
                if (parsedVolume <= 0) {
                    binding.tilVolumeSampah.error = "Volume harus lebih dari 0"
                    isValid = false
                    null
                } else {
                    binding.tilVolumeSampah.error = null
                    parsedVolume
                }
            }
        } catch (e: NumberFormatException) {
            binding.tilVolumeSampah.error = "Volume tidak valid (gunakan angka)"
            isValid = false
            null
        }

        if (jamMasuk.isEmpty()) {
            binding.tilJamMasuk.error = "Jam masuk tidak boleh kosong"
            isValid = false
        } else {
            binding.tilJamMasuk.error = null
        }

        if (jamKeluar.isEmpty()) {
            binding.tilJamKeluar.error = "Jam keluar tidak boleh kosong"
            isValid = false
        } else {
            binding.tilJamKeluar.error = null
        }

        if (petugasPencatat.isEmpty()) {
            binding.tilPetugasPencatat.error = "Petugas pencatat tidak boleh kosong"
            isValid = false
        } else {
            binding.tilPetugasPencatat.error = null
        }

        val bruto: Double? = try {
            if (brutoStr.isEmpty()) {
                binding.tilBruto.error = "Bruto tidak boleh kosong"
                isValid = false
                null
            } else {
                val parsedBruto = brutoStr.replace(",", ".").toDouble()
                if (parsedBruto < 0) {
                    binding.tilBruto.error = "Bruto tidak boleh negatif"
                    isValid = false
                    null
                } else {
                    binding.tilBruto.error = null
                    parsedBruto
                }
            }
        } catch (e: NumberFormatException) {
            binding.tilBruto.error = "Bruto tidak valid"
            isValid = false
            null
        }

        val tarra: Double? = try {
            if (tarraStr.isEmpty()) {
                binding.tilTarra.error = "Tarra tidak boleh kosong"
                isValid = false
                null
            } else {
                val parsedTarra = tarraStr.replace(",", ".").toDouble()
                if (parsedTarra < 0) {
                    binding.tilTarra.error = "Tarra tidak boleh negatif"
                    isValid = false
                    null
                } else {
                    binding.tilTarra.error = null
                    parsedTarra
                }
            }
        } catch (e: NumberFormatException) {
            binding.tilTarra.error = "Tarra tidak valid"
            isValid = false
            null
        }

        val netto: Double? = try {
            if (nettoStr.isEmpty() || nettoStr.replace(",", ".").toDoubleOrNull() == null) {
                binding.tilNettoTonase.error = "Netto tidak valid"
                isValid = false
                null
            } else {
                binding.tilNettoTonase.error = null
                nettoStr.replace(",", ".").toDouble()
            }
        } catch (e: NumberFormatException) {
            binding.tilNettoTonase.error = "Netto tidak valid"
            isValid = false
            null
        }


        if (!isValid || volumeSampah == null || bruto == null || tarra == null || netto == null) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data yang wajib diisi dengan benar.", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat objek Ritasi
        val ritasi = RitasiModel(
            idLokasi = sampahViewModel.currentLokasiId,
            externalRitasiTpaId = idRitasiTpaInput,
            tanggalRitasi = tanggal,
            volumeSampah = volumeSampah,
            jamMasuk = jamMasuk,
            jamKeluar = jamKeluar,
            petugasPencatat = petugasPencatat,
            bruto = bruto,
            tarra = tarra,
            netto = netto
        )

        sampahViewModel.insertRitasi(ritasi)

        Toast.makeText(requireContext(), "Data Ritasi berhasil disimpan!", Toast.LENGTH_SHORT).show()

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, TambahDataSuratTugasFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}