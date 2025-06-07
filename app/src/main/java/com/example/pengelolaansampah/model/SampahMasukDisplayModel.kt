package com.example.pengelolaansampah.model

data class SampahMasukDisplayModel(
    val kodeSuratJalan: String,
    val nomorKendaraan: String,
    val volumeSampah: Double,
    val externalRitasiTpaId: String,
    val idRitasiInternal: Long,
    val tanggalRitasi: String,
    val lokasiSumberSampah: String
)