package com.example.pengelolaansampah.repository


import com.example.pengelolaansampah.dao.LokasiSampahDao
import com.example.pengelolaansampah.dao.RitasiDao
import com.example.pengelolaansampah.dao.SuratTugasDao
import com.example.pengelolaansampah.model.FullSampahDetailModel
import com.example.pengelolaansampah.model.LokasiSampahModel
import com.example.pengelolaansampah.model.RitasiModel
import com.example.pengelolaansampah.model.SampahMasukDisplayModel
import com.example.pengelolaansampah.model.SuratTugasModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampahRepository @Inject constructor(
    private val lokasiSampahDao: LokasiSampahDao,
    private val ritasiDao: RitasiDao,
    private val kendaraanDanKruDao: SuratTugasDao
) {

    suspend fun insertLokasiSampah(lokasiSampah: LokasiSampahModel): Long {
        return lokasiSampahDao.insertLokasiSampah(lokasiSampah)
    }

    suspend fun updateLokasiSampah(lokasiSampah: LokasiSampahModel) {
        lokasiSampahDao.updateLokasiSampah(lokasiSampah)
    }

    fun getLokasiSampahById(idLokasi: Long): Flow<LokasiSampahModel?> {
        return lokasiSampahDao.getLokasiSampahById(idLokasi)
    }

    fun getAllLokasiSampah(): Flow<List<LokasiSampahModel>> {
        return lokasiSampahDao.getAllLokasiSampah()
    }

    suspend fun deleteLokasiSampahById(idLokasi: Long) {
        lokasiSampahDao.deleteLokasiSampahById(idLokasi)
    }

    suspend fun insertRitasi(ritasi: RitasiModel): Long {
        return ritasiDao.insertRitasi(ritasi)
    }

    suspend fun updateRitasi(ritasi: RitasiModel) {
        ritasiDao.updateRitasi(ritasi)
    }

    fun getRitasiById(idRitasi: Long): Flow<RitasiModel?> {
        return ritasiDao.getRitasiById(idRitasi)
    }

    fun getRitasiByLokasiId(idLokasi: Long): Flow<List<RitasiModel>> {
        return ritasiDao.getRitasiByLokasiId(idLokasi)
    }

    fun getAllRitasi(): Flow<List<RitasiModel>> {
        return ritasiDao.getAllRitasi()
    }

    suspend fun deleteRitasiById(idRitasi: Long) {
        ritasiDao.deleteRitasiById(idRitasi)
    }

    suspend fun insertKendaraanDanKru(kendaraanDanKru: SuratTugasModel): Long {
        return kendaraanDanKruDao.insertKendaraanDanKru(kendaraanDanKru)
    }

    suspend fun updateKendaraanDanKru(kendaraanDanKru: SuratTugasModel) {
        kendaraanDanKruDao.updateKendaraanDanKru(kendaraanDanKru)
    }

    fun getKendaraanDanKruById(idKendaraanKru: Long): Flow<SuratTugasModel?> {
        return kendaraanDanKruDao.getKendaraanDanKruById(idKendaraanKru)
    }

    fun getKendaraanDanKruByRitasiId(idRitasi: Long): Flow<SuratTugasModel?> {
        return kendaraanDanKruDao.getKendaraanDanKruByRitasiId(idRitasi)
    }

    fun getAllKendaraanDanKru(): Flow<List<SuratTugasModel>> {
        return kendaraanDanKruDao.getAllKendaraanDanKru()
    }

    suspend fun deleteKendaraanDanKruById(idKendaraanKru: Long) {
        kendaraanDanKruDao.deleteKendaraanDanKruById(idKendaraanKru)
    }

    fun getAllSampahMasukDisplay(): Flow<List<SampahMasukDisplayModel>> {
        return ritasiDao.getAllSampahMasukDisplay()
    }

    fun getFullSampahDetailById(ritasiId: Long): Flow<FullSampahDetailModel?> {
        return ritasiDao.getFullSampahDetailById(ritasiId)
    }
}