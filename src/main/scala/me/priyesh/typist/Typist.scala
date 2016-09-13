package me.priyesh.typist

import monix.eval.Coeval
import monix.execution.Ack.Continue
import monix.execution.Scheduler
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object Typist extends JSApp {

  private lazy val WordContainer = dom.document.getElementById("words-container")
  private lazy val Input = dom.document.getElementById("input").asInstanceOf[HTMLInputElement]
  private val Words = WordSource.get

  @JSExport
  def main() = {
    val initialState = WordsState(Words)
    bind(initialState)

    StringEvaluator
      .run(Words, new InputEmitter(Input))
      .scan(Coeval(initialState)) { _ advance _ }
      .doOnNext(bind)
      .takeLast(1)
      .map(state => {
        val s = state.completed.map(s => s.map(_.string))
        Calculator.netWordsPerMinute(s, 50)
      })
      .subscribe(wpm => {
        println("wpm = " + wpm)
        Continue
      })(Scheduler.global)
  }

  private def bind(state: WordsState): Unit = WordContainer.innerHTML = state.render
}