package me.priyesh.typist

object Calculator {
  def netWordsPerMinute(results: List[Result[String]], seconds: Int): Int = {
    val (totalChars, incorrectWords) = results.foldLeft((0, 0)) {
      (m, r) => r match {
        case Success(s) => m.copy(_1 = s.length + 1)
        case Failure(e, _) => (e.length + 1,  m._2 + 1)
      }
    }
    (((totalChars / 5.0) - incorrectWords) / (seconds / 60.0)) toInt
  }
}
