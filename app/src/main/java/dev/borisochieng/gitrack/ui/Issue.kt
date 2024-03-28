package dev.borisochieng.gitrack.ui

data class Issue(
    val id : Int,
    val issueTitle : String,
    val issueStatus : String,
    val openedAt : String,
    val username : String,
    val commentCount : String
)
