package com.cashcue.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.cashcue.data.local.room.CashcueRepository
import com.cashcue.data.local.room.entity.Anggaran

class HomeViewModel(private val cashcueRepository: CashcueRepository) : ViewModel() {

    fun getAnggaran(): LiveData<PagedList<Anggaran>> {
        return cashcueRepository.getAnggaran()
    }

}