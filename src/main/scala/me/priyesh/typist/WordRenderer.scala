package me.priyesh.typist

import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.reactive.Observer

import scala.concurrent.Future
import org.scalajs.jquery.jQuery

class WordRenderer(containerSelector: String, strings: Array[String]) extends Observer[Result[String]] {

  val container = jQuery(containerSelector)
  val renderCache: Array[String] = strings.map(toSpan(_, Default))
  var index = 0
  render()

  override def onError(ex: Throwable): Unit = ()
  override def onComplete(): Unit = ()
  override def onNext(result: Result[String]): Future[Ack] = {
    renderCache(index) = result match {
      case Success(e) => println(s"success: $e"); toSpan(e, Correct)
      case Failure(e, a) => println(s"failure: ($e, $a)"); toSpan(e, Incorrect)
    }

    index += 1
    render()
    Continue
  }

  def toSpan(str: String, state: State): String = s"<span style='color: ${state.color}'>$str</span>"

  def render() = {
    container.empty()
    container.append(renderCache.mkString(" "))
  }

  sealed trait State { def color: String }

  case object Default extends State {
    def color: String = "#abb2bf"
  }

  case object Correct extends State {
    def color: String = "#98c379"
  }

  case object Incorrect extends State {
    def color: String = "#e06c75"
  }
}
