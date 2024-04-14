package dev.borisochieng.gitrack.ui.adapters

fun interface SetRecyclerViewItemClickListener<T> {
    fun setOnItemClickListener(item: T)

}