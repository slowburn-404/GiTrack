package dev.borisochieng.gitrack.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.databinding.ItemIssueBinding
import dev.borisochieng.gitrack.ui.Issue
import dev.borisochieng.gitrack.utils.IssuesDiffUtil
class IssueAdapter() : RecyclerView.Adapter<IssueAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemIssueBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Issue) {
            binding.apply {
                tvOpenedDate.text = item.openedAt
                tvStatus.text = item.issueStatus
                tvIssueTitle.text = item.issueTitle
                tvUsername.text = item.username
                tvCommentCount.text = item.commentCount.toString()
            }
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, IssuesDiffUtil())

    fun setList (list: MutableList<Issue>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemIssueBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }
}