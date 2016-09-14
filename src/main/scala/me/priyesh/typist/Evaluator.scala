package me.priyesh.typist

import monix.reactive.Observable

sealed trait Result[+A] {
  def expected: A
  def map[B](f: A => B): Result[B] = this match {
    case Success(e) => Success(f(e))
    case Failure(e, a) => Failure(f(e), f(a))
  }
}
case class Success[+A](expected: A) extends Result[A]
case class Failure[+A](expected: A, actual: A) extends Result[A]

trait Evaluator[A] {
  def evaluate(exp: A, act: A): Result[A]
  def run(exp: List[A], act: Observable[A]): Observable[Result[A]] = Observable.fromIterable(exp).zipWith(act)(evaluate)
}

object StringEvaluator extends Evaluator[String] {
  def evaluate(exp: String, act: String): Result[String] = if (exp == act) Success(exp) else Failure(exp, act)
}