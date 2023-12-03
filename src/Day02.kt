
private const val MAX_BLUE = 14
private const val MAX_RED = 12
private const val MAX_GREEN = 13

private fun parsing(input: List<String>): List<Game> {
  return buildList {
    input.withIndex().map { (rowIndex, row) ->
      row.split(": ")[1].let { turns ->
        val dicePerRound = turns.split("; ").map { turn ->
          turn.split(", ").map { dice ->
            val round = dice.split(" ")
            mapOf(round[1] to round[0].toInt())
          }
        }
        dicePerRound
      }.let {
        add(Game(rowIndex + 1, it))
      }
    }
  }
}
fun main() {
  fun part1(input: List<String>): Int {
    val games = parsing(input)

    return games.filter {game ->
      game.plays.forEach { plays ->
        plays.forEach { play ->
          println(play)
          if (play["blue"] != null && play["blue"]!! > MAX_BLUE) return@filter false
          if (play["red"] != null && play["red"]!! > MAX_RED) return@filter false
          if (play["green"] != null && play["green"]!! > MAX_GREEN) return@filter false
        }
      }
      return@filter true
    }.sumOf {
      it.id
    }
  }

  fun part2(input: List<String>): Int {
    val games = parsing(input)

    return games.sumOf { game ->
      var fewerGreen = 0
      var fewerRed = 0
      var fewerBleu = 0
      game.plays.forEach { plays ->
        plays.forEach { play ->
          if (play["blue"] != null && play["blue"]!! > fewerBleu) fewerBleu = play["blue"]!!
          if (play["red"] != null && play["red"]!! > fewerRed) fewerRed = play["red"]!!
          if (play["green"] != null && play["green"]!! > fewerGreen) fewerGreen = play["green"]!!
        }
      }
      val powerGame = fewerGreen * fewerRed * fewerBleu
      println("Game ${game.id} : $fewerGreen * $fewerRed * $fewerBleu = $powerGame")
      powerGame
    }
  }

  val testInput = readInput("day_02_test")
  val testActual = part2(testInput)
  check(testActual == 2286) { "Expected 2286, got $testActual" }

  part2(readInput("day02")).println()
}

data class Game(val id: Int, val plays: List<List<Map<String, Int>>>)
