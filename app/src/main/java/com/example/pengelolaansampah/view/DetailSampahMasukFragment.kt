package com.example.pengelolaansampah.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.FragmentDetailSampahMasukBinding
import com.example.pengelolaansampah.model.FullSampahDetailModel
import com.example.pengelolaansampah.viewmodel.SampahViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class DetailSampahMasukFragment : Fragment() {

    private var _binding: FragmentDetailSampahMasukBinding? = null
    private val binding get() = _binding!!

    private val sampahViewModel: SampahViewModel by activityViewModels()

    private var detailRitasiId: Long = 0L
    private var currentFullSampahDetail: FullSampahDetailModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailSampahMasukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            detailRitasiId = it.getLong("ritasiId", 0L)
            Log.d("DetailFragment", "Received ritasiId: $detailRitasiId")
            if (detailRitasiId == 0L) {
                Toast.makeText(
                    requireContext(),
                    "ID Ritasi tidak valid. Kembali.",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.popBackStack()
                return
            }
        } ?: run {
            Toast.makeText(
                requireContext(),
                "ID Ritasi tidak ditemukan. Kembali.",
                Toast.LENGTH_SHORT
            ).show()
            parentFragmentManager.popBackStack()
            return
        }

        setFragmentResultListener(KonfirmasiHapusDialogFragment.REQUEST_KEY_DELETE_CONFIRM) { requestKey, bundle ->
            val result = bundle.getBoolean(KonfirmasiHapusDialogFragment.BUNDLE_KEY_DELETE_RESULT)
            if (result) {
                deleteSampahMasuk()
            } else {
                Toast.makeText(requireContext(), "Penghapusan dibatalkan.", Toast.LENGTH_SHORT).show()
            }
        }

        setEditButtonsEnabled(false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loadDetailData(detailRitasiId)
            }
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.iconEditSuratTugas.setOnClickListener {
            handleEditClick(
                "Info Surat Tugas",
                currentFullSampahDetail?.idKendaraanKru,
                EditDataSuratTugasFragment()
            )
        }

        binding.iconEditRitasi.setOnClickListener {
            handleEditClick(
                "Info Ritasi",
                currentFullSampahDetail?.idRitasi,
                EditDataInfoRitasiFragment()
            )
        }

        binding.iconEditLokasi.setOnClickListener {
            handleEditClick(
                "Info Lokasi Sumber Sampah",
                currentFullSampahDetail?.idLokasi,
                EditDataLokasiFragment()
            )
        }

        binding.btnHapusDataLayout.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun setEditButtonsEnabled(enabled: Boolean) {
        binding.iconEditSuratTugas.isEnabled = enabled
        binding.iconEditRitasi.isEnabled = enabled
        binding.iconEditLokasi.isEnabled = enabled
    }

    private fun handleEditClick(fragmentName: String, idToPass: Long?, targetFragment: Fragment) {
        if (currentFullSampahDetail != null && idToPass != null && idToPass != 0L) {
            val bundle = Bundle().apply {
                when (targetFragment) {
                    is EditDataSuratTugasFragment -> putLong("idKendaraanKru", idToPass)
                    is EditDataInfoRitasiFragment -> putLong("idRitasi", idToPass)
                    is EditDataLokasiFragment -> putLong("idLokasi", idToPass)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, targetFragment.apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        } else {
            Toast.makeText(
                requireContext(),
                "Data detail $fragmentName belum dimuat atau tidak valid. Coba lagi.",
                Toast.LENGTH_SHORT
            ).show()
            Log.w(
                "DetailFragment",
                "Edit click failed. currentFullSampahDetail: $currentFullSampahDetail, ID: $idToPass"
            )
        }
    }

    private suspend fun loadDetailData(ritasiId: Long) {
        Log.d("DetailFragment", "Starting loadDetailData for ritasiId: $ritasiId")
        sampahViewModel.getFullSampahDetailById(ritasiId).collectLatest { detail ->
            if (detail != null) {
                Log.d("DetailFragment", "Data loaded successfully for ritasiId: ${detail.idRitasi}")
                currentFullSampahDetail = detail
                setEditButtonsEnabled(true)

                binding.valueIdSuratJalanDtl.text = "#${detail.idSuratTugas}"
                binding.valueNoKendaraanDtl.text = detail.nomorKendaraan
                binding.valueJenisPengirimDtl.text = detail.jenisPengirimLokasi
                binding.valueJenisKendaraanDtl.text = detail.jenisKendaraan
                binding.valueNamaPengemudiDtl.text = detail.namaPengemudi
                binding.valueCrew1Dtl.text = detail.crew1
                binding.valueCrew2Dtl.text = detail.crew2
                binding.valueCrew3Dtl.text = detail.crew3
                binding.valueCrew4Dtl.text = detail.crew4
                binding.valueCrew5Dtl.text = detail.crew5

                binding.valueIdRitasiTpaDtl.text = "#${detail.externalRitasiTpaId}"
                binding.valueTanggalDtl.text = detail.tanggalRitasi
                binding.valueVolumeSampahDtl.text =
                    String.format(Locale("id", "ID"), "%.2f ton", detail.volumeSampah)
                binding.valueJamMasukDtl.text = detail.jamMasuk
                binding.valueJamKeluarDtl.text = detail.jamKeluar
                binding.valuePetugasPencatatDtl.text = detail.petugasPencatat
                binding.valueBrutoDtl.text =
                    String.format(Locale("id", "ID"), "%.2f kg", detail.bruto)
                binding.valueTarraDtl.text =
                    String.format(Locale("id", "ID"), "%.2f kg", detail.tarra)
                binding.valueNettoTonaseDtl.text =
                    String.format(Locale("id", "ID"), "%.2f kg", detail.netto)

                binding.valueKodeSuratJalanDtl.text = "#${detail.kodeSuratJalan}"
                binding.valueLokasiSumberSampahDtl.text = detail.lokasiSumberSampah
                binding.valueStatusKeterangkutanDtl.text = detail.statusKeterangan
                binding.valueJenisPengirimLokasiDtl.text = detail.jenisPengirimLokasi
                binding.valueAlamatLokasiDtl.text = detail.alamatLokasi
                binding.valueVolumeTerangkutDtl.text =
                    String.format(Locale("id", "ID"), "%.2f ton", detail.volumeTerangkut)

                binding.cardInfoSuratTugas.visibility = View.VISIBLE
                binding.cardInfoRitasi.visibility = View.VISIBLE
                binding.cardInfoLokasi.visibility = View.VISIBLE

            } else {
                Log.w("DetailFragment", "FullSampahDetail not found for ritasiId: $ritasiId")
                currentFullSampahDetail = null
                setEditButtonsEnabled(false)
                Toast.makeText(requireContext(), "Detail data tidak ditemukan.", Toast.LENGTH_LONG)
                    .show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialog = KonfirmasiHapusDialogFragment()
        dialog.show(parentFragmentManager, "KonfirmasiHapusDialogTag")
    }

    private fun deleteSampahMasuk() {
        if (detailRitasiId == 0L) {
            Toast.makeText(requireContext(), "Gagal menghapus: ID Ritasi tidak valid.", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val ritasi = sampahViewModel.getRitasiById(detailRitasiId).firstOrNull()
            ritasi?.let {

                sampahViewModel.deleteLokasiSampahById(it.idLokasi)
                Toast.makeText(requireContext(), "Data berhasil dihapus.", Toast.LENGTH_SHORT).show()

                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DaftarSampahMasukFragment())
                    .commit()
            } ?: run {
                Toast.makeText(requireContext(), "Data tidak ditemukan atau sudah dihapus.", Toast.LENGTH_SHORT).show()
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            val ritasi = sampahViewModel.getRitasiById(detailRitasiId).firstOrNull()
            ritasi?.let {
                sampahViewModel.deleteLokasiSampahById(it.idLokasi)
                Toast.makeText(requireContext(), "Data berhasil dihapus.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } ?: run {
                Toast.makeText(requireContext(), "Data tidak ditemukan atau sudah dihapus.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}