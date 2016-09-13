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

  // TODO: Partially cache the result
  def render: String = completed.collect {
    case Success(e) => e; case Failure(e, _) => e
  } ++ remaining map { _.html } mkString " "
}