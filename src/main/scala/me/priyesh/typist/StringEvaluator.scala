package me.priyesh.typist

import monix.reactive.Observable

sealed trait Result[A] {
  def expected: A
}

case class Success[A](expected: A) extends Result[A]
case class Failure[A](expected: A) extends Result[A]

trait Evaluator {
  type T
  def run(expected: List[T], actual: Observable[T], eval: (T, T) => Boolean): Observable[Result[T]]
}

object StringEvaluator extends Evaluator {
  type T = String
  def run(expected: List[T], emitter: Observable[T], eval: (T, T) => Boolean): Observable[Result[T]] =
    emitter
      .takeWhile(_ != " ")
      .reduce(_ + _)
      .map((expected head, _))
      .flatMap(i => {
        val res = if (eval tupled i) Success(i._1) else Failure(i._1)
        Observable.concat(Observable(res), run(expected tail, emitter, eval))
      })
}
