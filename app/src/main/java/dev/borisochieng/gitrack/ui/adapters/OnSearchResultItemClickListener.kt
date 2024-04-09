package dev.borisochieng.gitrack.ui.adapters

import dev.borisochieng.gitrack.ui.models.RepositorySearchResult

interface OnSearchResultItemClickListener {

    fun onSearchItemClick(item: RepositorySearchResult)
}