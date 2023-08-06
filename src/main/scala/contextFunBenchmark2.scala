import org.scalameter._

object contextFunBenchmark2 extends Bench.LocalTime {

  val numIterations = 10000
  val warmupIterations = 50

  val nonContextTest = new Bench.ForkedTime {
    override def aggregator = Aggregator.median

    for (i <- 0 until warmupIterations) {
      val t = Table()
      val r1 = Row()
      val r2 = Row()
      val r3 = Row()
      t.add(r1)
      r1.add(new Cell("top left"))
      r1.add(new Cell("top right"))
      t.add(r2)
      r2.add(new Cell("bottom left"))
      r2.add(new Cell("bottom right"))
      t.add(r3)
    }

    performance of "nonContext" in {
      measure method "nonContextTest" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: Table = Table()
          for (j <- 0 until numIterations) {
            val t = Table()
            val r1 = Row()
            val r2 = Row()
            val r3 = Row()
            t.add(r1)
            r1.add(new Cell("top left"))
            r1.add(new Cell("top right"))
            t.add(r2)
            r2.add(new Cell("bottom left"))
            r2.add(new Cell("bottom right"))
            t.add(r3)
            result = t
          }
          result
        }
      }
    }
  }
  nonContextTest.executor
}