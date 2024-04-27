package dev.borisochieng.gitrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.borisochieng.gitrack.data.GitTrackRepository

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