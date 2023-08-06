import org.scalameter._

trait Resettable {
  def reset(): Unit
}

trait Growable[T] {
  def add(t: T): Unit
}

def f(x: Resettable & Growable[String]) =
  x.reset()
  x.add("first")

object intersectionBenchmark extends Bench.LocalTime {

  val numIterations = 1000000
  var warmupIterations = 5000
  
  val growableBenchmark = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      val x = new Resettable with Growable[String] {
        var data = List.empty[String]
        def reset() = { data = List.empty[String] }
        def add(t: String) = { data = t :: data }
      }
      f(x)
    }

    performance of "Growable and Resettable" in {
      measure method "f" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: Unit = ()
          for (j <- 0 until numIterations) {
            val x = new Resettable with Growable[String] {
              var data = List.empty[String]
              def reset() = { data = List.empty[String] }
              def add(t: String) = { data = t :: data }
            }
            f(x)
            result = ()
          }
          result
        }
      }
    }
  }
  growableBenchmark.executor
}