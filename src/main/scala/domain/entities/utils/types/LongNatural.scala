package domain.entities.utils.types

import zio.prelude.{Assertion, Subtype}

object LongNatural extends Subtype[Long] {

  override inline def assertion: Assertion[Long] =
    Assertion.greaterThanOrEqualTo(0L)

  val one: LongNatural =
    LongNatural(1)

  val zero: LongNatural =
    LongNatural(0)

  def times(x: LongNatural, y: LongNatural): LongNatural = {
    val product = x * y
    if (x == 0 || product / x != y) LongNatural(Long.MaxValue) else wrap(product)
  }

  def divides(x: LongNatural, y: LongNatural): LongNatural = {
    val division = x / y
    if (x == 0 || division * y != x) LongNatural(Long.MaxValue) else wrap(division)
  }

  def plus(x: LongNatural, y: LongNatural): LongNatural = {
    val sum = x + y
    if (sum < 0) LongNatural(Long.MaxValue) else wrap(sum)
  }

  def minus(x: LongNatural, y: LongNatural): LongNatural = {
    val difference = x - y
    if (difference < 0) zero else wrap(difference)
  }

  def max(x: LongNatural, y: LongNatural): LongNatural = {
    if (x < y) y else x
  }
}
