import org.scalameter._

type Elem[X] = X match
  case String => Char
  case Array[t] => t
  case Iterable[t] => t
type LeafElem[X] = X match
  case String => Char
  case Array[t] => LeafElem[t]
  case Iterable[t] => LeafElem[t]
  case AnyVal => X
def leafElem[X](x: X): LeafElem[X] = x match
  case x: String      => x.charAt(0)
  case x: Array[t]    => leafElem(x(0))
  case x: Iterable[t] => leafElem(x.head)
  case x: AnyVal      => x

object matchBenchmark extends Bench.LocalTime {
  val numIterations = 10000
  var warmupIterations = 500

  val leafElemBenchmark = new Bench.ForkedTime {
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
          val result: Any = null
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
  leafElemBenchmark.executor
}