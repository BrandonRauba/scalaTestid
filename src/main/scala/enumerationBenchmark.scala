import org.scalameter._

enum Color:
  case Red, Green, Blue

object enumerationBenchmark extends Bench.LocalTime {

  val numIterations = 1000000
  var warmupIterations = 5000

  val enumerationBenchmark = new Bench.ForkedTime {
    override def aggregator = Aggregator.median

    for (i <- 0 until warmupIterations) {
      val red = Color.Red
      val green = Color.Green
      val blue = Color.Blue
    }

    performance of "enumeration" in {
      measure method "enumeration" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: Color = null
          for (j <- 0 until numIterations) {
            val red = Color.Red
            val green = Color.Green
            val blue = Color.Blue
          }
          result
        }
      }
    }
  }
  enumerationBenchmark.executor
}