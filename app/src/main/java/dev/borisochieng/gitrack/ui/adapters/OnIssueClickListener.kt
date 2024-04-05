package dev.borisochieng.gitrack.ui.adapters

import dev.borisochieng.gitrack.ui.models.Issue

interface OnIssueClickListener {
    fun onClick(item: Issue)
}