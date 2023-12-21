package com.cashcue.ui.main.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.cashcue.data.local.room.CashcueRepository
import com.cashcue.data.local.room.entity.RiwayatKeuangan
import com.cashcue.data.local.room.entity.RiwayatKeuanganDanAnggaran
import com.cashcue.utils.TasksFilterType

class StatsViewModel(private val cashcueRepository: CashcueRepository) : ViewModel() {

    private val _filter = MutableLiveData<TasksFilterType>()

    val riwayatKeuangan: LiveData<PagedList<RiwayatKeuanganDanAnggaran>> = _filter.switchMap {
        cashcueRepository.getRiwayatKeuangan(it)
    }

    init {
        _filter.value = TasksFilterType.TERBARU
    }

    fun filter(filterType: TasksFilterType) {
        _filter.value = filterType
    }

    fun getCurrentRiwayatKeuangan(ago: Long, current: Long): LiveData<List<RiwayatKeuanganDanAnggaran>> {
        return cashcueRepository.getCurrentRiwayatKeuangan(ago, current)
    }

}