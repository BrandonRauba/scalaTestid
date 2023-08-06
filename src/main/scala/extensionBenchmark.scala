import org.scalameter._

case class Circle(x: Double, y: Double, radius: Double)
extension (c: Circle)
  def circumference: Double = c.radius * math.Pi * 2

object extensionBenchmark extends Bench.LocalTime {
  val numIterations = 1000000
  val warmupIterations = 500
  
  def circleClass(): Double = {
    val circle = Circle(0.0, 0.0, 10.0) 
    circle.circumference 
  }
  
  val circleTest = new Bench.ForkedTime {
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
  circleTest.executor
}