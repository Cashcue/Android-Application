package com.cashcue.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cashcue.data.local.room.entity.Anggaran

@Dao
interface AnggaranDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg anggaran: Anggaran)

    @Query("SELECT * FROM anggaran ORDER BY nama ASC")
    fun getAnggaran(): DataSource.Factory<Int, Anggaran>

    @Query("SELECT * FROM anggaran")
    fun getAnggaranList(): LiveData<List<Anggaran>>

    @Query("SELECT * FROM anggaran WHERE idAnggaran = :idAnggaran")
    fun getAnggaranById(idAnggaran: Int): LiveData<Anggaran>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anggaran: Anggaran)

    @Query("UPDATE anggaran SET saldo = :saldo WHERE idAnggaran = :idAnggaran")
    suspend fun inputSaldo(idAnggaran: Int, saldo: Int)

    @Query("UPDATE anggaran SET saldo = 0 WHERE idAnggaran = :idAnggaran")
    suspend fun resetSaldo(idAnggaran: Int)

    @Update
    suspend fun update(anggaran: Anggaran)

    @Delete
    suspend fun delete(anggaran: Anggaran)
}