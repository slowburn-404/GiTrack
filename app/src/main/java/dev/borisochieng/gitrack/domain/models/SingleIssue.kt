package dev.borisochieng.gitrack.domain.models

data class SingleIssue(
    val title: String,
    val createdAt: String,
    val status: String,
    val description: String,
    val author: String,
    val labels: List<String>
)