import org.scalameter._

def leafElem2[X](x: X): Any = x match
  case x: String      => x.charAt(0)
  case x: Array[t]    => leafElem(x(0))
  case x: Iterable[t] => leafElem(x.head)
  case x: AnyVal      => x

object matchBenchmark2 extends Bench.LocalTime {
  val numIterations = 10000
  var warmupIterations = 500

  val leafElemBenchmark2 = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      leafElem("test")
      leafElem(Array(1, 2, 3))
      leafElem(List("a", "b", "c"))
      leafElem(42)
    }

    performance of "leafElem" in {
      measure method "apply" in {
        using(Gen.unit("inputs")) in { _ =>
          var result: Any = null
          for (j <- 0 until numIterations) {
            leafElem("test")
            leafElem(Array(1, 2, 3))
            leafElem(List("a", "b", "c"))
            leafElem(42)
          }
          result
        }
      }
    }
  }
  leafElemBenchmark2.executor
}