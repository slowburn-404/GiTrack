package dev.borisochieng.gitrack.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.repositories.GitTrackRepository
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.domain.models.Repository
import dev.borisochieng.gitrack.domain.models.RepositorySearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class UserRepositoriesViewModel(
    private val gitTrackRepository: GitTrackRepository
) : ViewModel() {
    private val _repositoriesLiveData = MutableLiveData<List<Repository>?>()
    val repositoriesLiveData get() = _repositoriesLiveData

    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData

    private val _searchResultsLiveData = MutableLiveData<List<RepositorySearchResult>>()
    val searchResultsLiveData: LiveData<List<RepositorySearchResult>> get() = _searchResultsLiveData

    private val _filteredListLiveData = MutableLiveData<List<Repository>>()
    val filteredListLiveData get() = _filteredListLiveData

    private val _languagesLiveData = MutableLiveData<Set<String>>()
    val languagesLiveData get() = _languagesLiveData


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
                //return early if there are no repositories
                val userRepositories = gitTrackRepository.getRepositories(username) ?: return@launch

                val sortedRepositories = withContext(Dispatchers.Default) {
                    userRepositories.sortedBy { it.createdAt }
                }
                _repositoriesLiveData.value = sortedRepositories
                getLanguages(sortedRepositories)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error fetching repositories", e.message.toString())
            }
        }

    private fun getLanguages(repositoryList: List<Repository>) =
        viewModelScope.launch(Dispatchers.Default) {
            //flatten repositories to languages and convert to set for unique languages
            val languageList =
                repositoryList.flatMap { it.languages!! }.toSet()

            withContext(Dispatchers.Main) {
                _languagesLiveData.value = languageList
            }
        }

    fun filterByLanguage(selectedLanguage: String) =
        viewModelScope.launch(Dispatchers.Default) {
            //return early if the list is null
            val originalList = _repositoriesLiveData.value ?: return@launch
            val filteredList = if (selectedLanguage.isBlank()) {
                originalList
            } else {
                originalList.filter { repository ->
                    repository.languages?.contains(selectedLanguage) == true
                }
            }
            withContext(Dispatchers.Main) {
                _filteredListLiveData.value = filteredList
            }
        }

    fun clearFilter() =
        viewModelScope.launch {
            val originalList = _repositoriesLiveData.value ?: return@launch
            _filteredListLiveData.value = originalList

        }

    fun sortBy(sortCondition: String) =
        viewModelScope.launch(Dispatchers.Default) {
            val originalList = _repositoriesLiveData.value ?: return@launch
            val filteredList = _filteredListLiveData.value //also sort filtered list
            val sortedList = when (sortCondition) {
                "Oldest" -> {
                    //sort original list if filtered list is empty
                    (filteredList ?: originalList).sortedBy { parseDate(it.createdAt) }
                }

                "Most Stars" -> {
                    (filteredList ?: originalList).sortedByDescending { it.starCount }
                }

                else -> {
                    (filteredList ?: originalList).sortedByDescending { it.issueCount }
                }
            }
            if (filteredList != null) {
                withContext(Dispatchers.Main) {
                    _filteredListLiveData.value = sortedList
                }
            } else {
                withContext(Dispatchers.Main) {
                    _repositoriesLiveData.value = sortedList
                }
            }
        }

    fun searchPublicRepositories(query: String) =
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                try {
                    val searchResults =
                        gitTrackRepository.searchPublicRepositories(query) ?: return@launch

                    _searchResultsLiveData.value = searchResults
                } catch (e: Exception) {
                    //TODO("Better error handling")
                    e.printStackTrace()
                    Log.e("Error searching", e.message.toString())
                }
            }
        }


    private fun parseDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
        return LocalDate.parse(dateString, formatter)
    }
}