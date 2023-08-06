import org.scalameter._

case class Cell(value: String)
case class Row(cells: Seq[Cell] = Seq.empty) {
  def add(cell: Cell): Row = copy(cells = cells :+ cell)
}
case class Table(rows: Seq[Row] = Seq.empty) {
  def add(row: Row): Table = copy(rows = rows :+ row)
}

def table(init: Table ?=> Unit) =
  given t: Table = Table()
  init
  t

def row(init: Row ?=> Unit)(using t: Table) =
  given r: Row = Row()
  init
  t.add(r)

def cell(str: String)(using r: Row) =
  r.add(new Cell(str))

object contextFunBenchmark extends Bench.LocalTime {

  val numIterations = 10000
  val warmupIterations = 50
  
  val contextTest = new Bench.ForkedTime {
    override def aggregator = Aggregator.median
    
    for (i <- 0 until warmupIterations) {
      table {
        row {
          cell("top left")
          cell("top right")
        }
        row {
          cell("bottom left")
          cell("bottom right")
        }
      }
    }

    performance of "table" in {
      measure method "table" in {
        using(Gen.unit("iterations")) in { _ =>
          var result: Table = Table()
          for (j <- 0 until numIterations) {
            result = table {
              row {
                cell("top left")
                cell("top right")
              }
              row {
                cell("bottom left")
                cell("bottom right")
              }
            }
          }
          result
        }
      }
    }
  }
  contextTest.executor
}