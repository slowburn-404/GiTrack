package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.databinding.FragmentRepositoryIssuesBinding
import dev.borisochieng.gitrack.ui.Issue
import dev.borisochieng.gitrack.ui.adapters.IssueAdapter

class RepositoryIssuesFragment : Fragment() {
    private var _binding: FragmentRepositoryIssuesBinding? = null
    private val binding get() = _binding!!

    private lateinit var issuesRecyclerView : RecyclerView
    private lateinit var issuesAdapter : IssueAdapter
    private val issuesList : MutableList<Issue> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRepositoryIssuesBinding.inflate(layoutInflater, container, false)
        initViews()
        initRecyclerView()

        for (i in 1..10) {
            issuesList.add(Issue(i, "Array Out of bounds exception", "Open", "27 Mar 2024", "slowburn-404", resources.getQuantityString(R.plurals.comments, 6, 6)))
            issuesList.add(Issue(i, "Illegal argument exception", "Open", "30 Mar 2024", "grave-walker", resources.getQuantityString(R.plurals.comments, 1, 1)))
        }

        issuesAdapter.setList(issuesList)


        return binding.root
    }

    private fun initViews() {
        binding.apply {
            issuesRecyclerView = rvIssues
        }
    }

    private fun initRecyclerView () {
        issuesAdapter = IssueAdapter()
        issuesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = issuesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =  null
    }
}