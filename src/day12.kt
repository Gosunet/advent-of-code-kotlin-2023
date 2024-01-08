import org.chocosolver.solver.Model
import org.chocosolver.solver.constraints.nary.automata.FA.FiniteAutomaton
import org.chocosolver.solver.constraints.nary.automata.FA.IAutomaton
import org.chocosolver.solver.variables.BoolVar
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.system.measureTimeMillis

private fun dfa(cells: Array<BoolVar>, rest: IntArray, model: Model, row: String) {
  val regexp = StringBuilder("0*")
  val m = rest.size
  for (i in 0 until m) {
    regexp.append('1').append('{').append(rest[i]).append('}')
    regexp.append('0')
    regexp.append(if (i == m - 1) '*' else '+')
  }
  val auto: IAutomaton = FiniteAutomaton(regexp.toString())
  model.regular(cells, auto).post()
}

fun main() {
  measureTimeMillis {
    val input = Path("src/day12.txt").readLines().map {
      val (first, second) = it.split(" ")
      first to second.split(",").map { it.toInt() }
    }

//  println(input)


    val solutionCount = input.sumOf {
//    println(it)
      val model = Model("Nonogram")
      var solutionCount = 0
      val cells = model.boolVarArray("c", it.first.length)
      dfa(cells, it.second.toIntArray(), model, it.first)
      while (model.solver.solve()) {
        val rowNumber = it.first.replace("#", "1").replace(".", "0")
        val regex = transformToRegex(rowNumber)
        val solution = cells.joinToString("") { it.value.toString() }
//      println(solution)
//      println(regex)
        if (solution.matches(regex.toRegex())) {
//        println(solution.map { if (it == '1') '#' else '.' })
          solutionCount++
        }
//      solutionCount++
//      println(cells.joinToString("") { if (it.value == 1) "#" else "." })
      }
//    println("Solution $solutionCount:")
      solutionCount
    }
    println(solutionCount)
  }.also { println("${it} ms") }
}

fun transformToRegex(inputString: String): String {
  val regex = StringBuilder()
  inputString.forEach {
    when (it) {
      '?' -> regex.append('.')
      else -> regex.append(it)
    }
  }
  return regex.toString()
}