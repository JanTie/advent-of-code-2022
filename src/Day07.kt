fun main() {
    fun buildFileTree(input: List<String>): Map<String, Int> {
        var currentDir = ""
        val map = mutableMapOf<String, Int>()
        input.forEach {
            when {
                it.startsWith("\$ cd") -> {
                    val param = it.split(" ")[2]
                    currentDir = when {
                        param.startsWith("/") -> param
                        param.startsWith("..") -> currentDir.substring(0, currentDir.lastIndexOf("/"))
                        else -> ("$currentDir/$param").replace("//","/")
                    }
                }
                it.startsWith("\$ ls") -> {
                    if (!map.containsKey(currentDir)) {
                        map[currentDir] = 0
                    }
                }
                it.startsWith("dir") -> Unit // no-op
                else -> {
                    val (size) = it.split(" ")
                    map[currentDir] = map[currentDir]!! + size.toInt()
                }
            }
        }
        return map
    }

    fun part1(input: List<String>): Int {
        val map = buildFileTree(input)
        return map.keys.map { key -> map.filter { it.key.startsWith(key) }.values.sum() }
                .filter { it < 100000 }
                .sum()
    }

    fun part2(input: List<String>): Int {
        val totalSize =  70000000
        val updateSize = 30000000
        val map = buildFileTree(input)
        val sizes = map.keys.associateWith { key -> map.filter { it.key.startsWith(key) }.values.sum() }
        val availableSpace = totalSize - sizes["/"]!!
        val requiredSpace = updateSize - availableSpace
        return sizes.filter { it.value >= requiredSpace }.minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    check(part1(testInput) == 95437)
    println(part1(input))

    check(part2(testInput) == 24933642)
    println(part2(input))
}
