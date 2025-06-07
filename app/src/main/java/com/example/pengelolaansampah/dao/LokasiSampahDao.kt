package com.example.pengelolaansampah.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pengelolaansampah.model.LokasiSampahModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LokasiSampahDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLokasiSampah(lokasiSampah: LokasiSampahModel): Long

    @Update
    suspend fun updateLokasiSampah(lokasiSampah: LokasiSampahModel)

    @Query("SELECT * FROM lokasi_sampah WHERE idLokasi = :idLokasi")
    fun getLokasiSampahById(idLokasi: Long): Flow<LokasiSampahModel?>

    @Query("SELECT * FROM lokasi_sampah ORDER BY idLokasi DESC")
    fun getAllLokasiSampah(): Flow<List<LokasiSampahModel>>

    @Query("DELETE FROM lokasi_sampah WHERE idLokasi = :idLokasi")
    suspend fun deleteLokasiSampahById(idLokasi: Long)
}
