package com.cashcue.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.data.local.pref.user.UserRepository
import com.cashcue.data.local.room.CashcueRepository
import com.cashcue.data.local.room.entity.Anggaran
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val cashcueRepository: CashcueRepository
): ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getAnggaranList(): LiveData<List<Anggaran>> {
        return cashcueRepository.getAnggaranList()
    }

}