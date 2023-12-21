package com.cashcue.ui.main.edit

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cashcue.R
import com.cashcue.data.Result
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.data.local.pref.user.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class EditProfileViewModel(private val userRepository: UserRepository, private val context: Context): ViewModel() {

    private var _result = MutableLiveData<Result<String>>()
    private val result: LiveData<Result<String>> = _result

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun send(nama: String, email: String, saldo: Int): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        val user = UserModel (
            0,
            email,
            nama,
            "https://gusnanto.net/assets/img/avatar.png",
            saldo,
        )

        saveSession(user)
        delay(3000L)
        _result.value = Result.Success(context.getString(R.string.save_successfully))
        emitSource(result)
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

}