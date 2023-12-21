package com.cashcue.ui.main.budgeting.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashcue.data.local.room.CashcueRepository
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.data.local.room.entity.RiwayatKeuangan
import kotlinx.coroutines.launch

class InputBudgetingViewModel(private val cashcueRepository: CashcueRepository): ViewModel() {

    fun getAnggaranById(idAnggaran: Int): LiveData<Anggaran> {
        return cashcueRepository.getAnggaranById(idAnggaran)
    }

    fun inputSaldoAnggaran(anggaran: Anggaran, saldoAnggaran: Int, tanggal: Long, saldoRiwayatKeuangan: Int) {
        viewModelScope.launch {
            cashcueRepository.inputSaldoAnggaran(anggaran.id, saldoAnggaran)
        }

        viewModelScope.launch {
            cashcueRepository.insertRiwayatKeuangan(
                RiwayatKeuangan(
                    idUser = anggaran.idUser,
                    idAnggaran = anggaran.id,
                    tanggal = tanggal,
                    saldo = saldoRiwayatKeuangan
                )
            )
        }
    }

}