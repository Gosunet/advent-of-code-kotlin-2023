fun main() {
  fun part1(input: List<String>): Int {
    val parts = mutableListOf<Part>()
    val symbol = mutableListOf<Symbol>()
    input.withIndex().forEach { (index, line) ->
      "\\d+".toRegex().findAll(line).forEach { matchResult ->
        parts.add(Part(matchResult.value.toInt(), PartPosition(index, matchResult.range)))
      }
      "[^\\w\\s.]".toRegex().findAll(line).forEach { matchResult ->
        symbol.add(Symbol(matchResult.range.first, index))
      }
    }

    var sumOfPart = 0
    parts.forEach { part ->
      val startIndexBufferZone = if (part.position.y.first == 0) 0 else part.position.y.first - 1
      val endIndexBufferZone = part.position.y.last + 1
      val topIndexBufferZone = if (part.position.x == 0) 0 else part.position.x - 1
      val bottomIndexBufferZone = part.position.x + 1

      symbol.forEach { sym ->
        if (sym.x in startIndexBufferZone..endIndexBufferZone && sym.y in topIndexBufferZone..bottomIndexBufferZone) {
          sumOfPart += part.value
        }
      }
    }
    return sumOfPart
  }

  fun part2(input: List<String>): Int {
    val parts = mutableListOf<Part>()
    val gears = mutableListOf<Symbol>()
    input.withIndex().forEach { (index, line) ->
      Regex("\\d+").findAll(line).forEach { matchResult ->
        parts.add(Part(matchResult.value.toInt(), PartPosition(index, matchResult.range)))
      }
      Regex("\\*").findAll(line).forEach { matchResult ->
        gears.add(Symbol(matchResult.range.first, index))
      }
    }

    return gears.sumOf { gear ->
      val startIndexBufferZone = gear.x - 1
      val endIndexBufferZone = gear.x + 1
      val topIndexBufferZone = gear.y - 1
      val bottomIndexBufferZone = gear.y + 1

      val partsInGearZone = parts.filter { part ->
        part.position.x in topIndexBufferZone..bottomIndexBufferZone && (part.position.y.first in startIndexBufferZone..endIndexBufferZone || part.position.y.last in startIndexBufferZone..endIndexBufferZone)
      }
      if (partsInGearZone.size == 2) {
        val gearValue = partsInGearZone[0].value * partsInGearZone[1].value
        gearValue
      } else {
        0
      }
    }
  }

  val testInput = readInput("day_03_test")
  val testActual = part1(testInput)
  check(testActual == 4361) { "Expected 4361, got $testActual" }

  val testActual2 = part2(testInput)
  check(testActual2 == 467835) { "Expected 467835, got $testActual2" }

  part1(readInput("day03")).println()
  part2(readInput("day03")).println()
}

data class PartPosition(val x: Int, val y: IntRange)
data class Part(val value: Int, val position: PartPosition)
data class Symbol(val x: Int, val y: Int)