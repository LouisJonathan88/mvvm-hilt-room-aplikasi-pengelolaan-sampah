package com.example.pengelolaansampah.di

import android.content.Context
import androidx.room.Room
import com.example.pengelolaansampah.dao.LokasiSampahDao
import com.example.pengelolaansampah.dao.RitasiDao
import com.example.pengelolaansampah.dao.SuratTugasDao
import com.example.pengelolaansampah.database.AppDatabase
import com.example.pengelolaansampah.repository.SampahRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sampah_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideLokasiSampahDao(database: AppDatabase): LokasiSampahDao = database.lokasiSampahDao()

    @Provides
    @Singleton
    fun provideRitasiDao(database: AppDatabase): RitasiDao = database.ritasiDao()

    @Provides
    @Singleton
    fun provideKendaraanDanKruDao(database: AppDatabase): SuratTugasDao = database.suratTugasDao()

    @Provides
    @Singleton
    fun provideRepository(
        lokasiSampahDao: LokasiSampahDao,
        ritasiDao: RitasiDao,
        kendaraanDanKruDao: SuratTugasDao
    ): SampahRepository = SampahRepository(lokasiSampahDao, ritasiDao, kendaraanDanKruDao)

}