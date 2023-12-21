package com.cashcue.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cashcue.data.Result
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.data.local.pref.user.UserRepository
import com.cashcue.data.remote.response.RegisterResponse
import com.cashcue.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {

    private var _result = MutableLiveData<Result<String>>()
    private val result: LiveData<Result<String>> = _result

    fun login(email: String, password: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = ApiConfig.getApiService().login(email, password)
            _result.value = Result.Success(response.message.toString())

            val user = UserModel(
                0,
                email,
                response.loginResult?.name.toString(),
                "https://gusnanto.net/assets/img/avatar.png",
                3000000,
            )
            saveSession(user)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            emit(Result.Error(errorBody.message.toString()))
        }

        emitSource(result)
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

}