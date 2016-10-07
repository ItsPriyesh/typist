package me.priyesh

import org.scalajs.dom.raw.Element

import scala.scalajs.js

package object typist {

  case class Binder[A <: Element, B](elem: A, private val r: B => String) {
    def bind(b: B): Unit = elem.innerHTML = r(b)
  }

  def elemById(id: String): Element = org.scalajs.dom.document.getElementById(id)
  def cssPxAttribute(elem: Element, attr: String): Double =
    org.scalajs.jquery.jQuery(elem).css(attr).stripSuffix("px").toDouble
  def calcChildTopOffset(parent: Element, childIdx: Int): Double =
    org.scalajs.jquery.jQuery(parent.children(childIdx)).position().asInstanceOf[Position].top

  def debug[A](a: A): A = {
    println(a.toString)
    a
  }

  @js.native
  trait Position extends js.Object { val top: Double = js.native }
}
