package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.databinding.FragmentIssueBinding
import dev.borisochieng.gitrack.ui.viewmodels.SingleIssueViewModel
import dev.borisochieng.gitrack.ui.viewmodels.SingleIssueViewModelFactory

class SingleIssueFragment : Fragment() {
    private var _binding : FragmentIssueBinding? = null
    private val binding get() = _binding!!

    private val navArgs: SingleIssueFragmentArgs by navArgs()

    private val singleIssueViewModel: SingleIssueViewModel by viewModels {
        SingleIssueViewModelFactory((requireActivity().application as GitTrackApplication).gitTrackRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIssueBinding.inflate(layoutInflater, container, false)


        binding.mtIssue.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        val repoName = navArgs.singleIssue.repoName
        val repoOwner = navArgs.singleIssue.repoOwner
        val issueNumber = navArgs.singleIssue.number

        binding.mtIssue.title = "Issue #$issueNumber"

        getIssueFromViewModel(repoName, repoOwner, issueNumber)



        return binding.root
    }

    private fun getIssueFromViewModel(repoName: String, repoOwner: String, number: Int) {
        singleIssueViewModel.singleIssueLiveData.observe(viewLifecycleOwner) {singleIssue ->
        binding.apply {
            tvIssueTitle.text = singleIssue.title
            tvOpenDate.text = singleIssue.createdAt
            tvStatus.text = singleIssue.status
            tvBody.text = singleIssue.description
        }
        }
        singleIssueViewModel.getSingleIssue(repoName, repoOwner, number)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}