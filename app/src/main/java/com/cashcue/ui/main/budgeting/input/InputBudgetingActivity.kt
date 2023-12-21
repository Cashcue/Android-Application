package com.cashcue.ui.main.budgeting.input

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cashcue.R
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.databinding.ActivityInputBudgetingBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.main.scanner.ScannerActivity
import com.cashcue.utils.DateConverter
import com.cashcue.utils.DatePickerFragment
import java.util.Calendar

class InputBudgetingActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private val inputBudgetingViewModel by viewModels<InputBudgetingViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityInputBudgetingBinding
    private lateinit var anggaran: Anggaran
    private var tanggal = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBudgetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val idAnggaran = intent.getIntExtra(EXTRA_ID, 0)
        inputBudgetingViewModel.getAnggaranById(idAnggaran).observe(this) {
            anggaran = it
            title = getString(R.string.input_saldo_anggaran) + " " + anggaran.nama
        }

        binding.tvInputDate.text = DateConverter.convertMillisToString(tanggal)
        binding.ibDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnInputSave.setOnClickListener {
            save()
        }
        binding.btnInputScan.setOnClickListener {
            scanCamera()
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

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        binding.tvInputDate.text = DateConverter.convertMillisToString(calendar.timeInMillis)

        tanggal = calendar.timeInMillis
    }

    private fun showDatePicker() {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    private fun save() {
        val saldo = binding.edInputSaldo.text.toString()

        when {
            saldo.isEmpty() -> showToast(getString(R.string.saldo_harus_diisi))
            saldo.toInt() < 1000 -> showToast(getString(R.string.saldo_target_tidak_boleh_kurang_dari_rp1000))
            else -> {
                inputBudgetingViewModel.inputSaldoAnggaran(anggaran, anggaran.saldo + saldo.toInt(), tanggal, saldo.toInt())
                showToast(getString(R.string.saldo_anggaran_berhasil_diinput))
                finish()
            }
        }
    }

    private fun scanCamera(){
        val intent = Intent(this, ScannerActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}