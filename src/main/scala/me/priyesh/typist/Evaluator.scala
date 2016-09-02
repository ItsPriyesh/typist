package me.priyesh.typist

import monix.reactive.Observable

object Evaluator {

  sealed trait Result
  case class Success(expected: String) extends Result
  case class Failure(expected: String) extends Result

  case class Word(actual: String, expected: String) {
    def evaluate: Result = if (actual == expected) Success(expected) else Failure(expected)
  }

  def run(expected: List[String], emitter: Observable[String]): Observable[Result] = {
    emitter
      .takeWhile(_ != " ")
      .reduce(_ + _)
      .map(Word(_, expected head))
      .flatMap(word => Observable.concat(Observable.pure(word.evaluate), run(expected tail, emitter)))
  }
}
