package me.priyesh.typist

import monix.eval.Coeval
import monix.execution.Ack.Continue
import monix.reactive.Observable
import org.scalajs.dom
import org.scalajs.dom.raw.{Element, HTMLInputElement}
import monix.execution.Scheduler.Implicits.global

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.duration._

@JSExport
object Typist extends JSApp {

  private lazy val WordContainer = dom.document.getElementById("words-container")
  private lazy val Input = dom.document.getElementById("input").asInstanceOf[HTMLInputElement]
  private lazy val Countdown = dom.document.getElementById("countdown")
  private val Words = WordSource.get
  private val Duration = 60 seconds

  @JSExport
  def main() = {
    val initialState = WordsState(Words)
    WordContainer bind initialState

    StringEvaluator
      .run(Words, new InputEmitter(Input, onStart = () => countdownFrom(Duration)))
      .scan(Coeval(initialState))(_ advance _)
      .doOnNext(WordContainer bind _)
      .takeUntil(Observable.evalDelayed(Duration, ()))
      .lastF
      .map(s => Calculator.netWordsPerMinute(s.mapRes(_.string), Duration))
      .subscribe(wpm => {
        println("wpm = " + wpm)
        Continue
      })
  }

  private def countdownFrom(duration: Duration): Unit = {
    Observable.range(duration.toSeconds - 1, 0, -1)
      .doOnSubscribe(Countdown bind duration.toSeconds)
      .delayOnNext(1 second)
      .foreach(Countdown bind _)
  }

  implicit class ElementBinder(val element: Element) extends AnyVal {
    def bind(s: => Any): Unit = element.innerHTML = s.toString
  }

}