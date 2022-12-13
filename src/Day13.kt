fun main() {
    fun parseString(input: String): List<*> {
        val mutableList = mutableListOf<Any>()

        fun listAtDepth(depth: Int): MutableList<Any> {
            var list = mutableList
            repeat((0 until depth).count()) {
                list = list.last() as MutableList<Any>
            }
            return list
        }

        var currentDepth = 0

        var currentString: String? = null
        input.forEach { char ->
            when (char) {
                '[' -> listAtDepth(currentDepth).add(mutableListOf<Any>()).also { currentDepth++ }
                ']' -> {
                    currentString?.toInt()?.let {
                        listAtDepth(currentDepth).add(it)
                        currentString = null
                    }
                    currentDepth--
                }

                ',' -> currentString?.toInt()?.let {
                    listAtDepth(currentDepth).add(it)
                    currentString = null
                }

                else -> {
                    if (currentString == null) {
                        currentString = char.toString()
                    } else {
                        currentString += char
                    }
                }
            }
        }
        return mutableList[0] as List<*>
    }

    fun parseInput(input: List<String>): List<List<*>> = input
        .filter { it.isNotEmpty() }
        .chunked(2)
        .map { it.map { parseString(it) } }

    fun isValid(left: List<*>, right: List<*>, currentIndex: Int = 0): Boolean? {
        val currentLeft = left.getOrNull(currentIndex)
        val currentRight = right.getOrNull(currentIndex)

        if (currentLeft == null && currentRight == null) {
            return null
        }
        if (currentLeft == null) {
            return true
        }
        if (currentRight == null) {
            return false
        }

        println("$currentLeft | $currentRight")
        println()
        if (currentLeft is Int && currentRight is Int) {
            if (currentLeft > currentRight) return false
            if (currentLeft < currentRight) return true
        } else if (currentLeft is List<*> && currentRight is List<*>) {
            isValid(currentLeft, currentRight)?.let { return it }
        } else {
            if (currentLeft is List<*> && currentRight is Int) {
                isValid(currentLeft, listOf(currentRight))?.let { return it }
            } else if (currentLeft is Int && currentRight is List<*>) {
                isValid(listOf(currentLeft), currentRight)?.let { return it }
            }
        }
        return isValid(left.drop(1), right.drop(1))
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).mapIndexed { index, (l, r) ->
            val left = l as List<*>
            val right = r as List<*>

            return@mapIndexed if (isValid(left, right) == true) index + 1 else 0
        }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return parseInput(input)
            .flatMap { (l, r) -> listOf(l as List<*>, r as List<*>) }
            .sortedWith(Comparator { thiz, other ->
                val result = isValid(thiz, other)
                if (result == true) return@Comparator -1
                if (result == false) return@Comparator 1
                return@Comparator 0
            })
            .let {
                println(it)
                val first = it.indexOfFirst { it == listOf(listOf(2)) } + 1
                val second = it.indexOfFirst { it == listOf(listOf(6)) } + 1

                println("first: $first")
                println("second: $second")

                first * second
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    val input = readInput("Day13")

    println(part1(testInput))
    //check(part1(testInput) == 13)
    println(part1(input))

    println(part2(testInput))
    check(part2(testInput) == 140)
    println(part2(input))
}
