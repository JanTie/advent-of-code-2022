fun main() {
    fun foldInput(input: List<String>) = input
        .foldRight(mutableListOf<Int>() to 0) { new, (currentList, currentCount) ->
            if (new.isNotBlank()) {
                // new input is not a blank line increase current count
                currentList to currentCount + new.toInt()
            } else {
                // add to list, set current count to 0
                // use mutableList and simply add to prevent new lists from being created
                currentList.add(currentCount)
                currentList to 0
            }
        }
        .first

    fun part1(input: List<String>): Int {
        return foldInput(input).maxOf { it }
    }

    fun part2(input: List<String>): Int {
        val output = mutableListOf<Int>()
        foldInput(input).forEach { calories ->
            if (output.isEmpty()) {
                // initial
                output.add(calories)
                return@forEach
            }

            output.indexOfLast { currentEntry -> calories >= currentEntry }
                .let { index ->
                    // if new value is larger than a current value, add it to that index
                    if (index != -1) index
                    // if maxsize is not reached yet, and no index is found, add to front of list
                    else if (output.size < MAX_SIZE) 0
                    // otherwise ignore value
                    else null
                }
                ?.let { index ->
                    output.add(index + 1, calories)
                    if (output.size > MAX_SIZE) {
                        output.removeAt(0)
                    }
                }
        }
        return output.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")

    println(part1(input))
    println(part2(input))
}

private const val MAX_SIZE = 3