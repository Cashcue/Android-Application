package com.cashcue.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    "riwayat_keuangan",
    foreignKeys = [
        ForeignKey(
            entity = Anggaran::class,
            parentColumns = arrayOf("idAnggaran"),
            childColumns = arrayOf("idAngrn"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RiwayatKeuangan (
    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("idUser")
    val idUser: Int,

    @ColumnInfo("idAngrn")
    val idAnggaran: Int,

    @ColumnInfo("tanggal")
    val tanggal: Long,

    @ColumnInfo("saldo")
    val saldo: Int
)