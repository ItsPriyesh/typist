package me.priyesh.typist

import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.jquery.jQuery
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.duration._

@JSExport
object Typist extends JSApp {

  @JSExport
  def main() = new Engine(
    container = Binder(elemById("words-container"), _.render),
    countdown = Binder(elemById("countdown"), _.toString),
    results = Binder(elemById("results"), r => {
      jQuery("results").css("display", "block")
      r.render
    }),
    input = elemById("input").asInstanceOf[HTMLInputElement],
    duration = 60 seconds
  ).start()
}