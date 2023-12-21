package com.cashcue.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RiwayatKeuanganDanAnggaran (
    @Embedded
    val riwayatKeuangan: RiwayatKeuangan,

    @Relation(
        parentColumn = "idAngrn",
        entityColumn = "idAnggaran"
    )
    val anggaran: Anggaran? = null
)