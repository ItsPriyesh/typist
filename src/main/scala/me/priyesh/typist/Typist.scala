package me.priyesh.typist

import monix.eval.Coeval
import monix.execution.Ack.Continue
import monix.reactive.Observable
import org.scalajs.dom.raw.{Element, HTMLInputElement}
import monix.execution.Scheduler.Implicits.global

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.concurrent.duration._
import org.scalajs.jquery.{JQuery, jQuery}
import FilterNotByFirstOperator._

import scala.scalajs.js

@JSExport
object Typist extends JSApp {

  @JSExport
  def main() = {
    val wordContainer = Binder(elemById("words-container"), (ws: WordsState) => ws.render)
    val countdown = Binder(elemById("countdown"), (l: Long) => l.toString)
    val input = elemById("input").asInstanceOf[HTMLInputElement]
    val duration = 60 seconds
    val words = WordSource.get
    val initialState = WordsState(words)

    wordContainer bind initialState

    val src = Evaluator
      .run(words, new InputEmitter(input, onStart = countdownFrom(countdown, duration)))
      .scan(Coeval.pure(initialState))(_ advance _)
      .publish

    src
      .doOnNext(wordContainer bind)
      .takeUntil(Observable.evalDelayed(duration, ()))
      .lastF
      .map(s => Calculator.netWordsPerMinute(s.mapRes(_.string), duration))
      .foreach(wpm => println("wpm = " + wpm))

    src
      .map(ws => calcChildTopOffset(wordContainer.elem, ws.index))
      .filterNotByFirst
      .foreach(_ => {
        val lineHeight = jQuery(wordContainer.elem).css("line-height").stripSuffix("px").toDouble
        wordContainer.elem.scrollTop += lineHeight
      })

    src.connect()
  }

  def calcChildTopOffset(parent: Element, childIdx: Int): Double =
    jQuery(parent.children(childIdx)).position().asInstanceOf[Position].top

  @js.native
  trait Position extends js.Object {
    val top: Double = js.native
    val left: Double = js.native
  }

  private def countdownFrom(binder: Binder[_, Long], duration: Duration): Unit = {
    Observable.range(duration.toSeconds - 1, 0, -1)
      .doOnSubscribe(binder bind duration.toSeconds)
      .delayOnNext(1 second)
      .foreach(binder bind)
  }
}