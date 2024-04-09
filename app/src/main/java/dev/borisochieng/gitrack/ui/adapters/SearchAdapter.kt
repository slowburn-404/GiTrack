package dev.borisochieng.gitrack.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.databinding.ItemSearchBinding
import dev.borisochieng.gitrack.ui.models.RepositorySearchResult

class SearchAdapter(
    private val onSearchResultItemClickListener: OnSearchResultItemClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RepositorySearchResult) {
            binding.apply {
                tvSearchItemTitle.text = item.repoName
                tvSearchItemDesc.text = item.repoDescription
                tvRepoOwner.text = item.repoOwner
                tvStarCount.text = item.starCount.toString()

                root.setOnClickListener {
                    onSearchResultItemClickListener.onSearchItemClick(item)
                }

            }
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun setList(list: List<RepositorySearchResult>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int  = asyncListDiffer.currentList.size


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RepositorySearchResult>() {
            override fun areItemsTheSame(
                oldItem: RepositorySearchResult,
                newItem: RepositorySearchResult
            ): Boolean =
                oldItem.repoId == newItem.repoId


            override fun areContentsTheSame(
                oldItem: RepositorySearchResult,
                newItem: RepositorySearchResult
            ): Boolean =
                oldItem == newItem


        }
    }
}


