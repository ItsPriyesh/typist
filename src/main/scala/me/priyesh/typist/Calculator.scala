package me.priyesh.typist

import scala.concurrent.duration.Duration

object Calculator {

  case class Report(wpm: Int, words: (Int, Int)) {
    def render: String = s"WPM: $wpm\nCorrect Words: ${words._1}\nIncorrect Words: ${words._2}"
  }

  def report(results: Seq[Result[String]], duration: Duration): Report = {
    def wpm(chars: Int, mistakes: Int, minutes: Double): Int = Math.round((chars / 5.0 - mistakes) / minutes).toInt

    val (letters, failedWords) = results.foldLeft(0, 0) {
      (m, n) => n match {
        case Success(e) => m.copy(_1 = m._1 + e.length)
        case Failure(e, a) => (m._1 + a.length, m._2 + 1)
      }
    }

    val spaces = results.size - 1
    val totalChars = letters + spaces

    Report(wpm(totalChars, failedWords, duration.toSeconds / 60.0), (results.size - failedWords, failedWords))
  }
}
