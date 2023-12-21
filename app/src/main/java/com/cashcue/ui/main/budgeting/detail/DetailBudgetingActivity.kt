package com.cashcue.ui.main.budgeting.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cashcue.R
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.databinding.ActivityDetailBudgetingBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.main.budgeting.input.InputBudgetingActivity
import com.cashcue.utils.JenisBudgetingConverter
import com.cashcue.utils.RupiahConverter

class DetailBudgetingActivity : AppCompatActivity() {

    private val detailBudgetingViewModel by viewModels<DetailBudgetingViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailBudgetingBinding
    private lateinit var anggaran: Anggaran

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBudgetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.detail_anggaran)

        val idAnggaran = intent.getIntExtra(EXTRA_ID, 0)
        detailBudgetingViewModel.getAnggaranById(idAnggaran).observe(this) {
            if (it != null) {
                anggaran = it
                showDetail(it)
            }
        }

        binding.btnDetailInput.setOnClickListener {
            val inputIntent = Intent(this, InputBudgetingActivity::class.java)
            inputIntent.putExtra(InputBudgetingActivity.EXTRA_ID, idAnggaran)
            startActivity(inputIntent)
        }

        binding.btnDetailEdit.setOnClickListener {
            editMode(true)
        }

        binding.btnDetailSave.setOnClickListener {
            update()
        }

        binding.tvResetSaldo.setOnClickListener {
            resetSaldo()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_anggaran_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.delete))
                    setMessage(getString(R.string.are_u_sure))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        detailBudgetingViewModel.deleteAnggaran(anggaran)
                        finish()
                    }
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    setCancelable(false)
                    create()
                    show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDetail(anggaran: Anggaran) {
        if (anggaran.jenis == 0) {
            binding.rbDetailPemasukan.isChecked = true
            binding.rbDetailPengeluaran.isChecked = false
        } else {
            binding.rbDetailPemasukan.isChecked = false
            binding.rbDetailPengeluaran.isChecked = true
        }
        binding.tvDetailJenis.text = JenisBudgetingConverter.convert(this, anggaran.jenis)

        binding.tvDetailNama.text = anggaran.nama
        binding.edDetailNama.setText(anggaran.nama)

        binding.tvDetailSaldo.text = RupiahConverter.compress(anggaran.saldo)
        binding.tvDetailTarget.text = RupiahConverter.compress(anggaran.saldoTarget)
        binding.edDetailSaldoTarget.setText(anggaran.saldoTarget.toString())

        binding.tvResetSaldo.isVisible = anggaran.saldo != 0
    }

    private fun editMode(isEdit: Boolean) {
        binding.tvDetailJenis.isVisible = !isEdit
        binding.tvDetailLabelNama.isVisible = !isEdit
        binding.tvDetailNama.isVisible = !isEdit
        binding.tvDetailLabelSaldoTarget.isVisible = !isEdit
        binding.tvDetailSaldo.isVisible = !isEdit
        binding.tvPer.isVisible = !isEdit
        binding.tvDetailTarget.isVisible = !isEdit
        binding.tvResetSaldo.isVisible = !isEdit
        binding.btnDetailInput.isVisible = !isEdit
        binding.btnDetailEdit.isVisible = !isEdit

        binding.rgDetailJenis.isVisible = isEdit
        binding.edlDetailNama.isVisible = isEdit
        binding.edlDetailSaldoTarget.isVisible = isEdit
        binding.btnDetailSave.isVisible = isEdit
    }

    private fun update() {
        var jenis = -1
        when (binding.rgDetailJenis.checkedRadioButtonId) {
            R.id.rb_detail_pemasukan -> {
                jenis = 0
            }
            R.id.rb_detail_pengeluaran -> {
                jenis = 1
            }
        }
        val nama = binding.edDetailNama.text.toString()
        val saldoTarget = binding.edDetailSaldoTarget.text.toString()

        when {
            jenis == -1 -> showToast(getString(R.string.jenis_harus_dipilih))
            nama.isEmpty() -> showToast(getString(R.string.nama_harus_diisi))
            saldoTarget.isEmpty() -> showToast(getString(R.string.saldo_target_harus_diisi))
            saldoTarget.toInt() < 1000 -> showToast(getString(R.string.saldo_target_tidak_boleh_kurang_dari_rp1000))
            else -> {
                detailBudgetingViewModel.updateAnggaran(
                    Anggaran(
                        anggaran.id,
                        anggaran.idUser,
                        jenis,
                        nama,
                        anggaran.saldo,
                        saldoTarget.toInt()
                    )
                )
                showToast(getString(R.string.anggaran_berhasil_diupdate))
                editMode(false)
            }
        }
    }

    private fun resetSaldo() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.reset_saldo))
            setMessage(getString(R.string.are_u_sure))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                detailBudgetingViewModel.resetSaldo(anggaran.id)
                showToast(getString(R.string.saldo_berhasil_direset))
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}