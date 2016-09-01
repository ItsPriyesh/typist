package me.priyesh.typist

object WordOps {

  sealed trait State {
    val color: String
  }

  case object Default extends State { val color = "#abb2bf" }
  case object Selected extends State { val color = "#61afef" }
  case object Success extends State { val color = "#98c379" }
  case object Error extends State { val color = "#e06c75" }

  case class Word(str: String, state: State)

  def from(xs: List[String]): List[Word] = xs map { Word(_, Default) }

  def html(w: Word): String = s"<span style='color: ${w.state.color}'>${w.str}</span>"
}
