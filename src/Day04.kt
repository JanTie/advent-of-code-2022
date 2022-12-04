fun main() {
    fun parseInput(input: List<String>) = input.map { elfPair ->
        elfPair.split(",", "-")
            .chunked(2)
            .map { it[0].toInt()..it[1].toInt() }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input)
            .filter {
                it[0].all { section -> it[1].contains(section) } || it[1].all { section -> it[0].contains(section) }
            }
            .size
    }

    fun part2(input: List<String>): Int {
        return parseInput(input)
            .filter {
                it[0].any { section -> it[1].contains(section) } || it[1].any { section -> it[0].contains(section) }
            }
            .size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 2)
    println(part1(input))

    check(part2(testInput) == 4)
    println(part2(input))
}