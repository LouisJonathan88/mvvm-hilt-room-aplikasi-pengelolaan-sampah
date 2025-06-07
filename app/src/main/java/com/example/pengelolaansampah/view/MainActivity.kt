// hari tanggal pengerjaan : 6 Juni 2025
// nim                     : 10122362
// nama lengkap            : Louis Jonathan Susanto Putra
// kelas                   : PA 4

package com.example.pengelolaansampah.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DaftarSampahMasukFragment())
            .commit()
    }
}