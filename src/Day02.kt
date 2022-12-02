import kotlin.math.absoluteValue

enum class Symbol(val points: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun resultAgainst(other: Symbol): Result {
        return if (other == this) Result.DRAW
        else if (this.ordinal > other.ordinal) {
            // this ordinal is higher
            if ((this.ordinal - other.ordinal).absoluteValue > 1) Result.LOSS
            else Result.WIN
        } else {
            // opponents ordinal is higher
            if ((this.ordinal - other.ordinal).absoluteValue > 1) Result.WIN
            else Result.LOSS
        }
    }
}

enum class Result(val points: Int) {
    LOSS(0),
    DRAW(3),
    WIN(6),
}

data class Play(
    val opponentSymbol: Symbol,
    val mySymbol: Symbol,
) {
    val result: Result = mySymbol.resultAgainst(opponentSymbol)

    val myPoints: Int = mySymbol.points + result.points

    override fun toString(): String {
        return "Play(opponentSymbol=$opponentSymbol, mySymbol=$mySymbol, result=$result, myPoints=$myPoints)"
    }
}

fun main() {
    fun <T> parseInput(input: List<String>, secondValueParser: (String) -> T?) = input.map { it.split(" ") }
        .mapNotNull {
            val opponentSymbol = when (it[0]) {
                "A" -> Symbol.ROCK
                "B" -> Symbol.PAPER
                "C" -> Symbol.SCISSORS
                else -> return@mapNotNull null // parsing error
            }

            return@mapNotNull opponentSymbol to (secondValueParser(it[1]) ?: error("Parsing error"))
        }

    fun part1(input: List<String>): Int {
        return parseInput(input) {
            when (it) {
                "X" -> Symbol.ROCK
                "Y" -> Symbol.PAPER
                "Z" -> Symbol.SCISSORS
                else -> null // parsing error
            }
        }
            .map { (opponentSymbol, mySymbol) -> Play(opponentSymbol, mySymbol) }
            .sumOf { it.myPoints }
    }

    fun part2(input: List<String>): Int {
        return parseInput(input) {
            when (it) {
                "X" -> Result.LOSS
                "Y" -> Result.DRAW
                "Z" -> Result.WIN
                else -> null // parsing error
            }
        }.map { (opponentSymbol, desiredResult) ->
            Play(opponentSymbol, Symbol.values().first { it.resultAgainst(opponentSymbol) == desiredResult })
        }
            .sumOf { it.myPoints }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")

    println(part1(input))
    println(part2(input))
}