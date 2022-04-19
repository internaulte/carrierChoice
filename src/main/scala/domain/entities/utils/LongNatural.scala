package domain.entities.utils

import domain.entities.utils.Types.LongNatural
import zio.prelude.{Assertion, Subtype}

object LongNatural extends Subtype[Long] {

  override inline def assertion: Assertion[Long] =
    Assertion.greaterThanOrEqualTo(0L)

  val one: LongNatural =
    LongNatural(1)

  val zero: LongNatural =
    LongNatural(0)

  def successor(n: LongNatural): LongNatural =
    wrap(n + 1)

  def times(x: LongNatural, y: LongNatural): LongNatural = {
    val product = x * y
    if (x == 0 || product / x != y) LongNatural(Long.MaxValue) else wrap(product)
  }

  def plus(x: LongNatural, y: LongNatural): LongNatural = {
    val sum = x + y
    if (sum < 0) LongNatural(Long.MaxValue) else wrap(sum)
  }

  def minus(x: LongNatural, y: LongNatural): LongNatural = {
    val difference = x - y
    if (difference < 0) zero else wrap(difference)
  }

  def unsafeMake(n: Long): LongNatural =
    wrap(n)
}
