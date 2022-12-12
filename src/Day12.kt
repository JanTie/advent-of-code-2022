fun main() {
    fun parseInput(input: List<String>): List<List<Tile>> = input
        .map { line -> line.map { it } }
        .let { map ->
            map.mapIndexed { rowIndex, row ->
                row.mapIndexed { index, char ->
                    Tile(
                        x = index,
                        y = rowIndex,
                        char = char,
                        isStart = char == 'S',
                        isEnd = char == 'E',
                    )
                }
            }
        }

    fun grow(
        initialTile: Tile,
        grid: List<List<Tile>>,
    ): Pair<Int, Boolean> {
        var currentPaths = listOf(initialTile)
        var iteration = 0
        val seenTiles = mutableSetOf<Tile>()

        while (!currentPaths.any { it.isEnd } && currentPaths.isNotEmpty()) {
            currentPaths = currentPaths
                .flatMap { visited ->
                    seenTiles.add(visited)

                    val tileToLeft = if (visited.x != 0) grid[visited.y][visited.x - 1] else null
                    val tileToTop = if (visited.y != 0) grid[visited.y - 1][visited.x] else null
                    val tileToRight = if (visited.x != grid[0].lastIndex) grid[visited.y][visited.x + 1] else null
                    val tileToBottom = if (visited.y != grid.lastIndex) grid[visited.y + 1][visited.x] else null

                    return@flatMap listOfNotNull(tileToLeft, tileToTop, tileToRight, tileToBottom)
                        .filter { (it.height - visited.height) <= 1 } // legit to go there
                }
                .distinct()
                .filter { it !in seenTiles }
            iteration++
        }

        return iteration to currentPaths.any { it.isEnd }
    }

    fun part1(input: List<String>): Int {
        val grid = parseInput(input)

        return grow(grid.flatten().first { it.isStart }, grid).first
    }

    fun part2(input: List<String>): Int {
        val grid = parseInput(input)
        return grid.flatten().filter { it.char == 'a' }
            .map { grow(it, grid) }
            .filter { it.second }
            .minOf { it.first }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    val input = readInput("Day12")

    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}

private data class Tile(
    val x: Int,
    val y: Int,
    val char: Char,
    val isStart: Boolean,
    val isEnd: Boolean,
) {
    val height = when (char) {
        'S' -> 'a'
        'E' -> 'z'
        else -> char
    }.code
}
