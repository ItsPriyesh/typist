package me.priyesh.typist

import monix.eval.Coeval
import monix.execution.Scheduler
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.jquery.{JQuery, _}

trait Renderer {
  val Input: HTMLInputElement
  val WordContainer: JQuery

  def bind(state: WordsState): Unit
  def start(strings: List[String])(f: (WordsState, Result) => WordsState): Unit =
    (Pending +: StringEvaluator.run(strings, new InputEmitter(Input)))
      .scan(Coeval(WordsState(strings)))(f)
      .foreach(bind)(Scheduler.global)
}

class TypistRenderer extends Renderer {
  override lazy val WordContainer = jQuery("#words-container")
  override lazy val Input = dom.document.getElementById("input").asInstanceOf[HTMLInputElement]

  override def bind(state: WordsState): Unit = {
    WordContainer empty()
    WordContainer append state.render
  }
}