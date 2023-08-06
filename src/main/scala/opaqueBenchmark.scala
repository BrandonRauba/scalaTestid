import org.scalameter._

object MyMath1:
  opaque type Logarithm = Double

  object Logarithm:

    def apply(d: Double): Logarithm = math.log(d)

    def safe(d: Double): Option[Logarithm] =
      if d > 0.0 then Some(math.log(d)) else None

    extension (x: Logarithm)
      def toDouble: Double = math.exp(x)
      def + (y: Logarithm): Logarithm = Logarithm(math.exp(x) + math.exp(y))
      def * (y: Logarithm): Logarithm = x + y

end MyMath1

object opaqueBenchmark extends Bench.LocalTime {

  val numIterations = 100000
  var warmupIterations = 5000
  
  val myMathBenchmark = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      MyMath1.Logarithm(2.0)
    }

    performance of "MyMath" in {
      measure method "Logarithm.apply" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[MyMath1.Logarithm] = Nil
          for (j <- 0 until numIterations) {
            result = MyMath1.Logarithm(2.0) :: result
          }
          result
        }
      }

      measure method "Logarithm.safe" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[Option[MyMath1.Logarithm]] = Nil
          for (j <- 0 until numIterations) {
            result = MyMath1.Logarithm.safe(2.0) :: result
          }
          result
        }
      }

      measure method "Logarithm.toDouble" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[Double] = Nil
          val log = MyMath1.Logarithm(2.0)
          for (j <- 0 until numIterations) {
            result = log.toDouble :: result
          }
          result
        }
      }

      measure method "Logarithm.+" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[MyMath1.Logarithm] = Nil
          val log1 = MyMath1.Logarithm(2.0)
          val log2 = MyMath1.Logarithm(3.0)
          for (j <- 0 until numIterations) {
            result = (log1 + log2) :: result
          }
          result
        }
      }

      measure method "Logarithm.*" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[MyMath1.Logarithm] = Nil
          val log1 = MyMath1.Logarithm(2.0)
          val log2 = MyMath1.Logarithm(3.0)
          for (j <- 0 until numIterations) {
            result = (log1 * log2) :: result
          }
          result
        }
      }
    }
  }
  myMathBenchmark.executor
}