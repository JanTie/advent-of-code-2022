import kotlin.streams.toList

fun main() {
    fun parseInput(input: List<String>) = input
        .filter { it.isNotEmpty() }
        .map { listOf(it.substring(0, it.length / 2), it.substring(it.length / 2)) }
        .map { rucksack ->
            rucksack.map { compartment ->
                compartment.chars().toList().map { if (it >= 97) it - 97 + 1 else it - 65 + 27 }
            }
        }

    fun part1(input: List<String>): Int {
        return parseInput(input).sumOf { rucksack -> rucksack[0].first { it in rucksack[1] } }
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).let { list ->
            list.chunked(3) // list / group / rucksack / compartment / item
                .map { group -> group.map { rucksack -> rucksack.flatten() } } // list / group / rucksack / item
                .map { group ->
                    group.flatten().distinct().first { group.all { rucksack -> rucksack.contains(it) } }
            }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    check(part1(testInput) == 157)
    println(part1(input))

    check(part2(testInput) == 70)
    println(part2(input))
}