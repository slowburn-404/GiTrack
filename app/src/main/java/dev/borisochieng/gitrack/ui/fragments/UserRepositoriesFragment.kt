package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import android.util.Log
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
import dev.borisochieng.gitrack.databinding.FragmentUserRepositoriesBinding
import dev.borisochieng.gitrack.ui.models.Repository
import dev.borisochieng.gitrack.ui.adapters.OnRepositoryClickListener
import dev.borisochieng.gitrack.ui.adapters.RepositoryAdapter
import dev.borisochieng.gitrack.ui.models.RepositoryParcelable
import dev.borisochieng.gitrack.ui.viewmodels.UserRepositoriesViewModel
import dev.borisochieng.gitrack.ui.viewmodels.UserRepositoriesViewModelFactory

class UserRepositoriesFragment : Fragment(), OnRepositoryClickListener {
    private var _binding: FragmentUserRepositoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var repositoryRecyclerView: RecyclerView
    private lateinit var repositoryAdapter: RepositoryAdapter

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
        //repositoryAdapter.setList(emptyList())

    }

    private fun getRepositoriesFromViewModel(username: String) {
        userRepositoriesViewModel.repositoriesLiveData.observe(viewLifecycleOwner) { repositoryList ->
            repositoryAdapter.setList(repositoryList)
            Log.d("Repository List", repositoryList.toString())
        }
        userRepositoriesViewModel.getRepositories(username)

    }

    private fun getUserFromViewModel() {
        userRepositoriesViewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvUsername.text = "/${it.username}"
            username = it.username
            getRepositoriesFromViewModel(it.username)
        })
        userRepositoriesViewModel.getUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        getUserFromViewModel()
    }

    override fun onItemClick(item: Repository) {
        val clickedItem =
            RepositoryParcelable(item.id, item.title, username)
        val action =
            UserRepositoriesFragmentDirections.actionUserRepositoriesFragmentToRepositoryIssuesFragment(
                clickedItem
            )
        findNavController().navigate(action)
    }

}