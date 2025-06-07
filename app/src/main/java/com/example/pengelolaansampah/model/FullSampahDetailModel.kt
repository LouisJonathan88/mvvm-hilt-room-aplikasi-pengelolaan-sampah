package com.example.pengelolaansampah.model


data class FullSampahDetailModel(

    val idLokasi: Long,
    val kodeSuratJalan: String,
    val lokasiSumberSampah: String,
    val alamatLokasi: String,
    val jenisPengirimLokasi: String,
    val statusKeterangan: String,
    val volumeTerangkut: Double,

    val idRitasi: Long,
    val externalRitasiTpaId: String,
    val tanggalRitasi: String,
    val volumeSampah: Double,
    val jamMasuk: String,
    val jamKeluar: String,
    val petugasPencatat: String,
    val bruto: Double,
    val tarra: Double,
    val netto: Double,

    val idKendaraanKru: Long,
    val idSuratTugas: String,
    val nomorKendaraan: String,
    val jenisKendaraan: String,
    val namaPengemudi: String,
    val crew1: String,
    val crew2: String,
    val crew3: String,
    val crew4: String,
    val crew5: String
)