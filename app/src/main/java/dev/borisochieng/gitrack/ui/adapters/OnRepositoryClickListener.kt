package dev.borisochieng.gitrack.ui.adapters

import dev.borisochieng.gitrack.ui.models.Repository

interface OnRepositoryClickListener {
    fun onItemClick (item: Repository)
}