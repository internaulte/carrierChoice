package domain.entities.utils.types

import scala.quoted.*

object NonZeroNaturalInt {
  opaque type NonZeroNaturalInt <: Int = Int

  def apply(value: Int): Option[NonZeroNaturalInt] =
    if (value >= 1) Some(value)
    else None

  def unsafe(value: Int): NonZeroNaturalInt = value

  val one: NonZeroNaturalInt = NonZeroNaturalInt.unsafe(1)

  val max: NonZeroNaturalInt = NonZeroNaturalInt.unsafe(Int.MaxValue)
}
