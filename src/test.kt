import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.pow

fun main() {
  val input = Path("src/day13.txt").readText().split("\n\n")
  println("Part 1: " + input.sumBy {
    Landscape(it.lines()).findMirror()
  })
  println("Part 2: " + input.sumBy {
    Landscape(it.lines()).findMirrorSmudged()
  })

}
class Landscape(lines: List<String>) {
  private val width = lines[0].length
  private val height = lines.size
  private val horizontals = lines.map { it.indices.sumOf { index -> charToNumber(index, it[index]) } }
  private val verticals = (0 until width).map { x -> lines.indices.sumOf { y -> charToNumber(y, lines[y][x]) } }

  fun findMirror(): Int {
    val (vertical, horizontal) = findVerticalAndHorizontal()
    return vertical ?: (horizontal!! * 100)
  }

  fun findMirrorSmudged(): Int {
    val (oVertical, oHorizontal) = findVerticalAndHorizontal()
    val x = (0 until width * height).map {
      val (nVertical, nHorizontal) =
        lookInDimension(smudgeVertical(it), oVertical ?: -1) to
                lookInDimension(smudgeHorizontal(it), oHorizontal ?: -1)
      if (nVertical != null && nVertical != oVertical) nVertical
      else if (nHorizontal != null && nHorizontal != oHorizontal) nHorizontal * 100
      else 0
    }
    return x.first { it != 0 }
  }

  private fun findVerticalAndHorizontal(): Pair<Int?, Int?> {
    return lookInDimension(verticals) to lookInDimension(horizontals)
  }

  private fun smudgeVertical(smudge: Int): MutableList<Long> {
    val newVerticals = verticals.toMutableList()
    val value = 2.0.pow((smudge % height).toDouble()).toLong()
    newVerticals[smudge / height] += if (newVerticals[smudge / height] and value == 0L) value else value * -1
    return newVerticals
  }

  private fun smudgeHorizontal(smudge: Int): MutableList<Long> {
    val newHorizontals = horizontals.toMutableList()
    val value = 2.0.pow((smudge % width).toDouble()).toLong()
    newHorizontals[smudge / width] += if (newHorizontals[smudge / width] and value == 0L) value else value * -1
    return newHorizontals
  }

  private fun lookInDimension(lines: List<Long>, skip: Int = -1): Int? {
    return (1 until lines.size).filter { lines[it - 1] == lines[it] }.firstOrNull { possibleMirror ->
      possibleMirror != skip &&
              (possibleMirror until lines.size).filter { it - possibleMirror < possibleMirror }
                .toList().filterIndexed { index, mirror -> lines[mirror - index * 2 - 1] != lines[mirror] }.isEmpty()
    }
  }

  private fun charToNumber(x: Int, char: Char): Long {
    return if (char == '#')
      2.0.pow(x.toDouble()).toLong()
    else 0L
  }
}