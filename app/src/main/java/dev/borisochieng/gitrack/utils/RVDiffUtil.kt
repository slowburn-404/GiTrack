package dev.borisochieng.gitrack.utils

import androidx.recyclerview.widget.DiffUtil
import dev.borisochieng.gitrack.ui.Repository

class RVDiffUtil : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem == newItem

}