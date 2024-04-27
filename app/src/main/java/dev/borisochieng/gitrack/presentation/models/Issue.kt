package dev.borisochieng.gitrack.presentation.models

data class Issue(
    val issueTitle: String,
    val issueStatus: String,
    val openedAt: String,
    val author: String,
    val commentCount: Int,
    val number: Int,
    val labels: Set<String>
)
