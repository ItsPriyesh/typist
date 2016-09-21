package me.priyesh.typist

import scala.concurrent.duration.Duration

object Calculator {
  def netWordsPerMinute(results: List[Result[String]], duration: Duration): Int = {
    val (letters, failedWords) = results.foldLeft(0, 0) {
      (m, n) => n match {
        case Success(e) => m.copy(_1 = m._1 + e.length)
        case Failure(e, a) => (m._1 + a.length, m._2 + 1)
      }
    }

    val spaces = results.size - 1
    val totalChars = letters + spaces

    (((totalChars / 5.0) - failedWords) / duration.toMinutes.toDouble).toInt
  }
}
