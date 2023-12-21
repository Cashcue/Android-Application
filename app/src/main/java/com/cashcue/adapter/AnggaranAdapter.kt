package com.cashcue.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashcue.R
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.databinding.ItemBudgetingBinding
import com.cashcue.ui.main.budgeting.detail.DetailBudgetingActivity
import com.cashcue.utils.JenisBudgetingConverter
import com.cashcue.utils.RupiahConverter

class AnggaranAdapter(private val context: Context): PagedListAdapter<Anggaran, AnggaranAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBudgetingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anggaran = getItem(position) as Anggaran

        holder.bind(anggaran)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailBudgetingActivity::class.java)
            intentDetail.putExtra(DetailBudgetingActivity.EXTRA_ID, anggaran.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    inner class ViewHolder(private val binding: ItemBudgetingBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(anggaran: Anggaran) {
            binding.tvBudgetingName.text = anggaran.nama
            binding.tvBudgetingJenis.text = JenisBudgetingConverter.convert(context, anggaran.jenis)
            binding.tvCurrentSaldo.text = RupiahConverter.compress(anggaran.saldo)
            binding.tvTarget.text = RupiahConverter.compress(anggaran.saldoTarget)

            val progress = anggaran.saldo.toFloat() / anggaran.saldoTarget.toFloat() * 100
            binding.progressBar.progress = progress.toInt()
            @SuppressLint("SetTextI18n")
            binding.tvProgress.text = progress.toInt().toString()+"%"
            if (progress.toInt() > 100) {
                binding.progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.custom_progress_bar_warning)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Anggaran>() {
            override fun areItemsTheSame(oldItem: Anggaran, newItem: Anggaran): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Anggaran, newItem: Anggaran): Boolean {
                return oldItem == newItem
            }
        }

    }

}