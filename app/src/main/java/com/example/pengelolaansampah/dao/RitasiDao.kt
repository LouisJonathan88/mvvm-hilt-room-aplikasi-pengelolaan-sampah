package com.example.pengelolaansampah.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pengelolaansampah.model.FullSampahDetailModel
import com.example.pengelolaansampah.model.RitasiModel
import com.example.pengelolaansampah.model.SampahMasukDisplayModel
import kotlinx.coroutines.flow.Flow


@Dao
interface RitasiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRitasi(ritasi: RitasiModel): Long

    @Update
    suspend fun updateRitasi(ritasi: RitasiModel)

    @Query("SELECT * FROM ritasi WHERE idRitasi = :idRitasi")
    fun getRitasiById(idRitasi: Long): Flow<RitasiModel?>

    @Query("SELECT * FROM ritasi WHERE idLokasi = :idLokasi ORDER BY tanggalRitasi DESC, idRitasi DESC")
    fun getRitasiByLokasiId(idLokasi: Long): Flow<List<RitasiModel>>

    @Query("SELECT * FROM ritasi ORDER BY idRitasi DESC")
    fun getAllRitasi(): Flow<List<RitasiModel>>

    @Query("DELETE FROM ritasi WHERE idRitasi = :idRitasi")
    suspend fun deleteRitasiById(idRitasi: Long)

    @Query("""
        SELECT
            ls.kodeSuratJalan,
            kdk.nomorKendaraan,
            r.volumeSampah,
            r.externalRitasiTpaId,
            r.idRitasi AS idRitasiInternal, 
            r.tanggalRitasi,
            ls.lokasiSumberSampah
        FROM lokasi_sampah AS ls
        INNER JOIN ritasi AS r ON ls.idLokasi = r.idLokasi
        INNER JOIN surat_tugas AS kdk ON r.idRitasi = kdk.idRitasi
        ORDER BY r.tanggalRitasi DESC, r.idRitasi DESC
    """)
    fun getAllSampahMasukDisplay(): Flow<List<SampahMasukDisplayModel>>

    @Query("""
    SELECT
        ls.idLokasi, ls.kodeSuratJalan, ls.lokasiSumberSampah, ls.alamatLokasi, ls.jenisPengirim AS jenisPengirimLokasi, ls.statusKeterangan, ls.volumeTerangkut,
        r.idRitasi, r.externalRitasiTpaId,r.tanggalRitasi, r.volumeSampah, r.jamMasuk, r.jamKeluar, r.petugasPencatat, r.bruto, r.tarra, r.netto,
        kdk.idKendaraanKru, kdk.idSuratTugas, kdk.nomorKendaraan, kdk.jenisKendaraan, kdk.namaPengemudi, kdk.crew1, kdk.crew2, kdk.crew3, kdk.crew4, kdk.crew5
    FROM lokasi_sampah AS ls
    INNER JOIN ritasi AS r ON ls.idLokasi = r.idLokasi
    INNER JOIN surat_tugas AS kdk ON r.idRitasi = kdk.idRitasi
    WHERE r.idRitasi = :ritasiId
    LIMIT 1
""")
    fun getFullSampahDetailById(ritasiId: Long): Flow<FullSampahDetailModel?>
}