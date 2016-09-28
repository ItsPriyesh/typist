package me.priyesh.typist

import monix.reactive.Observable

sealed trait Result[+A] {
  def expected: A
  final def map[B](f: A => B): Result[B] = this match {
    case Success(e) => Success(f(e))
    case Failure(e, a) => Failure(f(e), f(a))
  }
}

case class Success[+A](expected: A) extends Result[A]
case class Failure[+A](expected: A, actual: A) extends Result[A]

object Evaluator {
  private def evaluate[A](exp: A, act: A): Result[A] = if (exp == act) Success(exp) else Failure(exp, act)
  def run[A](exp: Seq[A], act: Observable[A]): Observable[Result[A]] = Observable.fromIterable(exp).zipWith(act)(evaluate)
}