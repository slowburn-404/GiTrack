package dev.borisochieng.gitrack.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.databinding.ItemRepositoryBinding
import dev.borisochieng.gitrack.domain.models.Repository
import androidx.recyclerview.widget.DiffUtil

class RepositoryAdapter(private val onRepositoryClickListener: SetRecyclerViewItemClickListener<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Repository) {
            binding.apply {
                repositoryName.text = item.name
                repositoryDesc.text = item.desc
                issueCount.text = item.issueCount.toString()
                starsCount.text = item.starCount.toString()
                //lastUpdated.text = item.lastUpdated

                root.setOnClickListener {
                    onRepositoryClickListener.setOnItemClickListener(item)
                }
            }
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun setList(list: List<Repository>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
                oldItem.databaseId == newItem.databaseId


            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
                oldItem == newItem

        }
    }
}