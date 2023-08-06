import org.scalameter._

inline def inlinedLogged[T](level: Int, message: => String)(inline op: T): T =
  println(s"[$level]Computing $message")
  val res = op
  println(s"[$level]Result of $message: $res")
  res

object inlinedLoggedBenchmark extends Bench.LocalTime {

  val numIterations = 1000
  val level = 1
  val message = "faktoriaal arvutus"
  val warmupIterations = 5000
  
  def expensiveComputation(): Long = {
    def factorial(n: Long): Long = if (n <= 1) 1 else n * factorial(n - 1)
    factorial(20L)
  }
  
  val loggedTest = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      inlinedLogged(level, message)(expensiveComputation())
    }
    
    performance of "logged" in {
      measure method "logged" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: Long = 0L
          for (j <- 0 until numIterations) {
            result = inlinedLogged(level, message)(expensiveComputation())
          }
          result
        }
      }
    }
  }
  loggedTest.executor
}