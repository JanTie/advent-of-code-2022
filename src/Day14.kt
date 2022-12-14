fun main() {

    fun Pair<Int, Int>.bottom() = this.copy(second = second + 1)
    fun Pair<Int, Int>.bottomLeft() = this.copy(first = first - 1, second = second + 1)
    fun Pair<Int, Int>.bottomRight() = this.copy(first = first + 1, second = second + 1)
    fun Pair<Int, Int>.move(blockedFields: Set<Pair<Int, Int>>) =
        listOf(bottom(), bottomLeft(), bottomRight()).firstOrNull { !blockedFields.contains(it) }

    fun parseInput(input: List<String>) = input.flatMap {
        it.split(" -> ")
            .windowed(2)
            .map {
                it.map { it.split(",").map { it.toInt() } }.map { (x, y) -> x to y }
            }.flatMap {
                val (first, second) = it
                val (firstX, firstY) = first
                val (secondX, secondY) = second

                if (firstX != secondX) {
                    if (firstX < secondX) {
                        (firstX..secondX)
                    } else {
                        (firstX downTo secondX)
                    }.map { x -> x to firstY }
                } else {
                    if (firstY < secondY) {
                        (firstY..secondY)
                    } else {
                        (firstY downTo secondY)
                    }.map { y -> firstX to y }
                }
            }
    }
        .distinct()

    fun part1(input: List<String>): Int {
        val blockedFields = parseInput(input)

        val maxY = blockedFields.maxOf { it.second }
        val startField = 500 to 0
        val fields = blockedFields.toMutableSet()
        val initialSize = fields.size

        var currentField = startField
        while (currentField.second < maxY) {
            val target = currentField.move(fields)
            if (target != null) {
                currentField = target
                continue
            } else {
                fields.add(currentField)
                currentField = startField
            }
        }

        return fields.size - initialSize
    }

    fun part2(input: List<String>): Int {
        val blockedFields = parseInput(input)

        val maxY = blockedFields.maxOf { it.second } + 2
        val startField = 500 to 0
        val fields = blockedFields.toMutableSet()
        val initialSize = fields.size

        var currentField = startField
        while (startField.move(fields) != null) {
            val target = currentField.move(fields)
            if (target != null && target.second < maxY) {
                currentField = target
                continue
            } else {
                fields.add(currentField)
                currentField = startField
            }
        }

        return fields.size - initialSize + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    val input = readInput("Day14")
    check(part1(testInput) == 24)
    println(part1(input))
    check(part2(testInput) == 93)
    println(part2(input))
}