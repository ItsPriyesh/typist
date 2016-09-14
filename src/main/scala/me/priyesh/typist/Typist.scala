package me.priyesh.typist

import monix.eval.Coeval
import monix.execution.Ack.Continue
import monix.execution.Scheduler
import monix.reactive.Observable
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.duration._

@JSExport
object Typist extends JSApp {

  private lazy val WordContainer = dom.document.getElementById("words-container")
  private lazy val Input = dom.document.getElementById("input").asInstanceOf[HTMLInputElement]
  private val Words = WordSource.get
  private val Duration = 10 seconds

  @JSExport
  def main() = {
    val initialState = WordsState(Words)
    bind(initialState)

    StringEvaluator
      .run(Words, new InputEmitter(Input))
      .scan(Coeval(initialState)) { _ advance _ }
      .doOnNext(bind)
      .takeUntil(Observable.evalDelayed(Duration, ()))
      .lastF
      .map(s => Calculator.netWordsPerMinute(s.mapRes(_.string), Duration))
      .subscribe(wpm => {
        println("wpm = " + wpm)
        Continue
      })(Scheduler.global)
  }

  private def bind(state: WordsState): Unit = WordContainer.innerHTML = state.render

}