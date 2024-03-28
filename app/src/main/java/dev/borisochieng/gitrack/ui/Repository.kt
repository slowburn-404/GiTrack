package dev.borisochieng.gitrack.ui

data class Repository(
    val id: Int,
    val title: String,
    val desc: String,
    val starCount: Long,
    val issueCount: Long,
    val lastUpdated: String
)
