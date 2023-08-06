import org.scalameter._

object MyMath2:
  type Logarithm = Double

  object Logarithm:

    def apply(d: Double): Logarithm = math.log(d)

    def safe(d: Double): Option[Logarithm] =
      if d > 0.0 then Some(math.log(d)) else None

    extension (x: Logarithm)
      def toDouble: Double = math.exp(x)
      def + (y: Logarithm): Logarithm = Logarithm(math.exp(x) + math.exp(y))
      def * (y: Logarithm): Logarithm = x + y

end MyMath2

object opaqueBenchmark2 extends Bench.LocalTime {

  val numIterations = 100000
  var warmupIterations = 5000
  
  val myMathBenchmark = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      MyMath2.Logarithm(2.0)
    }

    performance of "MyMath" in {
      measure method "Logarithm.apply" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[MyMath2.Logarithm] = Nil
          for (j <- 0 until numIterations) {
            result = MyMath2.Logarithm(2.0) :: result
          }
          result
        }
      }

      measure method "Logarithm.safe" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[Option[MyMath2.Logarithm]] = Nil
          for (j <- 0 until numIterations) {
            result = MyMath2.Logarithm.safe(2.0) :: result
          }
          result
        }
      }

      measure method "Logarithm.toDouble" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[Double] = Nil
          val log = MyMath2.Logarithm(2.0)
          for (j <- 0 until numIterations) {
            result = log.toDouble :: result
          }
          result
        }
      }

      measure method "Logarithm.+" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[MyMath2.Logarithm] = Nil
          val log1 = MyMath2.Logarithm(2.0)
          val log2 = MyMath2.Logarithm(3.0)
          for (j <- 0 until numIterations) {
            result = (log1 + log2) :: result
          }
          result
        }
      }

      measure method "Logarithm.*" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: List[MyMath2.Logarithm] = Nil
          val log1 = MyMath2.Logarithm(2.0)
          val log2 = MyMath2.Logarithm(3.0)
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