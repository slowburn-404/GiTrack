package dev.borisochieng.gitrack.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.search.SearchView
import com.google.android.material.textview.MaterialTextView
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.databinding.FragmentRepositoryIssuesBinding
import dev.borisochieng.gitrack.domain.models.Issue
import dev.borisochieng.gitrack.presentation.adapters.IssueAdapter
import dev.borisochieng.gitrack.presentation.adapters.SearchIssuesAdapter
import dev.borisochieng.gitrack.presentation.adapters.SetRecyclerViewItemClickListener
import dev.borisochieng.gitrack.presentation.models.IssueSearchResult
import dev.borisochieng.gitrack.presentation.models.SingleIssueParcelable
import dev.borisochieng.gitrack.presentation.viewmodels.RepositoryIssuesViewModel
import dev.borisochieng.gitrack.presentation.viewmodels.RepositoryIssuesViewModelFactory

class RepositoryIssuesFragment : Fragment() {
    private var _binding: FragmentRepositoryIssuesBinding? = null
    private val binding get() = _binding!!

    private lateinit var issuesRecyclerView: RecyclerView
    private lateinit var issuesAdapter: IssueAdapter
    private lateinit var searchResultRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: SearchIssuesAdapter
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var labelsChipGroup: ChipGroup
    private lateinit var searchResultSearchView: SearchView
    private lateinit var noIssuesTextView: MaterialTextView

    private val navArgs: RepositoryIssuesFragmentArgs by navArgs<RepositoryIssuesFragmentArgs>()

    private val repositoryIssuesViewModel: RepositoryIssuesViewModel by viewModels {
        RepositoryIssuesViewModelFactory((requireActivity().application as GitTrackApplication).gitTrackRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRepositoryIssuesBinding.inflate(layoutInflater, container, false)

        val repoName = navArgs.repository.name
        val repoOwner = navArgs.repository.owner

        binding.tvReponame.text = resources.getString(R.string.screen_title, repoName)


        initViews()
        progressIndicator.hide()
        initRecyclerView()
        initSearchRecyclerView()
        getIssuesFromViewModel(repoName, repoOwner)
        getLabelsFromViewModel()
        listenForTextChangesOnSearchView()
        filterBySelectedLabels()


        binding.issuesSearchBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun initViews() {
        binding.apply {
            issuesRecyclerView = rvIssues
            progressIndicator = issuesProgressCircular
            labelsChipGroup = cgLabels
            searchResultRecyclerView = rvIssuesSearchResults
            searchResultSearchView = svIssues
            noIssuesTextView = tvNoIssues
            tvNoIssues.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        val searchItemClickListener = setIssueItemClickListener()
        issuesAdapter = IssueAdapter(searchItemClickListener)
        issuesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = issuesAdapter
        }
    }

    private fun setIssueItemClickListener() = SetRecyclerViewItemClickListener<Issue> { item ->
        val clickedItem = SingleIssueParcelable(
            repoName = navArgs.repository.name,
            repoOwner = navArgs.repository.owner,
            issueNumber = item.number

        )
        val action: RepositoryIssuesFragmentDirections.ActionRepositoryIssuesFragmentToIssueFragment =
            RepositoryIssuesFragmentDirections.actionRepositoryIssuesFragmentToIssueFragment(
                clickedItem
            )
        findNavController().navigate(action)
    }

    private fun initSearchRecyclerView() {
        val searchItemClickListener = setSearchRecyclerViewItemListener()
        searchResultAdapter = SearchIssuesAdapter(searchItemClickListener)
        searchResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = searchResultAdapter
        }

    }

    private fun setSearchRecyclerViewItemListener() =
        SetRecyclerViewItemClickListener<IssueSearchResult> { item ->
            val clickedItem = SingleIssueParcelable(
                repoName = navArgs.repository.name,
                repoOwner = navArgs.repository.owner,
                issueNumber = item.number

            )
            val action: RepositoryIssuesFragmentDirections.ActionRepositoryIssuesFragmentToIssueFragment =
                RepositoryIssuesFragmentDirections.actionRepositoryIssuesFragmentToIssueFragment(
                    clickedItem
                )
            findNavController().navigate(action)

        }

    private fun getIssuesFromViewModel(name: String, owner: String) {
        progressIndicator.show()
        repositoryIssuesViewModel.getIssues(name, owner)
        repositoryIssuesViewModel.issuesLiveData.observe(viewLifecycleOwner) { issuesList ->
            if (issuesList.isNotEmpty()) {
                issuesAdapter.setList(issuesList)
            } else {
                issuesRecyclerView.visibility = View.GONE
                noIssuesTextView.visibility = View.VISIBLE
            }
            progressIndicator.hide()
        }
    }

    private fun getLabelsFromViewModel() {
        repositoryIssuesViewModel.labelsLiveData.observe(viewLifecycleOwner) { labelSet ->
            labelSet?.let {
                addLabelsToChipGroup(it)
                Log.d("Labels list", it.toString())
            }
        }
    }

    private fun addLabelsToChipGroup(labels: Set<String>) {
        labelsChipGroup.removeAllViews()
        labels.forEach { label ->
            val chip = Chip(requireContext())
            chip.text = label
            chip.isCheckable = true
            labelsChipGroup.addView(chip)
        }

    }

    private fun getSearchResultsFromViewModel(query: String) {
        repositoryIssuesViewModel.searchIssues(query)
        repositoryIssuesViewModel.searchResultLiveData.observe(viewLifecycleOwner) { searchResultList ->
            searchResultAdapter.setList(searchResultList)
        }
    }

    private fun listenForTextChangesOnSearchView() {
        searchResultSearchView.apply {
            //remove any listener
            editText.setOnEditorActionListener(null)
            editText.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = v.text.trim().toString()
                    getSearchResultsFromViewModel(query)
                }
                true
            }
            editText.addTextChangedListener(object : TextWatcher {
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
                            getSearchResultsFromViewModel(query.toString())
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

    private fun filterBySelectedLabels() {
        val selectedChipValues = mutableSetOf<String>()
        labelsChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedChipValues.clear()

            checkedIds.forEach { checkedId ->
                val selectedChip = group.findViewById<Chip>(checkedId)
                selectedChipValues.add("${selectedChip.text}")
            }

            repositoryIssuesViewModel.filterByLabel(selectedChipValues)
            getFilteredListFromViewModel()
        }

    }

    private fun getFilteredListFromViewModel() {
        repositoryIssuesViewModel.filteredListLiveData.observe(viewLifecycleOwner) { filteredIssues ->
            filteredIssues?.let { filteredIssuesList ->
                issuesAdapter.setList(filteredIssuesList)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}