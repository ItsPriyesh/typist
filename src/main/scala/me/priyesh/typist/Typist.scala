package me.priyesh.typist

import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.duration._

@JSExport
object Typist extends JSApp {

  @JSExport
  def main() = new Engine(
    container = Binder(elemById("words-container"), _.render),
    status = Binder(elemById("status"), _.toString),
    input = elemById("input").asInstanceOf[HTMLInputElement],
    duration = 60 seconds
  ).start()
}