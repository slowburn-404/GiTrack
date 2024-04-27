package dev.borisochieng.gitrack.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.presentation.models.Issue
import dev.borisochieng.gitrack.presentation.models.IssueSearchResult
import kotlinx.coroutines.launch
import java.util.Locale

class RepositoryIssuesViewModel(
    private val gitTrackRepository: GitTrackRepository
) : ViewModel() {
    private val _issuesLiveData = MutableLiveData<List<Issue>>()
    val issuesLiveData = _issuesLiveData

    private val _searchIssueLiveData = MutableLiveData<List<IssueSearchResult>>()
    val searchResultLiveData = _searchIssueLiveData

    private val _filteredListLiveData = MutableLiveData<List<Issue>?>()
    val filteredListLiveData = _filteredListLiveData

    fun getIssues(name: String, owner: String) =
        viewModelScope.launch {
            try {
                val issues = gitTrackRepository.getRepositoryIssues(name, owner)
                _issuesLiveData.value = issues
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error fetching issues", e.message.toString())
            }
        }

    fun searchIssues(query: String) {
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                val lowerCaseQuery = query.lowercase(Locale.getDefault())
                val filteredList =
                    transformIssueToSearchResult(_issuesLiveData.value).filter { issue: IssueSearchResult ->
                        issue.title.lowercase(Locale.getDefault())
                            .contains(lowerCaseQuery) || "${issue.number}".lowercase(Locale.getDefault())
                            .contains(lowerCaseQuery)
                    }
                _searchIssueLiveData.value = filteredList

            } else {
                _searchIssueLiveData.value = emptyList()
            }
        }
    }

    fun filterByLabel(filterOptions: Set<String>) {
        viewModelScope.launch {
            val originalList = _issuesLiveData.value ?: return@launch
            val previousFilteredList = _filteredListLiveData.value ?: originalList

            val filteredList = if (filterOptions.isNotEmpty()) {
                originalList.filter { issue ->
                    issue.labels.containsAll(filterOptions)
                }
            } else {
                originalList
            }

            // If the current filteredList is different from the previous one, update _filteredListLiveData
            if (filteredList != previousFilteredList) {
                _filteredListLiveData.value = filteredList
            }
        }
    }

    fun clearFilter() {
        viewModelScope.launch {
            val originalList = _issuesLiveData.value ?: return@launch
            _filteredListLiveData.value = originalList
        }
    }


    private fun transformIssueToSearchResult(issuesList: List<Issue>?): List<IssueSearchResult> =
        issuesList?.map { issue ->
            IssueSearchResult(
                number = issue.number,
                title = issue.issueTitle,
                openedAt = issue.openedAt
            )
        } ?: emptyList()

}

class RepositoryIssuesViewModelFactory(
    private val gitTrackRepository: GitTrackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepositoryIssuesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepositoryIssuesViewModel(gitTrackRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")

    }
}