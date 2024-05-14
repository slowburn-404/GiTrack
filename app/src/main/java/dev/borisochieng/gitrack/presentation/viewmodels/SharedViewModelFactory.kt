package dev.borisochieng.gitrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.borisochieng.gitrack.data.repositories.AuthRepository
import dev.borisochieng.gitrack.data.repositories.GitTrackRepository

class SharedViewModelFactory(
    private val authRepository: AuthRepository,
    private val gitTrackRepository: GitTrackRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(UserRepositoriesViewModel::class.java) -> {
                UserRepositoriesViewModel(gitTrackRepository) as T
            }

            modelClass.isAssignableFrom(RepositoryIssuesViewModel::class.java) -> {
                RepositoryIssuesViewModel(gitTrackRepository) as T
            }

            modelClass.isAssignableFrom(SingleIssueViewModel::class.java) -> {
                SingleIssueViewModel(gitTrackRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
