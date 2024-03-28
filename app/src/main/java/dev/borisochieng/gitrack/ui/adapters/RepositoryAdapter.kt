package dev.borisochieng.gitrack.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import dev.borisochieng.gitrack.databinding.ItemRepositoryBinding
import dev.borisochieng.gitrack.ui.Repository
import dev.borisochieng.gitrack.utils.RVDiffUtil

class RepositoryAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (item: Repository) {
            binding.apply {
                repositoryTitle.text = item.title
                repositoryDesc.text = item.desc
                issueCount.text = item.issueCount.toString()
                starsCount.text = item.starCount.toString()
                //lastUpdated.text = item.lastUpdated


                root.setOnClickListener {
                    onItemClickListener.onItemClick()
                }
            }

        }



    }
    private val asyncListDiffer = AsyncListDiffer(this, RVDiffUtil())

    fun setList(list: MutableList<Repository>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int  = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
         holder.bind(item)
    }
}