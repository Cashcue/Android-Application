package com.cashcue.utils

import java.text.NumberFormat
import java.util.Locale

object RupiahConverter {
    fun convert(number: Int): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number).toString()
    }

    fun compress(number: Int): String {
        var result: Number = number
        var simbol = ""

        if (number in 1000..999999) {
            result = number / 1000
            simbol = "rb"
        } else if (number in 1000000..999999999) {
            result = number.toFloat() / 1000000
            simbol = "jt"
        }

        val resultString = "$result$simbol"
        return resultString.replace(".0jt", "jt")
    }
}