package dev.borisochieng.gitrack.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.repositories.AuthRepository
import dev.borisochieng.gitrack.data.models.AccessTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _accessToken = MutableLiveData<AccessTokenResponse?>()
    val accessToken = _accessToken


    fun getAccessToken(clientId: String, clientSecret: String, code: String?) =
        code?.let {
            viewModelScope.launch {
                try {
                    val response = authRepository.getAccessToken(clientId, clientSecret, it)
                    _accessToken.value = response
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

}

class LoginViewModelFactory(private val authRepository: AuthRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepository) as T

        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}