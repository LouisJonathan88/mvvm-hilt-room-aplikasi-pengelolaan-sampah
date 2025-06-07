package com.example.pengelolaansampah.viewmodel // Sesuaikan dengan package Anda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pengelolaansampah.model.FullSampahDetailModel
import com.example.pengelolaansampah.model.LokasiSampahModel
import com.example.pengelolaansampah.model.RitasiModel
import com.example.pengelolaansampah.model.SampahMasukDisplayModel
import com.example.pengelolaansampah.model.SuratTugasModel
import com.example.pengelolaansampah.repository.SampahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampahViewModel @Inject constructor(
    private val repository: SampahRepository
) : ViewModel() {

    var currentLokasiId: Long = 0
    var currentRitasiId: Long = 0

    fun insertLokasiSampah(lokasiSampah: LokasiSampahModel) {
        viewModelScope.launch {
            currentLokasiId = repository.insertLokasiSampah(lokasiSampah)
        }
    }

    fun updateLokasiSampah(lokasiSampah: LokasiSampahModel) {
        viewModelScope.launch {
            repository.updateLokasiSampah(lokasiSampah)
        }
    }

    fun getLokasiSampahById(idLokasi: Long): Flow<LokasiSampahModel?> {
        return repository.getLokasiSampahById(idLokasi)
    }

    fun getAllLokasiSampah(): Flow<List<LokasiSampahModel>> {
        return repository.getAllLokasiSampah()
    }

    fun deleteLokasiSampahById(idLokasi: Long) {
        viewModelScope.launch {
            repository.deleteLokasiSampahById(idLokasi)
        }
    }

    fun insertRitasi(ritasi: RitasiModel) {
        viewModelScope.launch {
            currentRitasiId = repository.insertRitasi(ritasi)
        }
    }

    fun updateRitasi(ritasi: RitasiModel) {
        viewModelScope.launch {
            repository.updateRitasi(ritasi)
        }
    }

    fun getRitasiById(idRitasi: Long): Flow<RitasiModel?> {
        return repository.getRitasiById(idRitasi)
    }

    fun getRitasiByLokasiId(idLokasi: Long): Flow<List<RitasiModel>> {
        return repository.getRitasiByLokasiId(idLokasi)
    }

    fun getAllRitasi(): Flow<List<RitasiModel>> {
        return repository.getAllRitasi()
    }

    fun deleteRitasiById(idRitasi: Long) {
        viewModelScope.launch {
            repository.deleteRitasiById(idRitasi)
        }
    }

    fun insertKendaraanDanKru(kendaraanDanKru: SuratTugasModel) {
        viewModelScope.launch {
            repository.insertKendaraanDanKru(kendaraanDanKru)
        }
    }

    fun updateKendaraanDanKru(kendaraanDanKru: SuratTugasModel) {
        viewModelScope.launch {
            repository.updateKendaraanDanKru(kendaraanDanKru)
        }
    }

    fun getKendaraanDanKruById(idKendaraanKru: Long): Flow<SuratTugasModel?> {
        return repository.getKendaraanDanKruById(idKendaraanKru)
    }

    fun getKendaraanDanKruByRitasiId(idRitasi: Long): Flow<SuratTugasModel?> {
        return repository.getKendaraanDanKruByRitasiId(idRitasi)
    }

    fun getAllKendaraanDanKru(): Flow<List<SuratTugasModel>> {
        return repository.getAllKendaraanDanKru()
    }

    fun deleteKendaraanDanKruById(idKendaraanKru: Long) {
        viewModelScope.launch {
            repository.deleteKendaraanDanKruById(idKendaraanKru)
        }
    }
    fun getFullSampahDetailById(ritasiId: Long): Flow<FullSampahDetailModel?> {
        return repository.getFullSampahDetailById(ritasiId)
    }

    val allSampahMasukDisplay: Flow<List<SampahMasukDisplayModel>> =
        repository.getAllSampahMasukDisplay()
}