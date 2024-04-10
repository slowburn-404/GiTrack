package dev.borisochieng.gitrack.ui.models

import java.time.LocalDate

data class Repository(
    val databaseId: Int,
    val name: String,
    val owner: String,
    val desc: String?,
    val starCount: Int?,
    val issueCount: Int?,
    val labels: List<String>?
)