package dev.borisochieng.gitrack.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.repositories.GitTrackRepository
import dev.borisochieng.gitrack.domain.models.Issue
import dev.borisochieng.gitrack.presentation.models.IssueSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class RepositoryIssuesViewModel(
    private val gitTrackRepository: GitTrackRepository
) : ViewModel() {
    private val _issuesLiveData = MutableLiveData<List<Issue>>()
    val issuesLiveData get() = _issuesLiveData

    private val _searchIssueLiveData = MutableLiveData<List<IssueSearchResult>>()
    val searchResultLiveData get() = _searchIssueLiveData

    private val _filteredListLiveData = MutableLiveData<List<Issue>?>()
    val filteredListLiveData get() = _filteredListLiveData

    private val _labelsLiveData = MutableLiveData<Set<String>>()
    val labelsLiveData get() = _labelsLiveData

    fun getIssues(name: String, owner: String) =
        viewModelScope.launch {
            try {
                val issues = gitTrackRepository.getRepositoryIssues(name, owner)

                withContext(Dispatchers.Main) {
                    issues.let {
                        _issuesLiveData.value = it
                        getLabels(it)
                    }
                }

            } catch (e: Exception) {
                //TODO("To dispatch coroutine to main thread and update UI")
                e.printStackTrace()
                Log.e("Error fetching issues", e.message.toString())
            }
        }

    private fun getLabels(issuesList: List<Issue>) =
        viewModelScope.launch(Dispatchers.Default) {
            val labelSet =
            issuesList.flatMap {it.labels }.toSet()

            withContext(Dispatchers.Main) {
                _labelsLiveData.value = labelSet
            }
        }

    fun filterByLabel(filterOptions: Set<String>) =
        viewModelScope.launch(Dispatchers.Default) {
            val originalList = _issuesLiveData.value ?: return@launch
            //val previousFilteredList = _filteredListLiveData.value ?: originalList

            val filteredList = if (filterOptions.isNotEmpty()) {
                originalList.filter { issue ->
                    issue.labels.containsAll(filterOptions)
                }
            } else {
                originalList
            }

            withContext(Dispatchers.Main) {
                _filteredListLiveData.value = filteredList
            }
        }


    fun searchIssues(query: String) =
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isNotEmpty()) {
                val lowerCaseQuery = query.lowercase(Locale.getDefault())
                val filteredList =
                    transformIssueToSearchResult(_issuesLiveData.value).filter { issue: IssueSearchResult ->
                        issue.title.lowercase(Locale.getDefault())
                            .contains(lowerCaseQuery) || "${issue.number}".lowercase(Locale.getDefault())
                            .contains(lowerCaseQuery)
                    }

                withContext(Dispatchers.Main) {
                    _searchIssueLiveData.value = filteredList
                }

            } else {
                withContext(Dispatchers.Main) {
                    _searchIssueLiveData.value = emptyList()
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