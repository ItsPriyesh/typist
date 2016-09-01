package me.priyesh.typist

import me.priyesh.typist.WordOps.Word
import monix.execution.Ack.Continue
import monix.execution.Cancelable
import monix.reactive.{Observable, OverflowStrategy}
import org.scalajs.dom
import org.scalajs.dom.{EventTarget, KeyboardEvent}

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.jQuery
import monix.execution.Scheduler.Implicits.global

@JSExport
object App extends JSApp {

  object Page {

    import scalatags.JsDom.all._

    def body =
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

    val words: List[Word] = WordOps.from("mist enveloped the ship three hours out from port the face of the moon was in shadow a shining crescent far beneath the flying vessel".split(" ").toList)

    jQuery("#words-container").append(words.map(WordOps.html).mkString(" "))

    val input = dom.window.document.getElementById("input")
    // h, e, l, l, o,  , w, o, r, l, d,  ,
    // hello, world
    val keyObservable = keyPresses(input)
    keyObservable
      .subscribe(s => {
        println(s)
        Continue
      })

  }

  def handleKeyPress(expected: String, input: String) = {
    input match {
      case "Backspace" =>
      case " " =>
      case i if i == expected =>
      case _ =>
    }
  }

  def keyPresses(target: EventTarget): Observable[String] = {
    def permittedKeyCode(c: Int): Boolean = c >= 65 && c <= 90 || c == 8 || c == 32
    Observable.create[KeyboardEvent](OverflowStrategy.Unbounded)(subscriber => {
      val name = "keyup"
      val listener = (e: KeyboardEvent) => {
        subscriber.onNext(e)
      }
      target.addEventListener(name, listener)
      Cancelable(() => target.removeEventListener(name, listener))
    }).filter(e => permittedKeyCode(e.keyCode)).map(_.key)
  }
}