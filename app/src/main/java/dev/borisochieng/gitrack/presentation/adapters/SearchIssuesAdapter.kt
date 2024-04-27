package dev.borisochieng.gitrack.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.databinding.ItemIssuesSearchBinding
import dev.borisochieng.gitrack.presentation.models.IssueSearchResult

class SearchIssuesAdapter(
    private val onSearchResultItemClickListener: SetRecyclerViewItemClickListener<IssueSearchResult>
) : RecyclerView.Adapter<SearchIssuesAdapter.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, SEARCH_ISSUES_DIFF_CALLBACK)

    inner class ViewHolder(private val binding: ItemIssuesSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IssueSearchResult) {
            binding.apply {
                tvSearchIssueNumber.text = "#${item.number}"
                tvSearchIssueTitle.text = item.title
                tvSearchIssueOpened.text = item.openedAt


                root.setOnClickListener {
                    onSearchResultItemClickListener.setOnItemClickListener(item)
                }

            }
        }
    }

    fun setList(list: List<IssueSearchResult>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemIssuesSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size


    companion object {
        private val SEARCH_ISSUES_DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<IssueSearchResult>() {
                override fun areItemsTheSame(
                    oldItem: IssueSearchResult,
                    newItem: IssueSearchResult
                ): Boolean =
                    oldItem.number == newItem.number


                override fun areContentsTheSame(
                    oldItem: IssueSearchResult,
                    newItem: IssueSearchResult
                ): Boolean =
                    oldItem == newItem

            }
    }
}