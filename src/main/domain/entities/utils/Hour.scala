package domain.entities.utils

import domain.entities.utils.LongNatural.wrap
import domain.entities.utils.Types.Hour
import zio.prelude.{Assertion, Subtype}

object Hour extends Subtype[Int] {

  override inline def assertion: Assertion[Int] =
    Assertion.between(0, 23)

  val one: Hour =
    Hour(1)

  val zero: Hour =
    Hour(0)

  def unsafeMake(n: Int): Hour =
    wrap(n)
}
