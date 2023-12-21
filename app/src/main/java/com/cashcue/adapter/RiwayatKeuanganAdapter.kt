package com.cashcue.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashcue.R
import com.cashcue.data.local.room.entity.RiwayatKeuanganDanAnggaran
import com.cashcue.databinding.ItemHistoryBinding
import com.cashcue.utils.DateConverter
import com.cashcue.utils.RupiahConverter

class RiwayatKeuanganAdapter(private val context: Context): PagedListAdapter<RiwayatKeuanganDanAnggaran, RiwayatKeuanganAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatKeuanganAdapter.ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RiwayatKeuanganAdapter.ViewHolder, position: Int) {
        val riwayatKeuanganDanAnggaran = getItem(position) as RiwayatKeuanganDanAnggaran

        holder.bind(riwayatKeuanganDanAnggaran)
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(riwayatKeuanganDanAnggaran: RiwayatKeuanganDanAnggaran) {
            val marker: String
            if (riwayatKeuanganDanAnggaran.anggaran?.jenis == 0) {
                marker = "+ "
                binding.tvItemSaldoHistory.setTextColor(ContextCompat.getColor(context, R.color.plus))
            } else {
                marker = "- "
                binding.tvItemSaldoHistory.setTextColor(ContextCompat.getColor(context, R.color.minus))
            }

            binding.tvItemDateHistory.text = DateConverter.convertMillisToString(riwayatKeuanganDanAnggaran.riwayatKeuangan.tanggal)
            binding.tvItemNameHistory.text = riwayatKeuanganDanAnggaran.anggaran?.nama
            binding.tvItemSaldoHistory.text = marker + RupiahConverter.convert(riwayatKeuanganDanAnggaran.riwayatKeuangan.saldo)
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RiwayatKeuanganDanAnggaran>() {
            override fun areItemsTheSame(oldItem: RiwayatKeuanganDanAnggaran, newItem: RiwayatKeuanganDanAnggaran): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: RiwayatKeuanganDanAnggaran, newItem: RiwayatKeuanganDanAnggaran): Boolean {
                return oldItem.riwayatKeuangan.id == newItem.riwayatKeuangan.id
            }
        }

    }

}