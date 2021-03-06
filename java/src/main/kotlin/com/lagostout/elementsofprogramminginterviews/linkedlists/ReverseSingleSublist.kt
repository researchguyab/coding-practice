package com.lagostout.elementsofprogramminginterviews.linkedlists

/**
 * Problem 8.2.1 page 116
 */
object ReverseSingleSublist {

    // 1-based start and end
    @Suppress("NAME_SHADOWING")
    fun <T> reverseSingleSublist(list: LinkedListNode<T>, start: Int, end: Int): LinkedListNode<T> {
        if (start == end) return list

        var start = start
        var end = end

        var lastNode = list
        var listSize = 1
        while (true) {
            lastNode.next?.let {
                ++listSize
                lastNode = it
            } ?: break
        }
        listOf(start, end).find { it < 1 || it > listSize } ?.let {
            throw IllegalArgumentException("Start and end positions must be >= 1 and <= list size")
        }
        if (listSize == 1) return list

        if (start > end) {
            val temp = start
            start = end
            end = temp
        }

        var nodeBeforeSublistStart: LinkedListNode<T>? = null
        var sublistEnd: LinkedListNode<T>? = null
        var currentPosition = 0
        val head = LinkedListNode(next = list)
        var currentNode = head
        while (true) {
            if (currentPosition + 1 == start)
                nodeBeforeSublistStart = currentNode
            else if (currentPosition == end) {
                sublistEnd = currentNode
                break
            }
            currentNode.next?.apply {
                currentNode = this
                ++currentPosition
            } ?: break
        }

        while (nodeBeforeSublistStart?.next !== sublistEnd) {
            nodeBeforeSublistStart?.next?.let {
                nodeBeforeSublistStart?.next = it.next
                sublistEnd?.apply {
                    it.next = next
                    next = it
                }
            }
        }

        return head.next!!
    }
}