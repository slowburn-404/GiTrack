package dev.borisochieng.gitrack.ui.models

data class Issue(
    val issueTitle : String,
    val issueStatus : String,
    val openedAt : String,
    val username : String,
    val commentCount : Int,
    val number: Int,
    val labels: List<String>
)
