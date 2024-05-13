package dev.borisochieng.gitrack.domain.models

data class Issue(
    val issueTitle: String,
    val issueStatus: String,
    val openedAt: String,
    val author: String,
    val commentCount: Int,
    val number: Int,
    val labels: Set<String>
)
