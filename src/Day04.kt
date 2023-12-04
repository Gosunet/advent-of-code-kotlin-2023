import kotlin.math.pow

fun main() {
    fun parse(input: List<String>) = input.withIndex().map { (index, line) ->
        line.substringAfter(":").replace("  ", " ").split(" | ")
            .let { (winningNumbersString, playedNumbersString) ->
                val playedNumbers = winningNumbersString.trimStart().split(" ").map(String::toInt)
                val winningsNumbers = playedNumbersString.trimStart().split(" ").map(String::toInt)
                Card(index, playedNumbers, winningsNumbers)
            }
    }

    fun part1(input: List<String>): Int = parse(input).map(Card::points).sum()

    fun part2(input: List<String>): Int {
        val allCards = parse(input)
        return allCards.sumOf { card ->
            for (i in 1..card.winningCount) {
                val wonCardIndex = card.id + i
                if (wonCardIndex < allCards.size) {
                    allCards[wonCardIndex].cardCount += card.cardCount
                }
            }
            card.cardCount
        }
    }

    val testInput = readInput("day_04_test")
    val testActual = part1(testInput)
    check(testActual == 13) { "Expected 13, got $testActual" }

    val testActual2 = part2(testInput)
    check(testActual2 == 30) { "Expected 30, got $testActual2" }

    part1(readInput("day04")).println()
    part2(readInput("day04")).println()
}

data class Card(val id: Int, val numbers: List<Int>, val winningsNumber: List<Int>, var cardCount: Int = 1) {
    val winningCount: Int
        get() = numbers.count { it in winningsNumber }

    val points: Int
        get() = 2.0.pow(winningCount - 1).toInt()
}