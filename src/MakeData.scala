import java.io._
import scala.io.Source

object MakeData extends App {
  val (
    csvFile,
    maxChoristPerRow,
    maxRowPerPage,
    imagesMargin,
    imageExtFile,
    out
  ) =
    args.toList match {
      case c :: mc :: mr :: im :: ie :: out :: _ =>
        (c, Integer.parseInt(mc), Integer.parseInt(mr), im, ie, out)
      case _ =>
        println(
          "Not enough args, give <csvFile> <maxChoristPerRow> <maxRowPerPage> <imagesMargin> <imageExtFile> <out>"
        )
        sys.exit(1)
    }

  val csvSeparator = ","
  val ratioImageWidth = 0.92 * (1.0 / maxChoristPerRow)

  val chorists = Source
    .fromFile(csvFile)
    .getLines
    .toList
    .tail
    .map(l => l.split(csvSeparator).toList)
    .map(a =>
      a match {
        case l :: n :: no :: v :: _ if no.nonEmpty && v.nonEmpty =>
          Some(Chorist(l, n, no, v))
        case _ => None
      }
    )
    .collect { case Some(c) => c }
    .sortBy(_.name)
    .groupBy(_.voice)

  val dataTex = List(
    "soprano",
    "alto",
    "ténor",
    "basse"
  ) map (v => toLatex(v, chorists.getOrElse(v, Nil)))
  writeFile(out, dataTex)

  // Behaviors and data

  trait ToLatex {
    def toLatex: String
  }
  case class Chorist(lastname: String, name: String, no: String, voice: String)
      extends ToLatex {
    override def toLatex: String = {
      s"""|
        |\\begin{subfigure}[b]{$ratioImageWidth\\linewidth}
        |  \\includegraphics[width=\\linewidth]{$no.$imageExtFile}
        |  \\small{${capitalize(name)} ${capitalize(lastname)}}
        |\\end{subfigure}
      |""".stripMargin
    }
  }
  case class ChoristsRow(chorists: List[Chorist]) extends ToLatex {
    val rows: List[List[Chorist]] =
      toTuples(chorists, maxChoristPerRow).filter(_.nonEmpty)
    val pages: List[List[List[Chorist]]] = toTuples(rows, maxRowPerPage)

    override def toLatex: String = pages
      .map(rows =>
        rows
          .map { cs =>
            s"""|
              |\\begin{figure}[h!]
              |  ${cs.map(_.toLatex).mkString(s"\\hspace{${imagesMargin}em}")}
              |\\end{figure}
            |""".stripMargin
          }
          .mkString("\n")
      )
      .mkString("\\newpage\n")

    private def toTuples[T](cs: List[T], max: Int): List[List[T]] = cs match {
      case Nil => List(Nil)
      case _   => cs.take(max) :: toTuples(cs.drop(max), max)
    }
  }

  def toLatex(voice: String, chorists: List[Chorist]): String = {
    val crs = ChoristsRow(chorists)
    s"""|
      |\\section*{${capitalize(voice)}}
      |${crs.toLatex}
    """.stripMargin
  }

  def capitalize(s: String): String = {
    def processChar(previousChar: Char, rest: String): String = {
      if (rest.isEmpty) ""
      else {
        val first =
          if (previousChar.isWhitespace || previousChar == '-')
            rest.head.toUpper
          else rest.head
        first.toString + processChar(rest.head, rest.tail)
      }
    }
    require(s.length > 0)
    processChar(' ', s.trim.toLowerCase)
  }

  def writeFile(filename: String, lines: Seq[String]): Unit = {
    val bw = new BufferedWriter(new FileWriter(new File(filename)))
    lines foreach bw.write
    bw.close()
  }
}
