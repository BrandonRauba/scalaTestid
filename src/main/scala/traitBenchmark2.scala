import org.scalameter._

trait Greeting2:
  val name: String
  def msg = s"How are you, $name"

class C2 extends Greeting2:
  val name = "Bob"
  println(msg)

object traitBenchmark2 extends Bench.LocalTime {
  val numIterations = 1000
  var warmupIterations = 500

  val greetingBenchmark = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      val c = new C()
      val msg = c.msg
    }

    performance of "Greeting" in {
      measure method "msg" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: String = ""
          for (j <- 0 until numIterations) {
            val c = new C2()
            result = c.msg
          }
          result
        }
      }
    }
  }
  greetingBenchmark.executor
}