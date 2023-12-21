package com.cashcue.ui.main.budgeting.add

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cashcue.R
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.databinding.ActivityAddBudgetingBinding
import com.cashcue.ui.ViewModelFactory

class AddBudgetingActivity : AppCompatActivity() {

    private val addBudgetingViewModel by viewModels<AddBudgetingViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityAddBudgetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBudgetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_anggaran)

        binding.btnDetailSave.setOnClickListener {
            var jenis = -1
            when (binding.rgAddJenis.checkedRadioButtonId) {
                R.id.rb_add_pemasukan -> {
                    jenis = 0
                }
                R.id.rb_add_pengeluaran -> {
                    jenis = 1
                }
            }
            val nama = binding.edAddNama.text.toString()
            val saldoTarget = binding.edAddSaldoTarget.text.toString()

            var idUser = 0
            addBudgetingViewModel.getSession().observe(this) {
                idUser = it.id
            }

            when {
                jenis == -1 -> showToast(getString(R.string.jenis_harus_dipilih))
                nama.isEmpty() -> showToast(getString(R.string.nama_harus_diisi))
                saldoTarget.isEmpty() -> showToast(getString(R.string.saldo_target_harus_diisi))
                saldoTarget.toInt() < 1000 -> showToast(getString(R.string.saldo_target_tidak_boleh_kurang_dari_rp1000))
                else -> {
                    val anggaran = Anggaran(
                        idUser = idUser,
                        jenis = jenis,
                        nama = nama,
                        saldoTarget = saldoTarget.toInt()
                    )

                    addBudgetingViewModel.insert(anggaran)
                    showToast(getString(R.string.anggaran_berhasil_dibuat))
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}