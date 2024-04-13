package dev.borisochieng.gitrack.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.databinding.ItemIssueBinding
import dev.borisochieng.gitrack.ui.models.Issue

class IssueAdapter(private val onIssueClickListener: OnIssueClickListener) :
    RecyclerView.Adapter<IssueAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemIssueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Issue) {
            binding.apply {
                tvOpenedDate.text = item.openedAt
                tvStatus.text = item.issueStatus
                tvIssueTitle.text = item.issueTitle
                tvUsername.text = item.author
                tvCommentCount.text = item.commentCount.toString()

                root.setOnClickListener {
                    onIssueClickListener.onClick(item)
                }
            }
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, ISSUES_DIFF_CALLBACK)

    fun setList(list: List<Issue>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemIssueBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }

    companion object {
        private val ISSUES_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Issue>() {
            override fun areItemsTheSame(oldItem: Issue, newItem: Issue): Boolean {
                Log.d("DiffUtil", "areItemsTheSame: oldItem=$oldItem, newItem=$newItem")
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(oldItem: Issue, newItem: Issue): Boolean {
                Log.d("DiffUtil", "areContentsTheSame: oldItem=$oldItem, newItem=$newItem")
                return oldItem == newItem
            }
        }
    }
}