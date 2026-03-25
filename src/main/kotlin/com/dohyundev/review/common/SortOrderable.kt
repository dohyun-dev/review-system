package com.dohyundev.review.common

interface SortOrderable<T> : Comparable<T> where T : SortOrderable<T> {
    var sortOrder: Int

    override fun compareTo(other: T): Int {
        return this.sortOrder.compareTo(other.sortOrder)
    }

    fun swapOrder(other: T) {
        val tmp = this.sortOrder
        this.sortOrder = other.sortOrder
        other.sortOrder = tmp
    }
}
