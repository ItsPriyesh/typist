package me.priyesh.typist

import java.util.concurrent.TimeUnit

import monix.eval.Coeval
import monix.reactive.observables.ObservableLike._
import org.scalajs.dom.raw.{Element, HTMLInputElement}

import scala.concurrent.duration.{Duration, FiniteDuration}
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import monix.reactive.observables.ConnectableObservable

final class Engine(container: Binder[Element, WordsState],
                   status: Binder[Element, Any],
                   input: HTMLInputElement,
                   duration: FiniteDuration) {

  val words = WordSource.takeFor(duration)
  val initialState = WordsState(words)
  val timer = countdownFrom(status, duration)

  val src = Evaluator
    .run(words, InputEmitter(input, onStart = () => timer.connect()))
    .scan(Coeval pure initialState)(_ advance _)
    .doOnNext(container bind)
    .takeUntil(timer)
    .publish

  src
    .lastF
    .map(ws => s"Net WPM = ${Calculator.wpm(ws.results, duration)}")
    .foreach(status bind)

  src
    .map(ws => calcChildTopOffset(container.elem, ws.index))
    .liftByOperator(new FilterNotByFirstOperator[Double])
    .foreach(_ => container.elem.scrollTop += cssPxAttribute(container.elem, "line-height"))

  def start() = {
    container bind initialState
    src.connect()
  }

  private def countdownFrom(binder: Binder[_, Any], from: Duration): ConnectableObservable[Long] = Observable
    .interval(Duration(1, TimeUnit.SECONDS))
    .map(from.toSeconds - _)
    .doOnNext(binder bind)
    .takeWhile(_ != 0)
    .lastF
    .publish
}

private final class FilterNotByFirstOperator[A] extends Operator[A, A] {

  import scala.concurrent.Future
  import scala.util.control.NonFatal
  import monix.execution.Ack.{Continue, Stop}
  import monix.execution.Ack
  import monix.reactive.observers.Subscriber

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
