package dev.borisochieng.gitrack.ui.models

data class Repository(
    val id: String,
    val title: String,
    val desc: String?,
    val starCount: Int?,
    val issueCount: Int?,
    val labels: List<String>?
)