fun main() {
    fun parseInput(input: List<String>): List<Monkey> = input.chunked(7)
        .mapIndexed { index, it ->
            val items = it[1].split(": ")[1].split(", ").map { it.toLong() }
            val operation = it[2].split(": ")[1]
            val test = it[3].split(": ")[1]
            val ifTrue = it[4].split(": ")[1]
            val ifFalse = it[5].split(": ")[1]

            Monkey(
                id = index,
                items = items,
                roundTestDivisible = test.split(" ").last().toLong(),
                targetMonkeyIfTrue = ifTrue.split(" ").last().toInt(),
                targetMonkeyIfFalse = ifFalse.split(" ").last().toInt(),
                operation = {
                    val left = when (val l = operation.split(" ")[2]) {
                        "old" -> it
                        else -> l.toLong()
                    }
                    val right = when (val r = operation.split(" ")[4]) {
                        "old" -> it
                        else -> r.toLong()
                    }
                    when (operation.split(" ")[3].toCharArray().first()) {
                        '+' -> left.plus(right)
                        '*' -> left.times(right)
                        else -> left.plus(right)
                    }
                }
            )
        }

    fun simulateRounds(input: List<String>, amountOfRounds: Int, worryLevelDecrease: (Long) -> Long): List<Int> {
        var monkeys = parseInput(input)
        // calculate greatest common divisor, to decrease the numbers
        val modulus = monkeys.map { it.roundTestDivisible }.fold(1, Long::times)
        return (0 until amountOfRounds).map {
            // create new monkey list based on the previous one
            val newMonkeys = monkeys.map { it.copy() }
            newMonkeys.map { monkey ->
                val itemCountAtStart = monkey.items.size
                monkey.items
                    .map { monkey.operation(it) }
                    .map { worryLevelDecrease(it) % modulus }
                    .forEach {
                        if (it % monkey.roundTestDivisible == 0L) {
                            newMonkeys[monkey.targetMonkeyIfTrue].items += it
                        } else {
                            newMonkeys[monkey.targetMonkeyIfFalse].items += it
                        }
                        monkey.items = monkey.items.drop(1)
                    }
                monkey to itemCountAtStart
            }.also { monkeys = it.map { pair -> pair.first } }
        }
            .let { rounds ->
                // map by items held by each monkey
                (0 until rounds.first().size).map { index -> rounds.sumOf { round -> round[index].second } }
            }
            .sorted()
    }

    fun part1(input: List<String>): Int {
        return simulateRounds(input, 20) { it / 3 }
            .takeLast(2)
            .fold(1, Int::times)
    }

    fun part2(input: List<String>): Long {
        return simulateRounds(input, 10000) { it }
            .takeLast(2)
            .fold(1, Long::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    check(part1(testInput) == 10605)
    println(part1(input))

    check(part2(testInput) == 2713310158)
    println(part2(input))
}

data class Monkey(
    val id: Int,
    var items: List<Long>,
    val roundTestDivisible: Long,
    val targetMonkeyIfTrue: Int,
    val targetMonkeyIfFalse: Int,
    val operation: (old: Long) -> Long,
)