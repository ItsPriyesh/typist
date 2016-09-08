package me.priyesh.typist

import monix.eval.Coeval
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.jquery._
import monix.execution.Scheduler.Implicits.global

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

  private lazy val WordsContainer = jQuery("#words-container")
  private lazy val Input = jQuery("#input").get().head.asInstanceOf[HTMLInputElement]

  @JSExport
  def main() = {
    dom.document.body appendChild Page.body.render

    val words = WordSource.get

    (Pending +: StringEvaluator.run(words, new InputEmitter(Input)))
      .scan(Coeval(Words(words.map(Word(_))))) { (words, result) =>
        result match {
          case Success(_) => words advance Correct
          case Failure(_, _) => words advance Incorrect
          case Pending => words
        }
      }.foreach(bind)
  }

  private def bind(words: Words): Unit = {
    WordsContainer.empty()
    WordsContainer.append(words.render)
  }

}