package me.priyesh

package object typist {

  object Words {
    def apply(remaining: List[Word]): Words = new Words(Nil, remaining)
  }

  class Words(val completed: List[Word] = Nil, val remaining: List[Word]) {
    def advance(newState: State): Words =
      if (remaining isEmpty) this else new Words(completed :+ remaining.head.copy(state = newState), remaining.tail)

    // TODO: Partially cache the result
    def render: String = (completed ++ remaining).map(_.html).mkString(" ")
  }

  case class Word(string: String, state: State = Default) {
    lazy val html: String = s"<span style='color: ${state.color}'>$string</span>"
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
