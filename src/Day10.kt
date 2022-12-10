import kotlin.math.absoluteValue

fun main() {
    fun parseInput(input: List<String>): List<Int> {
        return input.map { it.split(" ") }
            .flatMap {
                when (it.first()) {
                    "noop" -> listOf(0)
                    "addx" -> listOf(0, it[1].toInt())
                    else -> emptyList()
                }
            }
            .let { listOf(0) + it } // fix initial offset, as we only represent the result of a cycle
            .let { list ->
                var currentRegistrarValue = 1
                list.map {
                    currentRegistrarValue += it
                    currentRegistrarValue
                }
            }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input)
            .let { list -> (20 until list.size step 40).map { list[it - 1] * it } }
            .sum()
    }

    fun part2(input: List<String>): Int {
        parseInput(input)
            .dropLast(1) // drop post-last, as we don't care about that
            .chunked(40)
            .map { ints ->
                ints.mapIndexed { index, registrar -> (index - registrar).absoluteValue < 2 }
                    .joinToString("") { if (it) "#" else "." }
            }
            .onEach { line -> println(line) }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    check(part1(testInput) == 13140)
    println(part1(input))

    check(part2(testInput) == 0)
    println()
    part2(input)
}