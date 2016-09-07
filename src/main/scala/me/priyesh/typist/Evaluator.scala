package me.priyesh.typist

import monix.reactive.Observable

sealed trait Result[A]
case object Pending extends Result[Nothing]
case class Success[A](expected: A) extends Result[A]
case class Failure[A](expected: A, actual: A) extends Result[A]

trait Evaluator[T] {
  def evaluate(exp: T, act: T): Result[T]
  def run(exp: List[T], act: Observable[T]): Observable[Result[T]] = Observable.fromIterable(exp).zipWith(act)(evaluate)
}

object StringEvaluator extends Evaluator[String] {
  def evaluate(exp: String, act: String): Result[String] = if (exp == act) Success(exp) else Failure(exp, act)
}