package dev.borisochieng.gitrack.presentation.adapters

fun interface SetRecyclerViewItemClickListener<T> {
    fun setOnItemClickListener(item: T)

}