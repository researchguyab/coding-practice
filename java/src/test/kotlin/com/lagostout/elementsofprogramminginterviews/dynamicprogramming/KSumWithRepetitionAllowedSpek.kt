package com.lagostout.elementsofprogramminginterviews.dynamicprogramming

import com.lagostout.common.nextInt
import com.lagostout.elementsofprogramminginterviews.greedyalgorithmsandinvariants.canPickKNumbersThatAddUpToSumWithRepetitionAllowed
import org.apache.commons.math3.random.RandomDataGenerator
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.paukov.combinatorics3.Generator
import java.util.*
import kotlin.streams.toList

class KSumWithRepetitionAllowedSpek : Spek({
    describe("permutationsWithRepetition") {
        fun bruteForceSolution(list: List<Int>, k: Int, sum: Int): Boolean {
            return Generator.combination(list).multi(k).stream()
                    .filter { it.sum() == sum }.toList().also {
                println(it)
            }.isNotEmpty()
        }
        val random = Random(1)
        @Suppress("NAME_SHADOWING")
        val randomData = {
            val cases = mutableListOf<Triple<List<Int>, Int, Int>>()
            val random = RandomDataGenerator().apply { reSeed(1) }
            val caseCount = 1000
            val numberCountRange = (0..5)
            val numberRange = (-10..10)
            val sumRange = (-50..50)
            val kRange = (0..10)
            (0..caseCount).forEach {
                val numbers = mutableSetOf<Int>().apply {
                    while (size < random.nextInt(numberCountRange)) {
                        add(random.nextInt(numberRange))
                    }
                }
                cases.add(Triple(numbers.toList(), random.nextInt(kRange),
                        random.nextInt(sumRange)))
            }
            cases
        }()
        val data = (
//                randomData +
                listOfNotNull(
//                Triple(listOf(-9,-7,6,9), 6, 10),  // 10 = [[-7, -7, 6, 6, 6, 6]]
//                Triple(listOf(-9,-7,6,9), 4, 11),
//                Triple(listOf(-9,-7,6,9), 4, -2),  // -2 = [[-7, -7, 6, 6]]
                Triple(listOf(-9,-7,6,9), 4, -2),  // -2 = [[-7, -7, 6, 6]]
//                Triple(listOf(-9,-7,6,9), 2, -14),
//                Triple(listOf(-9,-7,6,9), 2, -1),
//                Triple(listOf(1), 3, -34),
//                Triple(listOf(1), 0, 3),
//                Triple(listOf(1), 1, 1),
//                Triple(listOf(1), 1, 0),
//                Triple(listOf(1), 3, 3),
//                Triple(listOf(2), 3, 6),
//                Triple(listOf(2,3), 3, 6),
//                Triple(listOf(1,2), 6, 6),
//                Triple(listOf(1,2), 0, 2),
//                Triple(listOf(1,2), 0, 3),
//                Triple(listOf(1,2), 1, 1),
//                Triple(listOf(1,2), 1, 2),
//                Triple(listOf(1,2), 1, 3),
//                Triple(listOf(1,2), 1, 0),
//                Triple(listOf(1,2), 2, 3),
//                Triple(listOf(1,2), 2, 4),
//                Triple(listOf(1,2,3), 2, 3),
//                Triple(listOf(1,2,3,4), 2, 5),
//                Triple(listOf(1,2,4,5), 2, 6),
//                Triple(listOf(1,2,4,5), 6, 12),
                null)).map { (list, k, sum) ->
            data(list.shuffled(random), k, sum,
                    bruteForceSolution(list, k, sum))
        }.toTypedArray()
        on("list: %s, k: %s, sum: %s", with = *data) {
            list: List<Int>, k: Int, sum: Int, expected: Boolean ->
            it("returns $expected") {
                assertThat(canPickKNumbersThatAddUpToSumWithRepetitionAllowed(
                                list, k, sum)).isEqualTo(expected)
            }
        }
    }
})
