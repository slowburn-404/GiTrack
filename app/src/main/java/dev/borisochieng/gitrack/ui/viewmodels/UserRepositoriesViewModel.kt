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
import dev.borisochieng.gitrack.ui.models.RepositorySearchResult
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class UserRepositoriesViewModel(
    private val gitTrackRepository: GitTrackRepository
) : ViewModel() {
    private val _repositoriesLiveData = MutableLiveData<List<Repository>?>()
    val repositoriesLiveData = _repositoriesLiveData

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> = _userLiveData

    private val _searchResultsLiveData = MutableLiveData<List<RepositorySearchResult>>()
    val searchResultsLiveData: LiveData<List<RepositorySearchResult>> = _searchResultsLiveData

    fun getUser() =
        viewModelScope.launch {
            try {
                val user = gitTrackRepository.getUser()
                _userLiveData.value = user
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error fetching user:", e.message.toString())
            }
        }

    fun getRepositories(username: String) =
        viewModelScope.launch {
            try {
                val userRepositories = gitTrackRepository.getRepositories(username)
                _repositoriesLiveData.value = userRepositories
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error fetching repositories", e.message.toString())
            }
        }

    fun searchPublicRepositories(query: String) =
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                try {
                    val searchResults = gitTrackRepository.searchPublicRepositories(query)
                    _searchResultsLiveData.value = searchResults
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("Error searching", e.message.toString())
                }
            }
        }

    fun sortBy(filterCondition: String) =
        viewModelScope.launch {
            val originalList = _repositoriesLiveData.value
            val filteredList = when (filterCondition) {
                "Most Recent" -> {
                    originalList?.sortedByDescending { parseDate(it.createdAt) }
                }

                "Most Stars" -> {
                    originalList?.sortedByDescending { it.starCount }
                }

                else -> {
                    originalList?.sortedByDescending { it.issueCount }
                }
            }
            _repositoriesLiveData.value = filteredList
        }


    private fun parseDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        return LocalDate.parse(dateString, formatter)
    }
}

class UserRepositoriesViewModelFactory(
    private val gitTrackRepository: GitTrackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserRepositoriesViewModel(gitTrackRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")

    }
}