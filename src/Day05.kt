import kotlin.system.measureTimeMillis

fun main() {

  fun part1(input: List<String>): Long {
    val seeds = mutableListOf<Long>()
    val mappingTables = mutableListOf<MappingTable>()
    input.joinToString("\n").split("\n\n").withIndex().map { (index, value) ->
      when (index) {
        0 -> seeds.addAll(value.substringAfter("seeds: ").split(" ").map { it.toLong() })
        else -> mappingTables.addAll(value.substringAfter("map:\n").split("\n").map { mappingValues ->
          val (destination, sourceStart, range) = mappingValues.split(" ").map { it.toLong() }

          MappingTable(
            index.indexToMappingType(),
            destination until destination + range,
            sourceStart until sourceStart + range,
            range
          )
        })
      }
    }
    val soils = seeds.map { seed ->
      var soil = seed
      mappingTables.filter { it.mappingType == MappingCategory.SEED_TO_SOIL }.forEach { mappingTable ->
        if (seed in mappingTable.sourceRange) {
          soil = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + seed
        }
      }
      soil
    }
    val fertilizers = soils.map { soil ->
      var fertilizer = soil
      mappingTables.filter { it.mappingType == MappingCategory.SOIL_TO_FERTILIZER }.forEach { mappingTable ->
        if (soil in mappingTable.sourceRange) {
          fertilizer = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + soil
        }
      }
      fertilizer
    }
    val waters = fertilizers.map { fertilizer ->
      var water = fertilizer
      mappingTables.filter { it.mappingType == MappingCategory.FERTILIZER_TO_WATER }.forEach { mappingTable ->
        if (fertilizer in mappingTable.sourceRange) {
          water = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + fertilizer
        }
      }
      water
    }
    val lights = waters.map { water ->
      var light = water
      mappingTables.filter { it.mappingType == MappingCategory.WATER_TO_LIGHT }.forEach { mappingTable ->
        if (water in mappingTable.sourceRange) {
          light = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + water
        }
      }
      light
    }
    val temperatures = lights.map { light ->
      var temperature = light
      mappingTables.filter { it.mappingType == MappingCategory.LIGHT_TO_TEMPERATURE }.forEach { mappingTable ->
        if (light in mappingTable.sourceRange) {
          temperature = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + light
        }
      }
      temperature
    }
    val humidities = temperatures.map { temperature ->
      var humidity = temperature
      mappingTables.filter { it.mappingType == MappingCategory.TEMPERATURE_TO_HUMIDITY }.forEach { mappingTable ->
        if (temperature in mappingTable.sourceRange) {
          humidity = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + temperature
        }
      }
      humidity
    }
    val locations = humidities.map { humidity ->
      var location = humidity
      mappingTables.filter { it.mappingType == MappingCategory.HUMIDITY_TO_LOCATION }.forEach { mappingTable ->
        if (humidity in mappingTable.sourceRange) {
          location = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + humidity
        }
      }
      location
    }

    return locations.min()
  }

  fun parse(input: List<String>): Pair<List<LongRange>, List<MappingTable>> {
    val seedRanges = mutableListOf<LongRange>()
    val mappingTables = mutableListOf<MappingTable>()
    input.joinToString("\n").split("\n\n").withIndex().map { (index, value) ->
      when (index) {
        0 -> {
          seedRanges.addAll(value.substringAfter("seeds: ").split(" ").chunked(2).map { (startRange, range) ->
            (startRange.toLong() until startRange.toLong() + range.toLong())
          })
        }

        else -> mappingTables.addAll(value.substringAfter("map:\n").split("\n").map { mappingValues ->
          val (destination, sourceStart, range) = mappingValues.split(" ").map { it.toLong() }

          MappingTable(
            index.indexToMappingType(),
            destination until destination + range,
            sourceStart until sourceStart + range,
            range
          )
        })
      }
    }
    return Pair(seedRanges.toList(), mappingTables.toList())
  }

  fun soils(
    seedRange: LongRange,
    mappingTablesSeedSoil: List<MappingTable>
  ) = seedRange.asSequence().map { seed ->
    mappingTablesSeedSoil.forEach { mappingTable ->
      if (seed in mappingTable.sourceRange) {
        return@map (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + seed
      }
    }
    seed
  }


  fun part2(input: List<String>): Long {
    val (seedRanges, mappingTables) = parse(input)
    val mappingTablesSeedSoil = mappingTables.filter { it.mappingType == MappingCategory.SEED_TO_SOIL }
    val mappingTablesSoilFertilizer = mappingTables.filter { it.mappingType == MappingCategory.SOIL_TO_FERTILIZER }
    val mappingTablesFertilizerWater = mappingTables.filter { it.mappingType == MappingCategory.FERTILIZER_TO_WATER }
    val mappingTablesWaterLight = mappingTables.filter { it.mappingType == MappingCategory.WATER_TO_LIGHT }
    val mappingTablesLightTemperature = mappingTables.filter { it.mappingType == MappingCategory.LIGHT_TO_TEMPERATURE }
    val mappingTablesTemperatureHumidity =
      mappingTables.filter { it.mappingType == MappingCategory.TEMPERATURE_TO_HUMIDITY }
    val mappingTablesHumidityLocation = mappingTables.filter { it.mappingType == MappingCategory.HUMIDITY_TO_LOCATION }

    return seedRanges.minOf { seedRange ->
      soils(seedRange, mappingTablesSeedSoil).map { soil ->
        mappingTablesSoilFertilizer.forEach { mappingTable ->
          if (soil in mappingTable.sourceRange) {
            return@map (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + soil
          }
        }
        soil
      }.map { fertilizer ->
        mappingTablesFertilizerWater.forEach { mappingTable ->
          if (fertilizer in mappingTable.sourceRange) {
            return@map (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + fertilizer
          }
        }
        fertilizer
      }.map { water ->
        mappingTablesWaterLight.forEach { mappingTable ->
          if (water in mappingTable.sourceRange) {
            return@map (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + water
          }
        }
        water
      }.map { light ->
        mappingTablesLightTemperature.forEach { mappingTable ->
          if (light in mappingTable.sourceRange) {
            return@map (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + light
          }
        }
        light
      }.map { temperature ->
        mappingTablesTemperatureHumidity.forEach { mappingTable ->
          if (temperature in mappingTable.sourceRange) {
            return@map (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + temperature
          }
        }
        temperature
      }.minOf { humidity ->
        var location = humidity
        mappingTablesHumidityLocation.forEach { mappingTable ->
          if (humidity in mappingTable.sourceRange) {
            location = (mappingTable.destinationRange.first - mappingTable.sourceRange.first) + humidity
          }
        }
        location
      }
    }
  }

  val testInput = readInput("day_05_test")
//  val testActual = part1(testInput)
//  check(testActual == 35L) { "Expected 13, got $testActual" }

//  val testActual2 = part2(testInput)
//  check(testActual2 == 46L) { "Expected 46, got $testActual2" }

//  part1(readInput("day05")).println()

  val time = measureTimeMillis {
    part2(readInput("day05")).println()
  }
  println(time)

}

data class MappingTable(
  val mappingType: MappingCategory,
  val destinationRange: LongRange,
  val sourceRange: LongRange,
  val range: Long
)

enum class MappingCategory {
  SEED_TO_SOIL,
  SOIL_TO_FERTILIZER,
  FERTILIZER_TO_WATER,
  WATER_TO_LIGHT,
  LIGHT_TO_TEMPERATURE,
  TEMPERATURE_TO_HUMIDITY,
  HUMIDITY_TO_LOCATION,
}

fun Int.indexToMappingType() = when (this) {
  1 -> MappingCategory.SEED_TO_SOIL
  2 -> MappingCategory.SOIL_TO_FERTILIZER
  3 -> MappingCategory.FERTILIZER_TO_WATER
  4 -> MappingCategory.WATER_TO_LIGHT
  5 -> MappingCategory.LIGHT_TO_TEMPERATURE
  6 -> MappingCategory.TEMPERATURE_TO_HUMIDITY
  7 -> MappingCategory.HUMIDITY_TO_LOCATION
  else -> throw IllegalArgumentException("Invalid mapping type")
}