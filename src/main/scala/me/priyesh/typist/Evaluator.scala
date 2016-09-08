package me.priyesh.typist

import monix.reactive.Observable

sealed trait Result
case object Pending extends Result
case object Success extends Result
case object Failure extends Result

trait Evaluator[T] {
  def evaluate(exp: T, act: T): Result
  def run(exp: List[T], act: Observable[T]): Observable[Result] = Observable.fromIterable(exp).zipWith(act)(evaluate)
}

object StringEvaluator extends Evaluator[String] {
  def evaluate(exp: String, act: String): Result = if (exp == act) Success else Failure
}