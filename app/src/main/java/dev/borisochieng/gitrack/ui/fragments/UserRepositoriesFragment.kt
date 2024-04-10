package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.search.SearchView
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.databinding.FragmentUserRepositoriesBinding
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.adapters.OnRepositoryClickListener
import dev.borisochieng.gitrack.ui.adapters.OnSearchResultItemClickListener
import dev.borisochieng.gitrack.ui.adapters.RepositoryAdapter
import dev.borisochieng.gitrack.ui.adapters.SearchAdapter
import dev.borisochieng.gitrack.ui.models.RepositoryParcelable
import dev.borisochieng.gitrack.ui.models.RepositorySearchResult
import dev.borisochieng.gitrack.ui.viewmodels.UserRepositoriesViewModel
import dev.borisochieng.gitrack.ui.viewmodels.UserRepositoriesViewModelFactory
import dev.borisochieng.gitrack.utils.AccessTokenManager

class UserRepositoriesFragment : Fragment(), OnRepositoryClickListener,
    OnSearchResultItemClickListener {
    private var _binding: FragmentUserRepositoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var repositoryRecyclerView: RecyclerView
    private lateinit var repositoryAdapter: RepositoryAdapter
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var repositoryProgressCircular: CircularProgressIndicator
    private lateinit var searchProgressCircular: CircularProgressIndicator
    private lateinit var repositorySearchView: SearchView

    private val searchResultsList: MutableList<RepositorySearchResult> = mutableListOf()

    private lateinit var username: String

    private val userRepositoriesViewModel: UserRepositoriesViewModel by viewModels {
        UserRepositoriesViewModelFactory((requireActivity().application as GitTrackApplication).gitTrackRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserRepositoriesBinding.inflate(layoutInflater, container, false)


        initViews()
        repositoryProgressCircular.hide()
        searchProgressCircular.hide()
        initRecyclerView()
        initSearchRecyclerView()
        getUserFromViewModel()
        handleBackPress()
        getSearchResultFromAPI()

        binding.repositorySearchBar.setOnMenuItemClickListener {
            showDialog()
            true
        }

        return binding.root
    }

    private fun initViews() {
        binding.apply {
            repositoryRecyclerView = rvRepository
            repositoryProgressCircular = repoProgressCircular
            searchProgressCircular = searchCircularProgress
            searchResultsRecyclerView = rvRepositorySearchResults
            repositorySearchView = svRepository
        }
    }

    private fun initRecyclerView() {
        repositoryAdapter = RepositoryAdapter(this)
        repositoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = repositoryAdapter
        }
    }

    private fun initSearchRecyclerView() {
        searchAdapter = SearchAdapter(this)
        searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = searchAdapter
        }

        searchAdapter.setList(searchResultsList)

    }

    private fun getRepositoriesFromViewModel(username: String) {
        repositoryProgressCircular.show()
        userRepositoriesViewModel.getRepositories(username)
        userRepositoriesViewModel.repositoriesLiveData.observe(viewLifecycleOwner) { repositories ->
            repositoryAdapter.setList(repositories)
            repositoryProgressCircular.hide()
        }

    }

    private fun getUserFromViewModel() {
        userRepositoriesViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.tvUsername.text = "/${it.username}"
            username = it.username
            getRepositoriesFromViewModel(it.username)
        }
        userRepositoriesViewModel.getUser()
    }

    private fun getSearchResultsFromViewModel(query: String) {
        searchProgressCircular.show()
        searchResultsList.clear()
        userRepositoriesViewModel.searchResultsLiveData.observe(viewLifecycleOwner) { searchResults ->
            searchResultsList.addAll(searchResults)
            searchProgressCircular.hide()
            searchAdapter.notifyDataSetChanged()
        }
        userRepositoriesViewModel.searchPublicRepositories(query)
    }

    private fun getSearchResultFromAPI() {
        repositorySearchView.apply {
            //remove any listener
            editText.setOnEditorActionListener(null)

            editText.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = v.text.trim().toString()
                    getSearchResultsFromViewModel(query)

                }
                true
            }
        }
    }

    private fun handleBackPress() {
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.logout))
            .setMessage(resources.getString(R.string.confirm_logout))
            .setPositiveButton("Yes") { dialog, _ ->
                AccessTokenManager.clearAccessToken(requireContext())
                findNavController().popBackStack()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(item: Repository) {
        val clickedItem =
            RepositoryParcelable(
                item.databaseId,
                item.name,
                item.owner
            )
        val action: UserRepositoriesFragmentDirections.ActionUserRepositoriesFragmentToRepositoryIssuesFragment =
            UserRepositoriesFragmentDirections.actionUserRepositoriesFragmentToRepositoryIssuesFragment(
                clickedItem
            )
        findNavController().navigate(action)
    }

    override fun onSearchItemClick(item: RepositorySearchResult) {
        val clickedItem =
            RepositoryParcelable(
                item.databaseId,
                item.repoName,
                item.repoOwner
            )
        val action =
            UserRepositoriesFragmentDirections.actionUserRepositoriesFragmentToRepositoryIssuesFragment(
                clickedItem
            )
        findNavController().navigate(action)
    }

}