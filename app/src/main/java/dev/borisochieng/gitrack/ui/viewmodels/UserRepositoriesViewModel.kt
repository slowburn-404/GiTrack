package dev.borisochieng.gitrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Repository
import kotlinx.coroutines.launch

class UserRepositoriesViewModel(
    private val gitTrackRepository: GitTrackRepository
): ViewModel() {
    private val _repositoriesLiveData = MutableLiveData<List<Repository>>()
    val repositoriesLiveData = _repositoriesLiveData

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> = _userLiveData

    fun getUser() =
        viewModelScope.launch {
            try {
                val user = gitTrackRepository.getUser()
                _userLiveData.value = user
            } catch (e: Exception) {
                Log.e("Error fetching user:", e.message.toString())
            }
        }

    fun getRepositories(username: String) =
        viewModelScope.launch {
            try {
                val userRepositories = gitTrackRepository.getRepositories(username)
                _repositoriesLiveData.value = userRepositories
            }catch (e: Exception) {
                Log.e("Error fetching repositories", e.message.toString())
            }
        }
}

class UserRepositoriesViewModelFactory(
    private val gitTrackRepository: GitTrackRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserRepositoriesViewModel(gitTrackRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")

    }
}