fun main() {
  fun part2(input: List<String>): Int =
    input.map {
      it.replace("twone", "21")
        .replace("threeight", "38")
        .replace("nineight", "98")
        .replace("sevenine", "79")
        .replace("fiveight", "58")
        .replace("eightwo", "82")
        .replace("eighthree", "83")
        .replace("oneight", "18")
        .replace("one", "1")
        .replace("two", "2")
        .replace("three", "3")
        .replace("four", "4")
        .replace("five", "5")
        .replace("six", "6")
        .replace("seven", "7")
        .replace("eight", "8")
        .replace("nine", "9")
    }.sumOf { line ->
      val firstDigit = line.first(Char::isDigit)
      val lastDigit = line.last(Char::isDigit)
      "$firstDigit$lastDigit".toInt()
    }

  val testInput = readInput("Day01_test")
  val testActual = part2(testInput)
  check(testActual == 281)

  part2(readInput("day01")).println()
}