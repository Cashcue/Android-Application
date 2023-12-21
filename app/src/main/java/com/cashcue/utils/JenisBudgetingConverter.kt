package com.cashcue.utils

import android.content.Context
import com.cashcue.R

object JenisBudgetingConverter {
    
    fun convert(context: Context, jenis: Int): String {
        return if (jenis == 0) {
            context.getString(R.string.pemasukan)
        } else {
            context.getString(R.string.pengeluaran)
        }
    }
    
}