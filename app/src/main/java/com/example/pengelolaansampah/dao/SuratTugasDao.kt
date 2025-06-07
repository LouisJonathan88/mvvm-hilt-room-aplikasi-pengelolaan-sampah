package com.example.pengelolaansampah.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pengelolaansampah.model.SuratTugasModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SuratTugasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKendaraanDanKru(kendaraanDanKru: SuratTugasModel): Long

    @Update
    suspend fun updateKendaraanDanKru(kendaraanDanKru: SuratTugasModel)

    @Query("SELECT * FROM surat_tugas WHERE idKendaraanKru = :idKendaraanKru")
    fun getKendaraanDanKruById(idKendaraanKru: Long): Flow<SuratTugasModel?>

    @Query("SELECT * FROM surat_tugas WHERE idRitasi = :idRitasi")
    fun getKendaraanDanKruByRitasiId(idRitasi: Long): Flow<SuratTugasModel?>

    @Query("SELECT * FROM surat_tugas ORDER BY idKendaraanKru DESC")
    fun getAllKendaraanDanKru(): Flow<List<SuratTugasModel>>

    @Query("DELETE FROM surat_tugas WHERE idKendaraanKru = :idKendaraanKru")
    suspend fun deleteKendaraanDanKruById(idKendaraanKru: Long)
}