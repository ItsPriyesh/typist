package me.priyesh.typist

import monix.eval.Coeval
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
      .foldLeftL(Coeval(Words(remaining = words.map(Word(_)).toList))) { (words, result) =>
        val advanced = words.advance(result match {
          case Success(s) => Correct
          case Failure(e, a) => Incorrect
        })
        jQuery("#words-container").empty()
        jQuery("#words-container").append(words.render)
        advanced
      }.runAsync
    // .subscribe(new WordRenderer("#words-container", words))
  }

  case class Words(completed: List[Word] = Nil, remaining: List[Word]) {
    def advance(newState: State): Words =
      if (remaining isEmpty) this else Words(completed :+ remaining.head.copy(state = newState), remaining.tail)

    def render: String = (completed ++ remaining).map(_.render).mkString(" ")
  }

  case class Word(string: String, state: State = Default) {
    lazy val render: String = s"<span style='color: ${state.color}'>$string</span>"
  }

  sealed trait State {
    def color: String
  }

  case object Default extends State {
    def color: String = "#abb2bf"
  }

  case object Correct extends State {
    def color: String = "#98c379"
  }

  case object Incorrect extends State {
    def color: String = "#e06c75"
  }

}