package com.cashcue.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.cashcue.data.local.room.entity.RiwayatKeuangan
import com.cashcue.data.local.room.entity.RiwayatKeuanganDanAnggaran

@Dao
interface RiwayatKeuanganDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg riwayatKeuangan: RiwayatKeuangan)

    @RawQuery(observedEntities = [RiwayatKeuangan::class])
    fun getRiwayatKeuangan(query: SupportSQLiteQuery): DataSource.Factory<Int, RiwayatKeuanganDanAnggaran>

    @Query("SELECT * FROM riwayat_keuangan WHERE tanggal BETWEEN :ago AND :current")
    fun getCurrentRiwayatKeuangan(ago: Long, current: Long): LiveData<List<RiwayatKeuanganDanAnggaran>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(riwayatKeuangan: RiwayatKeuangan)
}