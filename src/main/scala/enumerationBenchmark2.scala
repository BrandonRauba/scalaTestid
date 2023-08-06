import org.scalameter._

object Color1 extends Enumeration {
  type Color = Value

  val Red: Color1.Value = Value("Red")
  val Green: Color1.Value = Value("Green")
  val Blue: Color1.Value = Value("Blue")
}

object enumerationBenchmark2 extends Bench.LocalTime {

  val numIterations = 1000000
  var warmupIterations = 5000
  
  val enumerationBenchmark = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      Color1.values.foreach(identity)
    }

    performance of "Enumeration" in {
      measure method "values" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[Color1.Value] = Nil
          for (j <- 0 until numIterations) {
            result = Color1.values.toList
          }
          result
        }
      }
    }
  }
  enumerationBenchmark.executor
}