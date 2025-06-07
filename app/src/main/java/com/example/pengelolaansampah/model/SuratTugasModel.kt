package com.example.pengelolaansampah.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "surat_tugas",
    foreignKeys = [ForeignKey(
        entity = RitasiModel::class,
        parentColumns = ["idRitasi"],
        childColumns = ["idRitasi"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SuratTugasModel(
    @PrimaryKey(autoGenerate = true)
    val idKendaraanKru: Long = 0,
    val idRitasi: Long,
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