package me.priyesh.typist

import monix.execution.Ack.Continue
import monix.execution.{Ack, Cancelable}
import monix.reactive.{Observable, Observer, OverflowStrategy}
import org.scalajs.dom
import org.scalajs.dom.{EventTarget, KeyboardEvent}

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import monix.execution.Scheduler.Implicits.global
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.jquery._

import scala.concurrent.Future

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

    val words = "mist enveloped the ship three hours out from port the face of the moon was in shadow a shining crescent far beneath the flying vessel".split(" ").toList
    val input = dom.window.document.getElementById("input").asInstanceOf[HTMLInputElement]

    jQuery("#words-container").append(words.mkString(" "))
    StringEvaluator.run(words, new InputEmitter(input)).subscribe { result =>
      result match {
        case Success(s) => println("success " + s)

        case Failure(s) => println("failure " + s)
      }
      input.value = ""
      Continue
    }
  }

  class WordRenderer(containerSelector: String, words: List[String]) extends Observer[Result[String]] {

    override def onNext(elem: Result[String]): Future[Ack] = {
      Continue
    }

    

    override def onError(ex: Throwable): Unit = ()
    override def onComplete(): Unit = ()
  }
}