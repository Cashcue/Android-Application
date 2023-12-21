package com.cashcue.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cashcue.data.local.pref.user.UserRepository
import com.cashcue.data.local.room.CashcueRepository
import com.cashcue.di.Injection
import com.cashcue.ui.auth.login.LoginViewModel
import com.cashcue.ui.auth.register.RegisterViewModel
import com.cashcue.ui.main.MainViewModel
import com.cashcue.ui.main.budgeting.add.AddBudgetingViewModel
import com.cashcue.ui.main.budgeting.detail.DetailBudgetingViewModel
import com.cashcue.ui.main.budgeting.input.InputBudgetingViewModel
import com.cashcue.ui.main.edit.EditProfileViewModel
import com.cashcue.ui.main.home.HomeViewModel
import com.cashcue.ui.main.settings.SettingsViewModel
import com.cashcue.ui.main.stats.StatsViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val cashcueRepository: CashcueRepository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, cashcueRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(userRepository, context) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(cashcueRepository) as T
            }
            modelClass.isAssignableFrom(AddBudgetingViewModel::class.java) -> {
                AddBudgetingViewModel(userRepository, cashcueRepository) as T
            }
            modelClass.isAssignableFrom(DetailBudgetingViewModel::class.java) -> {
                DetailBudgetingViewModel(cashcueRepository) as T
            }
            modelClass.isAssignableFrom(InputBudgetingViewModel::class.java) -> {
                InputBudgetingViewModel(cashcueRepository) as T
            }
            modelClass.isAssignableFrom(StatsViewModel::class.java) -> {
                StatsViewModel(cashcueRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideCashcueRepository(context),
                        context
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}