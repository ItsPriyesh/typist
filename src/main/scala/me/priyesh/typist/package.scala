package me.priyesh

package object typist {

  case class Word(string: String, highlight: Highlight = Default) {
    lazy val html: String = s"<span style='color: ${highlight.color}'>$string</span>"
  }

  sealed trait Highlight { def color: String }

  case object Default extends Highlight {
    def color: String = "#abb2bf"
  }

  case object Correct extends Highlight {
    def color: String = "#98c379"
  }

  case object Incorrect extends Highlight {
    def color: String = "#e06c75"
  }
}
