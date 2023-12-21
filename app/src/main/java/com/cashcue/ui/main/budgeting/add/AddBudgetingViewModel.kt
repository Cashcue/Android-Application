package com.cashcue.ui.main.budgeting.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.data.local.pref.user.UserRepository
import com.cashcue.data.local.room.CashcueRepository
import com.cashcue.data.local.room.entity.Anggaran
import kotlinx.coroutines.launch

class AddBudgetingViewModel(
    private val userRepository: UserRepository,
    private val cashcueRepository: CashcueRepository
): ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun insert(anggaran: Anggaran) {
        viewModelScope.launch {
            cashcueRepository.insertAnggaran(anggaran)
        }
    }

}