fun main() {
    fun String.findMarkerIndex(length: Int) = this
        .windowed(length)
        .indexOfFirst {
            it.toList().distinct().size == length
        } + length // add length because we skipped length indices due to window

    fun part1(input: List<String>): Int {
        return input[0].findMarkerIndex(4)
    }

    fun part2(input: List<String>): Int {
        return input[0].findMarkerIndex(14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    check(part1(testInput) == 7)
    println(part1(input))

    check(part2(testInput) == 19)
    println(part2(input))
}
