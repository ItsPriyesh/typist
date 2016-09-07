package me.priyesh.typist

import org.scalajs.dom

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import monix.execution.Scheduler.Implicits.global
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.jquery._

@JSExport
object App extends JSApp {

  object Page {

    import scalatags.JsDom.all._

    lazy val body =
      div(id := "content")(
        div(id := "words-container"),
        div(id := "input-container")(
          input(id := "input", `type` := "text")
        )
      )
  }

  @JSExport
  def main() = {
    dom.document.body appendChild Page.body.render

    val words = "mist was in the ship three hours out from port the face of the moon was in shadow a shining crescent far beneath the flying vessel".split(" ")
    val input = dom.window.document.getElementById("input").asInstanceOf[HTMLInputElement]

    jQuery("#words-container").append(words.mkString(" "))
    StringEvaluator.run(words.toList, new InputEmitter(input))
      .subscribe(new WordRenderer("#words-container", words))
  }
}