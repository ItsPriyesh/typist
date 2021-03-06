package me.priyesh.typist

import monix.execution.Cancelable
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.observers.Subscriber
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.dom.KeyboardEvent

final case class InputEmitter(target: HTMLInputElement, onStart: () => Unit) extends Observable[String] {

  override def unsafeSubscribeFn(subscriber: Subscriber[String]): Cancelable =
    keyboardEvents(target)
      .doOnStart(_ => onStart())
      .filter(_.key == " ")
      .map(_ => {
        // TODO: Fix race:
        // Space followed immediately by next char will include the next char in previous word
        // ie: Target is not cleared before next word
        val text = target.value.trim
        target.value = ""
        text
      })
      .unsafeSubscribeFn(subscriber)

  private def keyboardEvents(target: HTMLInputElement, eventType: String = "keydown"): Observable[KeyboardEvent] =
    Observable.create[KeyboardEvent] (OverflowStrategy.Unbounded) { subscriber =>
      val listener = (e: KeyboardEvent) => subscriber.onNext(e)
      target.addEventListener(eventType, listener)
      Cancelable(() => target.removeEventListener(eventType, listener))
    }
}
