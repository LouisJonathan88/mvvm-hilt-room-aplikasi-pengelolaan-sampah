package com.example.pengelolaansampah.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pengelolaansampah.R
import com.example.pengelolaansampah.databinding.FragmentDaftarSampahMasukBinding
import com.example.pengelolaansampah.view.adapter.SampahMasukAdapter
import com.example.pengelolaansampah.viewmodel.SampahViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DaftarSampahMasukFragment : Fragment(), SampahMasukAdapter.SampahItemClickListener {

    private var _binding: FragmentDaftarSampahMasukBinding? = null
    private val binding get() = _binding!!

    private val sampahViewModel: SampahViewModel by activityViewModels()

    private lateinit var sampahMasukAdapter: SampahMasukAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarSampahMasukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sampahMasukAdapter = SampahMasukAdapter(this)
        binding.rvSampahMasuk.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sampahMasukAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sampahViewModel.allSampahMasukDisplay.collectLatest { sampahList ->
                    sampahMasukAdapter.submitList(sampahList)
                    binding.tvTotalKendaraan.text = "${sampahList.size} kendaraan"
                }
            }
        }

        binding.btnTambah.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TambahDataLokasiFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onLihatDetailClick(idRitasi: Long) {
        val bundle = Bundle().apply {
            putLong("ritasiId", idRitasi)
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailSampahMasukFragment().apply { arguments = bundle })
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}