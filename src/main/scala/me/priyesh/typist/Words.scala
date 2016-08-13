package me.priyesh.typist

import scala.io.Source

object Words {
  val All = Source.fromFile("words.txt").getLines()
}
