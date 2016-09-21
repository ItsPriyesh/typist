package me.priyesh.typist

import monix.execution.Ack.{Continue, Stop}
import monix.execution.Ack
import monix.reactive.Observable
import monix.reactive.observables.ObservableLike.Operator
import monix.reactive.observers.Subscriber

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * Returns an Observable that skips the first item emitted by the source
  * and then emits future items that are not equal to the first item.
  *
  * ie: 1, 2, 1, 3, 1, 4 -> 2, 3, 4
  */
final class FilterNotByFirstOperator[A] extends Operator[A, A] {
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

object FilterNotByFirstOperator {
  implicit class Extension[A](o: Observable[A]) {
    def filterNotByFirst: Observable[A] = o.liftByOperator(new FilterNotByFirstOperator[A])
  }
}
