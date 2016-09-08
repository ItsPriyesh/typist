package me.priyesh.typist

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object Typist extends JSApp {

  @JSExport
  def main() = {
    new TypistRenderer().start(WordSource.get) { (state, result) =>
      result match {
        case Success => state advance Correct
        case Failure => state advance Incorrect
        case Pending => state
      }
    }
  }
}