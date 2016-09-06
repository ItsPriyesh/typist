package me.priyesh.typist

import monix.execution.Cancelable
import monix.reactive.Observable
import monix.reactive.observers.Subscriber
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.dom.KeyboardEvent

final class InputEmitter(target: HTMLInputElement, eventType: String = "keyup") extends Observable[String] {
  override def unsafeSubscribeFn(subscriber: Subscriber[String]): Cancelable = {
    Observable.unsafeCreate[String] { subscriber =>
      val listener = (e: KeyboardEvent) => {
        if (e.key == " ") subscriber.onNext(target.value.trim)
      }
      target.addEventListener(eventType, listener)
      Cancelable(() => target.removeEventListener(eventType, listener))
    }.unsafeSubscribeFn(subscriber)
  }
}
