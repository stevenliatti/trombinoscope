import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import scala.language.postfixOps

import sys.process.*

@main def convertImages(
    srcDir: String,
    destDir: String,
    imgWidthPixels: String
) =
  val dest = new File(destDir)
  if (!dest.exists) dest.mkdir

  filesIn(srcDir) match
    case Some(files) =>
      files.sorted.zipWithIndex.foreach { case (f, i) =>
        val ext = f.getName.split("\\.").last
        val copyFile = new File(s"$destDir/${i + 1}.$ext")
        s"convert -resize ${imgWidthPixels}x ${f.getAbsolutePath} ${copyFile.getAbsolutePath}" !
      }
    case None =>
      println("srcDir not present")
      sys.exit(1)

def filesIn(dir: String): Option[List[File]] =
  val d = new File(dir)
  if (d.exists && d.isDirectory) Some(d.listFiles.filter(_.isFile).toList)
  else None
