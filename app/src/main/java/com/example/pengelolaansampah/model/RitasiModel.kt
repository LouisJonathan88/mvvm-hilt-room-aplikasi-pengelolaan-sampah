package com.example.pengelolaansampah.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ritasi",
    foreignKeys = [ForeignKey(
        entity = LokasiSampahModel::class,
        parentColumns = ["idLokasi"],
        childColumns = ["idLokasi"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class RitasiModel(
    @PrimaryKey(autoGenerate = true)
    val idRitasi: Long = 0,
    val idLokasi: Long,
    val externalRitasiTpaId: String,
    val tanggalRitasi: String,
    val volumeSampah: Double,
    val jamMasuk: String,
    val jamKeluar: String,
    val petugasPencatat: String,
    val bruto: Double,
    val tarra: Double,
    val netto: Double
)