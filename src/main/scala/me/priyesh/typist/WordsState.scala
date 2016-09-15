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

  def mapRes[A](f: Word => A): List[Result[A]] = completed map { _ map f }

  // TODO: Partially cache the result
  def render: String = completed.map(_.expected) ++ remaining map(_.html) mkString " "
  override def toString: String = render
}