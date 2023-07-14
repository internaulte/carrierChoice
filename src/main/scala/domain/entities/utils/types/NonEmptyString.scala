package domain.entities.utils.types

object NonEmptyString {
  opaque type NonEmptyString <: String = String

  def apply(string: String): Option[NonEmptyString] = {
    if (string.nonEmpty) Some(string)
    else None
  }

  def unsafe(string: String): NonEmptyString = string
}
