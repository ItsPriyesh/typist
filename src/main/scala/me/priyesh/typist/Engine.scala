package me.priyesh.typist

import monix.eval.Coeval
import monix.execution.Ack
import monix.execution.Ack.{Continue, Stop}
import monix.reactive.observables.ObservableLike._
import monix.reactive.observers.Subscriber
import org.scalajs.dom.raw.{Element, HTMLInputElement}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.control.NonFatal
import monix.execution.Scheduler.Implicits.global

final class Engine(container: Binder[Element, WordsState],
                   countdown: Binder[Element, Long],
                   input: HTMLInputElement,
                   duration: FiniteDuration,
                   words: List[String]) {

  val initialState = WordsState(words)
  val timer = countdownFrom(countdown, duration)

  val src = Evaluator
    .run(words, InputEmitter(input, onStart = () => timer.connect()))
    .scan(Coeval pure initialState)(_ advance _)
    .doOnNext(container bind)
    .takeUntil(timer)
    .publish

  src
    .lastF
    .map(s => Calculator.netWordsPerMinute(s.mapRes(_.string), duration))
    .foreach(wpm => println("wpm = " + wpm))

  src
    .map(ws => calcChildTopOffset(container.elem, ws.index))
    .liftByOperator(new FilterNotByFirstOperator[Double])
    .foreach(_ => container.elem.scrollTop += cssPxAttribute(container.elem, "line-height"))

  def start() = {
    container bind initialState
    src.connect()
  }
}

private final class FilterNotByFirstOperator[A] extends Operator[A, A] {
  override def apply(out: Subscriber[A]): Subscriber[A] =
    new Subscriber[A] {
      implicit val scheduler = out.scheduler
      private var isFirst = true
      private var firstItem: A = _

      override def onNext(elem: A): Future[Ack] = {
        try {
          if (isFirst) {
            isFirst = false
            firstItem = elem
          } else if (elem != firstItem) {
            out.onNext(elem)
          }
          Continue
        } catch {
          case NonFatal(ex) =>
            onError(ex)
            Stop
        }
      }

      override def onError(ex: Throwable): Unit = out.onError(ex)
      override def onComplete(): Unit = out.onComplete()
    }
}
