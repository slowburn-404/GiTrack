package dev.borisochieng.gitrack.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import dev.borisochieng.gitrack.databinding.BottomSheetSortBinding

class SortByBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSortBinding? = null
    private val binding get() = _binding!!

    private lateinit var oldestCard: MaterialCardView
    private lateinit var mostStarredCard: MaterialCardView
    private lateinit var mostIssuesCard: MaterialCardView
    private lateinit var oldestTextView: MaterialTextView
    private lateinit var mostStarredTextView: MaterialTextView
    private lateinit var mostIssuesTextView: MaterialTextView

    private val userRepositoriesFragmentViewModel by lazy {
        (requireParentFragment() as UserRepositoriesFragment).userRepositoriesViewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSortBinding.inflate(inflater, container, false)

        initViews()
        sortonClick()

        return binding.root
    }

    private fun initViews() {
        binding.apply {
            oldestCard = cardOldest
            mostStarredCard = cardMostStars
            mostIssuesCard = cardMostIssues
            oldestTextView = tvOldest
            mostStarredTextView = tvMostStarred
            mostIssuesTextView = tvMostIssues
        }
    }

    private fun sortBy(sortOption: String) {
        userRepositoriesFragmentViewModel.sortBy(sortOption)
        dismiss() //dismiss BottomSheet
    }

    private fun sortonClick() {
        oldestCard.setOnClickListener {
            sortBy(oldestTextView.text.toString())
        }
        mostStarredCard.setOnClickListener {
            sortBy(mostStarredTextView.text.toString())
        }
        mostIssuesCard.setOnClickListener {
            sortBy(mostIssuesTextView.text.toString())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SortByBottomSheet"
    }
}