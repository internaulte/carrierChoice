package domain.entities.utils.types

import zio.prelude.newtypes.Natural
import zio.prelude.{Assertion, Subtype}

import scala.quoted.*

object NonZeroNaturalInt extends Subtype[Int] {

  override inline def assertion = Assertion.greaterThanOrEqualTo(1)

  val one: NonZeroNaturalInt = NonZeroNaturalInt(1)

  val max: NonZeroNaturalInt = NonZeroNaturalInt(Int.MaxValue)

  def divides(numerator: NonZeroNaturalInt, denominator: NonZeroNaturalInt): NonZeroNaturalInt = {
    val division = numerator / denominator
    wrap(division)
  }
}
