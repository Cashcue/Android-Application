package com.cashcue.data.local.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.cashcue.data.local.room.dao.AnggaranDao
import com.cashcue.data.local.room.dao.RiwayatKeuanganDao
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.data.local.room.entity.RiwayatKeuangan
import com.cashcue.data.local.room.entity.RiwayatKeuanganDanAnggaran
import com.cashcue.utils.FilterUtils
import com.cashcue.utils.TasksFilterType

class CashcueRepository(
    private val anggaranDao: AnggaranDao,
    private val riwayatKeuanganDao: RiwayatKeuanganDao
) {
    companion object {
        const val PAGE_SIZE = 10
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: CashcueRepository? = null

        fun getInstance(context: Context): CashcueRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = CashcueDatabase.getInstance(context)
                    instance = CashcueRepository(database.anggaranDao(), database.riwayatKeuanganDao())
                }
                return instance as CashcueRepository
            }

        }
    }

    fun getAnggaran(): LiveData<PagedList<Anggaran>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDERS)
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(anggaranDao.getAnggaran(), config).build()
    }

    fun getAnggaranList(): LiveData<List<Anggaran>> {
        return anggaranDao.getAnggaranList()
    }

    fun getAnggaranById(idAnggaran: Int): LiveData<Anggaran> {
        return anggaranDao.getAnggaranById(idAnggaran)
    }

    suspend fun insertAnggaran(anggaran: Anggaran) {
        anggaranDao.insert(anggaran)
    }

    suspend fun inputSaldoAnggaran(idAnggaran: Int, saldo: Int) {
        anggaranDao.inputSaldo(idAnggaran, saldo)
    }

    suspend fun resetSaldoAnggaran(idAnggaran: Int) {
        anggaranDao.resetSaldo(idAnggaran)
    }

    suspend fun updateAnggaran(anggaran: Anggaran) {
        anggaranDao.update(anggaran)
    }

    suspend fun deleteAnggaran(anggaran: Anggaran) {
        anggaranDao.delete(anggaran)
    }

    fun getRiwayatKeuangan(filter: TasksFilterType): LiveData<PagedList<RiwayatKeuanganDanAnggaran>> {
        val query = FilterUtils.getFilteredQuery(filter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDERS)
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(riwayatKeuanganDao.getRiwayatKeuangan(query), config).build()
    }

    fun getCurrentRiwayatKeuangan(ago: Long, current: Long): LiveData<List<RiwayatKeuanganDanAnggaran>> {
        return riwayatKeuanganDao.getCurrentRiwayatKeuangan(ago, current)
    }

    suspend fun insertRiwayatKeuangan(riwayatKeuangan: RiwayatKeuangan) {
        riwayatKeuanganDao.insert(riwayatKeuangan)
    }
}