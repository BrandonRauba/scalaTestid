import org.scalameter._

case class nonExtensionCircle(x: Double, y: Double, radius: Double) {
  def circumference: Double = radius * math.Pi * 2
}

object extensionBenchmark2 extends Bench.LocalTime {
  val numIterations = 1000000
  val warmupIterations = 500
  
  def circleClass(): Double = {
    val circle = nonExtensionCircle(0.0, 0.0, 10.0) 
    circle.circumference 
  }
  
  val circleTest2 = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      circleClass()
    }

    performance of "Circle" in {
      measure method "circumference" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: Double = 0.0
          for (j <- 0 until numIterations) {
            result = circleClass()
          }
          result
        }
      }
    }
  }
  circleTest2.executor
}