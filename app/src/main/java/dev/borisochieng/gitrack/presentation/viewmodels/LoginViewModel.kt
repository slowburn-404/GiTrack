package dev.borisochieng.gitrack.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.data.GithubAuthRepository
import dev.borisochieng.gitrack.data.models.AccessTokenResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val authRepository: GithubAuthRepository
) : ViewModel() {
    private val _accessToken = MutableLiveData<AccessTokenResponse?>()
    val accessToken = _accessToken


    fun getAccessToken(clientId: String, clientSecret: String, code: String?) =
        code?.let {
            viewModelScope.launch {
                val call = authRepository.getAccessToken(clientId, clientSecret, it)

                call.enqueue(object : Callback<AccessTokenResponse> {
                    override fun onResponse(
                        call: Call<AccessTokenResponse>,
                        response: Response<AccessTokenResponse>
                    ) {
                        if (response.isSuccessful) {
                            _accessToken.value = response.body()
                        }
                    }

                    override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                        t.printStackTrace()
                        _accessToken.value = null
                    }

                })
            }
        }

        }

    class LoginViewModelFactory(private val authRepository: GithubAuthRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(authRepository) as T

            }

            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }