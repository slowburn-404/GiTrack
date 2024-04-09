package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private val issuesListFromAPi: MutableList<Issue> = mutableListOf()

    private val navArgs: RepositoryIssuesFragmentArgs by navArgs<RepositoryIssuesFragmentArgs>()

    private val repositoryIssuesViewModel: RepositoryIssuesViewModel by viewModels{
        RepositoryIssuesViewModelFactory((requireActivity().application as GitTrackApplication).gitTrackRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRepositoryIssuesBinding.inflate(layoutInflater, container, false)

        val repoName = navArgs.repository.title
        val repoOwner = navArgs.repository.username

        binding.tvReponame.text = repoName


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
        }
    }

    private fun initRecyclerView() {
        issuesAdapter = IssueAdapter(this)
        issuesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = issuesAdapter
        }
        issuesAdapter.setList(issuesListFromAPi)
    }

    private fun getIssuesFromViewModel (name: String, owner: String) {
      progressIndicator.show()
        issuesListFromAPi.clear()
        repositoryIssuesViewModel.issuesLiveData.observe(viewLifecycleOwner) {issues ->
            issuesListFromAPi.addAll(issues)
            issuesAdapter.notifyDataSetChanged()
            progressIndicator.hide()
        }
        repositoryIssuesViewModel.getIssues(name, owner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        issuesListFromAPi.clear()
    }

    override fun onClick(item: Issue) {
        val clickedItem = SingleIssueParcelable(
            navArgs.repository.repoName,
            item.username,
            item.number)
        val action =
            RepositoryIssuesFragmentDirections.actionRepositoryIssuesFragmentToIssueFragment(clickedItem)
        findNavController().navigate(action)
    }
}