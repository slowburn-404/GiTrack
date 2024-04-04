package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.GitTrackApplication
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.databinding.FragmentUserRepositoriesBinding
import dev.borisochieng.gitrack.domain.models.User
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.adapters.OnItemClickListener
import dev.borisochieng.gitrack.ui.adapters.RepositoryAdapter
import dev.borisochieng.gitrack.ui.viewmodels.UserRepositoriesViewModel
import dev.borisochieng.gitrack.ui.viewmodels.UserRepositoriesViewModelFactory

class UserRepositoriesFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentUserRepositoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var repositoryRecyclerView: RecyclerView
    private lateinit var repositoryAdapter: RepositoryAdapter
    private val repositoryList = mutableListOf<Repository>()

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
        initRecyclerView()

        getUserFromViewModel()

        return binding.root
    }

    private fun initViews() {
        binding.apply {
            repositoryRecyclerView = rvRepository
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

    private fun getRepositoriesFromViewModel(username: String) {
        repositoryList.clear()
        userRepositoriesViewModel.repositoriesLiveData.observe(viewLifecycleOwner, Observer {
            repositoryList.addAll(it)
            repositoryAdapter.setList(it)
        })
        userRepositoriesViewModel.getRepositories(username)

    }

    private fun getUserFromViewModel() {
        userRepositoriesViewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvUsername.text = "/${it.username}"
            getRepositoriesFromViewModel(it.username)
        })
        userRepositoriesViewModel.getUser()
    }

    override fun onResume() {
        super.onResume()
        getUserFromViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick() {
        findNavController().navigate(R.id.action_userRepositoriesFragment_to_repositoryIssuesFragment)
    }

}