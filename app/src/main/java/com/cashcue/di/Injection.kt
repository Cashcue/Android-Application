package com.cashcue.di

import android.content.Context
import com.cashcue.data.local.pref.user.UserPreference
import com.cashcue.data.local.pref.user.UserRepository
import com.cashcue.data.local.pref.user.dataStore
import com.cashcue.data.local.room.CashcueRepository

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideCashcueRepository(context: Context): CashcueRepository {
        return CashcueRepository.getInstance(context)
    }

}