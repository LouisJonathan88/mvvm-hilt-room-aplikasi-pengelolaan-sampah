package com.example.pengelolaansampah.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pengelolaansampah.dao.LokasiSampahDao
import com.example.pengelolaansampah.dao.RitasiDao
import com.example.pengelolaansampah.dao.SuratTugasDao
import com.example.pengelolaansampah.model.LokasiSampahModel
import com.example.pengelolaansampah.model.RitasiModel
import com.example.pengelolaansampah.model.SuratTugasModel


@Database(
    entities = [LokasiSampahModel::class, RitasiModel::class, SuratTugasModel::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lokasiSampahDao(): LokasiSampahDao
    abstract fun ritasiDao(): RitasiDao
    abstract fun suratTugasDao(): SuratTugasDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sampah_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}