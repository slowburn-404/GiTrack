package dev.borisochieng.gitrack.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.CircularProgressIndicator
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.databinding.FragmentRepositoryIssuesBinding
import dev.borisochieng.gitrack.ui.models.Issue
import dev.borisochieng.gitrack.ui.adapters.IssueAdapter
import dev.borisochieng.gitrack.ui.adapters.OnIssueClickListener
import dev.borisochieng.gitrack.ui.models.SingleIssueParcelable
import dev.borisochieng.gitrack.ui.viewmodels.RepositoryIssuesViewModel
import dev.borisochieng.gitrack.ui.viewmodels.RepositoryIssuesViewModelFactory

class RepositoryIssuesFragment : Fragment(), OnIssueClickListener {
    private var _binding: FragmentRepositoryIssuesBinding? = null
    private val binding get() = _binding!!

    private lateinit var issuesRecyclerView: RecyclerView
    private lateinit var issuesAdapter: IssueAdapter
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var labelsChipGroup: ChipGroup

    //private val issuesListFromAPi: MutableList<Issue> = mutableListOf()

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

        binding.tvReponame.text = "/$repoName"


        initViews()
        progressIndicator.hide()
        initRecyclerView()
        getIssuesFromViewModel(repoName, repoOwner)

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
        }
    }

    private fun initRecyclerView() {
        issuesAdapter = IssueAdapter(this)
        issuesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = issuesAdapter
        }
    }

    private fun getIssuesFromViewModel(name: String, owner: String) {
        progressIndicator.show()
        repositoryIssuesViewModel.getIssues(name, owner)
        repositoryIssuesViewModel.issuesLiveData.observe(viewLifecycleOwner) { issuesList ->
            issuesAdapter.setList(issuesList)
            val uniqueLabels = mutableSetOf<String>()
            issuesList.flatMap { issue -> issue.labels }
                .forEach { label ->
                    uniqueLabels.add(label)
                    addLabelsToChipGroup(uniqueLabels)
                }
            progressIndicator.hide()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: Issue) {
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
}