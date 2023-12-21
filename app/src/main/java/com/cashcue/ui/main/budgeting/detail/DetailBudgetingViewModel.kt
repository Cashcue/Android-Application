package com.cashcue.ui.main.budgeting.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashcue.data.local.room.CashcueRepository
import com.cashcue.data.local.room.entity.Anggaran
import kotlinx.coroutines.launch

class DetailBudgetingViewModel(private val cashcueRepository: CashcueRepository): ViewModel() {

    fun getAnggaranById(idAnggaran: Int): LiveData<Anggaran> {
        return cashcueRepository.getAnggaranById(idAnggaran)
    }

    fun updateAnggaran(anggaran: Anggaran) {
        viewModelScope.launch {
            cashcueRepository.updateAnggaran(anggaran)
        }
    }

    fun resetSaldo(idAnggaran: Int) {
        viewModelScope.launch {
            cashcueRepository.resetSaldoAnggaran(idAnggaran)
        }
    }

    fun deleteAnggaran(anggaran: Anggaran) {
        viewModelScope.launch {
            cashcueRepository.deleteAnggaran(anggaran)
        }
    }

}