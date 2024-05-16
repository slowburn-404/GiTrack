package dev.borisochieng.gitrack.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.search.SearchView
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.databinding.FragmentUserRepositoriesBinding
import dev.borisochieng.gitrack.domain.models.Repository
import dev.borisochieng.gitrack.presentation.adapters.SetRecyclerViewItemClickListener
import dev.borisochieng.gitrack.presentation.adapters.RepositoryAdapter
import dev.borisochieng.gitrack.presentation.adapters.SearchAdapter
import dev.borisochieng.gitrack.presentation.models.RepositoryParcelable
import dev.borisochieng.gitrack.domain.models.RepositorySearchResult
import dev.borisochieng.gitrack.presentation.viewmodels.UserRepositoriesViewModel
import dev.borisochieng.gitrack.utils.AccessTokenManager

class UserRepositoriesFragment : Fragment() {
    private var _binding: FragmentUserRepositoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var repositoryRecyclerView: RecyclerView
    private lateinit var repositoryAdapter: RepositoryAdapter
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var repositoryProgressCircular: CircularProgressIndicator
    private lateinit var searchProgressCircular: CircularProgressIndicator
    private lateinit var repositorySearchView: SearchView
    private lateinit var languagesChipGroup: ChipGroup
    private lateinit var sortByChip: Chip


    private lateinit var username: String

    internal val userRepositoriesViewModel: UserRepositoriesViewModel by viewModels {
        (requireActivity().application as GitTrackApplication).sharedViewModelFactory
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
        getLanguagesFromViewModel()
        handleBackPress()
        getSearchResultFromAPI()
        filterByLanguage()


        binding.repositorySearchBar.setOnMenuItemClickListener {
            showDialog()
            true
        }
        sortByChip.setOnClickListener {
            showBottomSheet()
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
            tvNoRepos.visibility = View.GONE
            languagesChipGroup = cgLanguages
            sortByChip = chipSortBy
        }
    }

    private fun initRecyclerView() {
        val repositoryClickListener = setOnRepositoryItemClickListener()
        repositoryAdapter = RepositoryAdapter(repositoryClickListener)
        repositoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = repositoryAdapter
        }
    }

    private fun setOnRepositoryItemClickListener() =
        SetRecyclerViewItemClickListener<Repository> { item ->
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

    private fun initSearchRecyclerView() {
        val searchResultClickListener = setonSearchResultClickListener()
        searchAdapter = SearchAdapter(searchResultClickListener)
        searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = searchAdapter
        }
    }

    private fun setonSearchResultClickListener() =
        SetRecyclerViewItemClickListener<RepositorySearchResult> { item ->
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

    private fun getUserFromViewModel() {
        userRepositoriesViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.tvUsername.text = resources.getString(R.string.screen_title, it.username)
            username = it.username
            getRepositoriesFromViewModel(it.username)
        }
        userRepositoriesViewModel.getUser()
    }

    private fun getRepositoriesFromViewModel(username: String) {
        repositoryProgressCircular.show()
        userRepositoriesViewModel.getRepositories(username)
        userRepositoriesViewModel.repositoriesLiveData.observe(viewLifecycleOwner) { repositoriesList ->
            if (repositoriesList?.isNotEmpty() == true) {
                repositoryAdapter.setList(repositoriesList)
            } else {
                repositoryRecyclerView.visibility = View.GONE
                binding.tvNoRepos.visibility = View.VISIBLE
            }
            repositoryProgressCircular.hide()
        }

    }

    private fun getLanguagesFromViewModel() {
        userRepositoriesViewModel.languagesLiveData.observe(viewLifecycleOwner) { languages ->
            languages?.let {
                addLanguagesToChipGroup(it)
            }
        }
    }

    private fun getSearchResultsFromViewModel(query: String) {
        searchProgressCircular.show()
        userRepositoriesViewModel.searchPublicRepositories(query)
        userRepositoriesViewModel.searchResultsLiveData.observe(viewLifecycleOwner) { searchResults ->
            searchAdapter.setList(searchResults)
            searchProgressCircular.hide()
        }
    }

    private fun getSearchResultFromAPI() {
        repositorySearchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { query ->
                    if (query.isNotEmpty()) {
                        getSearchResultsFromViewModel(s.toString())
                        scrollToTopOfRV(searchResultsRecyclerView)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

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

    private fun showBottomSheet() {
        val sortByBottomSheet = SortByBottomSheet()
        sortByBottomSheet.show(childFragmentManager, SortByBottomSheet.TAG)
    }

    private fun addLanguagesToChipGroup(languages: Set<String>) {
        languagesChipGroup.removeAllViews()
        languages.forEach { language ->
            val languageChip = Chip(requireContext())
            languageChip.apply {
                text = language
                isCheckable = true
            }
            languagesChipGroup.addView(languageChip)
        }
    }

    private fun getFilteredListFromViewModel() {
        userRepositoriesViewModel.filteredListLiveData.observe(viewLifecycleOwner) { filteredList ->
            if (filteredList.isNotEmpty()) {
                repositoryAdapter.setList(filteredList)
                scrollToTopOfRV(repositoryRecyclerView)
            }
        }
    }

    private fun filterByLanguage() {
        languagesChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val selectedChipId = group.checkedChipId
            //chip selection check
            if (selectedChipId != -1) {
                val selectedChip = group.findViewById<Chip>(selectedChipId)
                val selectedLanguage = selectedChip.text.toString()
                userRepositoriesViewModel.filterByLanguage(selectedLanguage)
                getFilteredListFromViewModel()
            } else {
                userRepositoriesViewModel.clearFilter()
                scrollToTopOfRV(repositoryRecyclerView)

            }
        }
    }

    private fun scrollToTopOfRV(recyclerView: RecyclerView) {
        recyclerView.post {
            recyclerView.layoutManager?.let{layoutManager ->
                if(layoutManager is LinearLayoutManager) {
                    layoutManager.scrollToPosition(0)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}