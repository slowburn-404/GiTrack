package dev.borisochieng.gitrack.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.borisochieng.gitrack.databinding.FragmentIssueBinding

class IssueFragment : Fragment() {
    private var _binding : FragmentIssueBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIssueBinding.inflate(layoutInflater, container, false)


        binding.mtIssue.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}