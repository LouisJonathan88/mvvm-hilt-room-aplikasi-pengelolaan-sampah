package com.example.pengelolaansampah.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.ItemSampahMasukBinding
import com.example.pengelolaansampah.model.SampahMasukDisplayModel
import java.util.Locale

class SampahMasukAdapter(private val clickListener: SampahItemClickListener) :
    ListAdapter<SampahMasukDisplayModel, SampahMasukAdapter.SampahMasukViewHolder>(DiffCallback) {

    interface SampahItemClickListener {
        fun onLihatDetailClick(idRitasi: Long)

    }

    class SampahMasukViewHolder(private val binding: ItemSampahMasukBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sampah: SampahMasukDisplayModel, clickListener: SampahItemClickListener) {
            binding.tvIdSuratJalanValue.text = "#${sampah.kodeSuratJalan}"
            binding.tvNoKendaraanValue.text = sampah.nomorKendaraan
            binding.tvVolumeValue.text = String.format(Locale("id", "ID"), "%.2f ton", sampah.volumeSampah)
            binding.tvIdRitasiValue.text = "#${sampah.externalRitasiTpaId}"
            binding.tvTanggalValue.text = sampah.tanggalRitasi
            binding.tvSumberSampahValue.text = sampah.lokasiSumberSampah

            binding.btnLihatDetail.setOnClickListener {
                clickListener.onLihatDetailClick(sampah.idRitasiInternal)
            }

            binding.btnMenujuTpa.setOnClickListener {

            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<SampahMasukDisplayModel>() {
        override fun areItemsTheSame(oldItem: SampahMasukDisplayModel, newItem: SampahMasukDisplayModel): Boolean {
            return oldItem.idRitasiInternal == newItem.idRitasiInternal
        }

        override fun areContentsTheSame(oldItem: SampahMasukDisplayModel, newItem: SampahMasukDisplayModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampahMasukViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSampahMasukBinding.inflate(layoutInflater, parent, false)
        return SampahMasukViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SampahMasukViewHolder, position: Int) {
        val sampah = getItem(position)
        holder.bind(sampah, clickListener)
    }
}