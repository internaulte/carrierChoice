package domain.entities.utils

import domain.entities.utils.Types.Minute
import zio.prelude.{Assertion, Subtype}

object Minute extends Subtype[Int] {

  override inline def assertion: Assertion[Int] =
    Assertion.between(0, 59)

  val one: Minute =
    Minute(1)

  val zero: Minute =
    Minute(0)

  def unsafeMake(n: Int): Minute =
    wrap(n)
}