package com.example.pengelolaansampah.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lokasi_sampah")
data class LokasiSampahModel(
    @PrimaryKey(autoGenerate = true)
    val idLokasi: Long = 0,
    val kodeSuratJalan: String,
    val lokasiSumberSampah: String,
    val alamatLokasi: String,
    val jenisPengirim: String,
    val statusKeterangan: String,
    val volumeTerangkut: Double
)
