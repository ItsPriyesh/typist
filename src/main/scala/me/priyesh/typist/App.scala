package me.priyesh.typist

import monix.execution.Ack.Continue
import monix.execution.{Ack, Cancelable}
import monix.reactive.{Observable, Observer, OverflowStrategy}
import org.scalajs.dom
import org.scalajs.dom.{EventTarget, KeyboardEvent}

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future

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

    val words: List[String] = "mist enveloped the ship three hours out from port the face of the moon was in shadow a shining crescent far beneath the flying vessel".split(" ").toList


    val input = dom.window.document.getElementById("input")
    StringEvaluator.run(words, keyPresses(input), _ == _).subscribe(result => {
      result match {
        case Success(s) => println("Success")
        case Failure(s) => println("Failure")
      }
      Continue
    })

  }

  class WordRenderer(containerSelector: String, words: List[String]) extends Observer[Result[String]] {
    import org.scalajs.jquery.jQuery

    jQuery(containerSelector).append(words.mkString(" "))

    override def onNext(elem: Result[String]): Future[Ack] = {
      Continue
    }

    

    override def onError(ex: Throwable): Unit = ()
    override def onComplete(): Unit = ()
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