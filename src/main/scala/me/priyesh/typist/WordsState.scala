package me.priyesh.typist

object WordsState {
  def apply(remaining: Seq[String]): WordsState = new WordsState(Nil, remaining.map(Word(_)))
}

class WordsState(val completed: Seq[Result[Word]] = Nil, val remaining: Seq[Word]) {
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

  def results: Seq[Result[String]] = completed map { _ map { _.string } }

  // TODO: Partially cache the result
  def render: String = completed.map(_.expected) ++ remaining map(_.html) mkString " "
}