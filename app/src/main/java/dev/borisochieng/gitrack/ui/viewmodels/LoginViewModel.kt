package dev.borisochieng.gitrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.domain.models.User
import kotlinx.coroutines.launch

class LoginViewModel(
    private val gitTrackRepository: GitTrackRepository
) : ViewModel() {

}

class LoginViewModelFactory(private val gitTrackRepository: GitTrackRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(gitTrackRepository) as T

        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}