package dev.borisochieng.gitrack.ui.models

import android.content.res.ColorStateList

data class Issue(
    val issueTitle: String,
    val issueStatus: String,
    val openedAt: String,
    val author: String,
    val commentCount: Int,
    val number: Int,
    val labels: Set<String>
)
