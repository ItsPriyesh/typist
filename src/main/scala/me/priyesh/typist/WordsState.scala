package me.priyesh.typist

object WordsState {
  def apply(remaining: List[String]): WordsState = new WordsState(Nil, remaining.map(Word(_)))
}

class WordsState(val completed: List[Result[Word]] = Nil, val remaining: List[Word]) {
  def advance(result: Result[String]): WordsState =
    if (remaining isEmpty) {
      this
    } else {
      new WordsState(completed :+ result.map(Word(_, result match {
        case Success(_) => Correct
        case Failure(_, _) => Incorrect
      })), remaining.tail)
    }

  def index: Int = completed.length

  def results: List[Result[String]] = completed map { _ map { _.string } }

  // TODO: Partially cache the result
  def render: String = completed.map(_.expected) ++ (remaining.head.copy(highlight = Pending) :: remaining.tail) map(_.html) mkString " "
}

case class Word(string: String, highlight: Highlight = Default) {
  lazy val html: String = s"<span style='color: ${highlight.toColor}'>$string</span>"
}

case object Default extends Highlight
case object Correct extends Highlight
case object Incorrect extends Highlight
case object Pending extends Highlight

sealed trait Highlight {
  def toColor: String = this match {
    case Default => "#abb2bf"
    case Correct => "#98c379"
    case Incorrect => "#e06c75"
    case Pending => "#61afef"
  }
}