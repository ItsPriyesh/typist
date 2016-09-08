package me.priyesh

package object typist {

  object WordsState {
    def apply(remaining: List[String]): WordsState = new WordsState(Nil, remaining.map(Word(_)))
  }

  class WordsState(val completed: List[Word] = Nil, val remaining: List[Word]) {
    def advance(newHighlight: Highlight): WordsState =
      if (remaining isEmpty) this
      else new WordsState(completed :+ remaining.head.copy(highlight = newHighlight), remaining.tail)

    // TODO: Partially cache the result
    def render: String = (completed ++ remaining).map(_.html).mkString(" ")
  }

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
