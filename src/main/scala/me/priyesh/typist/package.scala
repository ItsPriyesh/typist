package me.priyesh

import org.scalajs.dom.raw.Element

package object typist {

  case class Binder[A <: Element, B](elem: A, private val r: B => String) {
    def bind(b: B): Unit = elem.innerHTML = r(b)
  }

  case class Word(string: String, highlight: Highlight = Default) {
    lazy val html: String = s"<span style='color: ${highlight.toColor}'>$string</span>"
  }

  sealed trait Highlight {
    def toColor: String = this match {
      case Default =>"#abb2bf"
      case Correct =>"#98c379"
      case Incorrect => "#e06c75"
    }
  }

  case object Default extends Highlight
  case object Correct extends Highlight
  case object Incorrect extends Highlight

  def elemById(id: String): Element = org.scalajs.dom.document.getElementById(id)
  def cssPxAttribute(elem: Element, attr: String): Double =
    org.scalajs.jquery.jQuery(elem).css(attr).stripSuffix("px").toDouble
}
