import kotlin.math.absoluteValue

fun main() {
    fun parseInput(input: List<String>, amountOfNodes: Int): List<List<Point>> {
        val initial = listOf(List(amountOfNodes) { Point(0, 0) })
        return input.flatMap {
            val (direction, moves) = it.split(" ")
            List(moves.toInt()) { direction }
        }.fold(initial) { listMutations, direction ->
            val currentList = listMutations.last()
            listMutations + listOf(
                currentList.mapIndexed { index, point ->
                    if (index == 0) {
                        // move head
                        return@mapIndexed when (direction) {
                            "L" -> point.copy(x = point.x - 1)
                            "U" -> point.copy(y = point.y + 1)
                            "R" -> point.copy(x = point.x + 1)
                            "D" -> point.copy(y = point.y - 1)
                            else -> point
                        }
                    } else {
                        return@mapIndexed point.movedTowards(currentList[index - 1])
                    }
                }
            )
        }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input, 2).map { it.last() }.distinct().size
    }

    fun part2(input: List<String>): Int {
        return parseInput(input, 10).map { it.last() }.distinct().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 1)
    println(part2(input))
}

data class Point(
    val x: Int,
    val y: Int,
) {
    fun movedTowards(other: Point): Point {
        return when {
            (x - other.x).absoluteValue + (y - other.y).absoluteValue > 2 -> {
                this.copy(
                    x = x + if ((other.x - x).isPositive) 1 else -1,
                    y = y + if ((other.y - y).isPositive) 1 else -1,
                )
            }

            (x - other.x).absoluteValue > 1 -> {
                this.copy(
                    x = x + (other.x - x) / 2,
                )
            }

            (y - other.y).absoluteValue > 1 -> {
                this.copy(
                    y = y + (other.y - y) / 2,
                )
            }

            else -> this
        }
    }
}

private val Int.isPositive: Boolean
    get() = this > 0
