package dev.borisochieng.gitrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.GitTrackRepository
import dev.borisochieng.gitrack.ui.models.SingleIssue
import kotlinx.coroutines.launch

class SingleIssueViewModel(
    private val gitTrackRepository: GitTrackRepository
) : ViewModel() {
    private val _singleIssueLiveData = MutableLiveData<SingleIssue>()
    val singleIssueLiveData: LiveData<SingleIssue> = _singleIssueLiveData


    fun getSingleIssue(name: String, owner: String, number: Int, first: Int) =
        viewModelScope.launch {
            try {
                val singleIssue = gitTrackRepository.getSingleIssue(name, owner, number, first)
                _singleIssueLiveData.value = singleIssue
            } catch (e: Exception) {
                Log.e("Error getting the issue", e.message.toString())
            }
        }
}

class SingleIssueViewModelFactory(
    private val gitTrackRepository: GitTrackRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SingleIssueViewModel::class.java)) {
            return SingleIssueViewModel(gitTrackRepository) as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")
    }
}