package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.R
import dev.borisochieng.gitrack.databinding.FragmentUserRepositoriesBinding
import dev.borisochieng.gitrack.ui.Repository
import dev.borisochieng.gitrack.ui.adapters.OnItemClickListener
import dev.borisochieng.gitrack.ui.adapters.RepositoryAdapter

class UserRepositoriesFragment : Fragment(), OnItemClickListener {
    private var _binding : FragmentUserRepositoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var repositoryRecyclerView: RecyclerView
    private lateinit var repositoryAdapter: RepositoryAdapter
    private val repositoryList : MutableList<Repository> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserRepositoriesBinding.inflate(layoutInflater, container, false)

        initViews()
        initRecyclerView()

        for(i in 1..10) {
            repositoryList.add(Repository(i,"NoteWave",
                resources.getString(R.string.repository_desc), 200, 150, "10 Feb 2024"))

            repositoryList.add(Repository(i,"Boriabe",
                resources.getString(R.string.repository_desc), 200, 150, "20 Mar 2024"))
        }

        Log.d("Repository List", repositoryList.toString())
        repositoryAdapter.setList(repositoryList)



        return binding.root
    }

    private fun initViews() {
        binding.apply {
            repositoryRecyclerView = rvRepository

        }
    }

    private fun initRecyclerView() {
        repositoryAdapter  = RepositoryAdapter(this)
        repositoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = repositoryAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick() {
        findNavController().navigate(R.id.action_userRepositoriesFragment_to_repositoryIssuesFragment)
    }

}