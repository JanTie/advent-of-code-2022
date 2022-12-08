fun main() {
    fun executeInstructions(
        input: List<String>,
        transactionHandling: (map: MutableMap<Int, List<Char>>, amount: Int, from: Int, to: Int) -> Unit,
    ): Map<Int, List<Char>> {
        val dividerIndex = input.indexOfFirst { it.isBlank() }
        val map = input.subList(0, dividerIndex - 1)
        val indices = input.subList(dividerIndex - 1, dividerIndex)[0]

        val mutableMap = indices
            .filter { !it.isWhitespace() }
            .map { indices.indexOf(it) }
            .associate { index ->
                indices[index].digitToInt() to map.reversed()
                    .mapNotNull { it.getOrNull(index)?.takeIf { !it.isWhitespace() } }
            }
            .toMutableMap()

        val instructions = input.subList(dividerIndex + 1, input.size)
            .map {
                it.split("move ", " from ", " to ")
                    .mapNotNull { it.toIntOrNull() }
            }

        instructions.forEach { (amount, from, to) ->
            transactionHandling(mutableMap, amount, from, to)

        }

        return mutableMap
    }

    fun part1(input: List<String>): String {
        return executeInstructions(input) { map, amount, from, to ->
            (0 until amount).forEach {
                val char: Char = map[from]!!.last()
                map[from] = map[from]!!.dropLast(1)
                map[to] = map[to]!! + char
            }
        }
            .map { it.value.last() }
            .joinToString("")
    }

    fun part2(input: List<String>): String {
        return executeInstructions(input) { map, amount, from, to ->
            val chars: List<Char> = map[from]!!.takeLast(amount)
            map[from] = map[from]!!.dropLast(amount)
            map[to] = map[to]!! + chars
        }
            .map { it.value.last() }
            .joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    check(part1(testInput) == "CMZ")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}