package com.lagostout.datastructures

import com.lagostout.common.MultilineShortPrefixRecursiveToStringStyle
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import java.util.*

open class BinaryTreeNode<T>(var parent: BinaryTreeNode<T>? = null,
                             var left: BinaryTreeNode<T>? = null,
                             var right: BinaryTreeNode<T>? = null,
                             val value: T) {

    val id = nextId

    val isRoot: Boolean
        get() = parent == null

    val isALeaf: Boolean
        get() = (left ?: right) == null

    val isNotALeaf: Boolean
        get() = !isALeaf

    val hasRight: Boolean
        get() = right != null

    val hasLeft: Boolean
        get() = left != null

    val children: Set<BinaryTreeNode<T>>
        get() = listOfNotNull(left, right).toSet()

    override fun hashCode(): Int = HashCodeBuilder().append(id).toHashCode()

    override fun equals(other: Any?): Boolean = when {
        other !is BinaryTreeNode<*> -> false
        this === other -> true
        else -> EqualsBuilder().append(id, other.id).isEquals ||
                (value == other.value && left == other.left && right == other.right)
    }

    override fun toString(): String {
        return ReflectionToStringBuilder(this,
                MultilineShortPrefixRecursiveToStringStyle()).toString()
    }

    companion object {

        private var _id = 0

        val nextId
            get() = _id++

        fun <T : Comparable<T>> toRawBinaryTreeNodes(
                rawTree: List<List<Any>>): List<RawBinaryTreeNode<T>> {
            return rawTree.map {
                RawBinaryTreeNode(it[0] as Int?, it[1] as Int?,
                        (if (it.size == 4) it[2] else null) as Int?, it[3] as T)
            }
        }

        fun <T : Comparable<T>> buildBinaryTrees(
                rawTree: List<List<Any>>,
                tree: MutableMap<Int, BinaryTreeNode<T>>) {
            rawTree.forEachIndexed { index, _ ->
                tree[index] ?: run {
                    buildBinaryTree(index, toRawBinaryTreeNodes(rawTree), tree)
                }
            }
        }

        /**
         * Constructs a binary tree.
         * @return A [Pair] of the tree's root and a list of all the tree's nodes.
         */
        fun <T : Comparable<T>> buildBinaryTree(
                rawTree: List<RawBinaryTreeNode<T>>):
                Pair<BinaryTreeNode<T>?, List<BinaryTreeNode<T>>> {
            val nodes = mutableMapOf<Int, BinaryTreeNode<T>>()
            buildBinaryTree(0, rawTree, nodes)
            nodes.values.forEach { parent ->
                listOfNotNull(parent.left, parent.right).forEach { childNode ->
                    childNode.parent ?: run { childNode.parent = parent }
                }
            }
            return Pair(nodes[0], nodes.toSortedMap().values.toList())
        }

        fun <T : Comparable<T>> bbtr (rawNodes: List<RawBinaryTreeNode<T>>): BinaryTreeNode<T>? {
            return BinaryTreeNode.buildBinaryTree(rawNodes).first
        }

        fun <T : Comparable<T>> buildBinaryTreeRoot(
                rawTree: List<RawBinaryTreeNode<T>>): BinaryTreeNode<T> {
            return BinaryTreeNode.buildBinaryTree(rawTree).first!!
        }

        fun <T : Comparable<T>> buildBinaryTree(
                rawTree: List<List<Any>>, tree: MutableMap<Int, BinaryTreeNode<T>>) {
            buildBinaryTree(0, toRawBinaryTreeNodes(rawTree), tree)
        }

        fun <T : Comparable<T>> buildBinaryTree(
                rootNodeIndex: Int,
                rawTree: List<RawBinaryTreeNode<T>>,
                nodes: MutableMap<Int, BinaryTreeNode<T>>) {
            if (rawTree.isEmpty()) return
            val rawNode = rawTree[rootNodeIndex]
            val node = BinaryTreeNode(value = rawNode.value)
            rawNode.parentIndex?.let {
                // Configure parent
                val parentNode = nodes[it]
                node.parent = parentNode
            }
            nodes[rootNodeIndex] = node
            val leftChildIndex = rawNode.leftChildIndex
            if (leftChildIndex != null) {
                buildBinaryTree(leftChildIndex, rawTree, nodes)
                node.left = nodes[leftChildIndex]
            }
            val rightChildIndex = rawNode.rightChildIndex
            if (rightChildIndex != null) {
                buildBinaryTree(rightChildIndex, rawTree, nodes)
                node.right = nodes[rightChildIndex]
            }
        }

        private fun <T> stringify(node: BinaryTreeNode<T>?): String {
            return node?.run {
                "BinaryTreeNode(value=$value)"
            } ?: "null"
        }

        fun <T> toList(root: BinaryTreeNode<T>?): List<BinaryTreeNode<T>>{
            if (root == null) return emptyList()
            val stack = LinkedList<List<BinaryTreeNode<T>>>()
            val nodes = mutableListOf<BinaryTreeNode<T>>()
            // Breadth-first traversal
            stack.push(listOf(root))
            while (!stack.isEmpty()) {
                val levelNodes = stack.pop()
                nodes.addAll(levelNodes)
                val nextLevelNodes = mutableListOf<BinaryTreeNode<T>>()
                levelNodes.forEach {
                    listOf(it.left, it.right).filterNotNull().forEach {
                        nextLevelNodes.add(it)
                    }
                }
                if (nextLevelNodes.isNotEmpty())
                    stack.push(nextLevelNodes)
            }
            return nodes
        }

    }
}
