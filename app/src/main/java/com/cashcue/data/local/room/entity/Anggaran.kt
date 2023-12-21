package com.cashcue.data.local.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("anggaran")
data class Anggaran (
    @PrimaryKey(true)
    @ColumnInfo("idAnggaran")
    val id: Int = 0,

    @ColumnInfo("idUser")
    val idUser: Int,

    @ColumnInfo("jenis")
    val jenis: Int,

    @ColumnInfo("nama")
    val nama: String,

    @ColumnInfo("saldo")
    val saldo: Int = 0,

    @ColumnInfo("saldoTarget")
    val saldoTarget: Int
): Parcelable