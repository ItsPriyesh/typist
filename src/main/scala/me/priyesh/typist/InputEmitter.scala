package me.priyesh.typist

import monix.execution.Cancelable
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.observers.Subscriber
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.dom.KeyboardEvent

final class InputEmitter(target: HTMLInputElement) extends Observable[String] {
  override def unsafeSubscribeFn(subscriber: Subscriber[String]): Cancelable =
    keyboardEvents(target)
      .filter(_.key == " ")
      .map(_ => {
        val text = target.value.trim
        target.value = ""
        text
      })
      .unsafeSubscribeFn(subscriber)

  private def keyboardEvents(target: HTMLInputElement, eventType: String = "keyup"): Observable[KeyboardEvent] =
    Observable.create[KeyboardEvent] (OverflowStrategy.Unbounded) { subscriber =>
      val listener = (e: KeyboardEvent) => subscriber.onNext(e)
      target.addEventListener(eventType, listener)
      Cancelable(() => target.removeEventListener(eventType, listener))
    }
}
