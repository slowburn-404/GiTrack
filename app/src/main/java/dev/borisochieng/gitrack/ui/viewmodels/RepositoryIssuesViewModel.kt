package dev.borisochieng.gitrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.ui.models.Issue
import kotlinx.coroutines.launch

class RepositoryIssuesViewModel(
    private val gitTrackRepository: GitTrackRepository
): ViewModel() {
    private val _issuesLiveData = MutableLiveData<List<Issue>>()
    val issuesLiveData = _issuesLiveData

    fun getIssues(name: String, owner: String) =
        viewModelScope.launch {
            try {
                val issues = gitTrackRepository.getRepositoryIssues(name, owner)
                _issuesLiveData.value = issues
            } catch (e: Exception) {
                Log.e("Error fetching issues", e.message.toString())
            }
        }
}
class RepositoryIssuesViewModelFactory(
    private val gitTrackRepository: GitTrackRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RepositoryIssuesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepositoryIssuesViewModel(gitTrackRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")

    }
}