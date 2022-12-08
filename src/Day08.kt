data class Tree(
        val height: Int,
        val allToTheLeft: List<Int>,
        val allToTheTop: List<Int>,
        val allToTheRight: List<Int>,
        val allToTheBottom: List<Int>,
) {
    val isVisible: Boolean
        get() = listOf(allToTheLeft, allToTheTop, allToTheRight, allToTheBottom)
                .any { direction -> direction.all { it < height } || direction.isEmpty() }

    val scenicScore: Int
        get() = listOf(allToTheLeft, allToTheTop, allToTheRight, allToTheBottom)
                .map { direction ->
                    direction.indexOfFirst { it >= height }.takeIf { it != -1 }?.let { it + 1 } ?: direction.size
                }
                .fold(1) { currentScore, direction -> currentScore * direction }
}

fun main() {
    fun parseInput(input: List<String>): List<Tree> {
        val map = input.map { it.toCharArray().map { it.digitToInt() } }

        val mapHeight = map.size
        val mapWidth = map[0].size

        // A kiss right on the eye
        return (0 until mapWidth).map { x ->
            (0 until mapHeight).map { y ->
                Tree(
                        map[y][x],
                        allToTheLeft = (0 until x).reversed().map { readX -> map[y][readX] },
                        allToTheRight = (x + 1 until mapWidth).map { readX -> map[y][readX] },
                        allToTheTop = (0 until y).reversed().map { readY -> map[readY][x] },
                        allToTheBottom = (y + 1 until mapHeight).map { readY -> map[readY][x] },
                )
            }
        }.flatten()
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).filter { it.isVisible }.size
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).maxOf { it.scenicScore }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")

    println(part1(input))
    println(part2(input))
}
