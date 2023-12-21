package com.cashcue.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {

    fun getFilteredQuery(filter: TasksFilterType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM riwayat_keuangan ")
        when (filter) {
            TasksFilterType.TERBARU -> {
                simpleQuery.append("ORDER BY tanggal DESC")
            }
            TasksFilterType.TERLAMA -> {
                simpleQuery.append("ORDER BY tanggal ASC")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

}