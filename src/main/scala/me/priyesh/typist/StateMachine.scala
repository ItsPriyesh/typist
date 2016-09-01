package me.priyesh.typist

object StateMachine {

  sealed trait State
  case object Pending
  case object Successful
  case object Failed

  case class Word(current: String, expected: String, state: State)

}
