package dev.borisochieng.gitrack.ui.models

data class Issue(
    val databaseId: Int,
    val issueTitle : String,
    val issueStatus : String,
    val openedAt : String,
    val author : String,
    val commentCount : Int,
    val number: Int,
    val labels: List<String>
)
