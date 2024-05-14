package dev.borisochieng.gitrack.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.borisochieng.gitrack.data.repositories.GitTrackRepository
import dev.borisochieng.gitrack.domain.models.SingleIssue
import kotlinx.coroutines.launch

class SingleIssueViewModel(
    private val gitTrackRepository: GitTrackRepository
) : ViewModel() {
    private val _singleIssueLiveData = MutableLiveData<SingleIssue>()
    val singleIssueLiveData: LiveData<SingleIssue> = _singleIssueLiveData


    fun getSingleIssue(name: String, owner: String, number: Int) =
        viewModelScope.launch {
            try {
                val singleIssue = gitTrackRepository.getSingleIssue(name, owner, number)
                _singleIssueLiveData.value = singleIssue
            } catch (e: Exception) {
                Log.e("Error getting the issue", e.message.toString())
            }
        }
}