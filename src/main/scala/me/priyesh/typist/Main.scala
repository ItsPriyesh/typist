package me.priyesh.typist

import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent
import org.scalajs.jquery._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object Main extends JSApp {

  @JSExport
  def main() = {
    jQuery(".words-container").append(List.fill(666) { "gg" } .mkString(" "))
    dom.document.addEventListener("keyup", (e: KeyboardEvent) => handleKeyPress(e.key), useCapture = false)
  }

  private def handleKeyPress(key: String) = key match {
    case i => println(i)
  }
}
