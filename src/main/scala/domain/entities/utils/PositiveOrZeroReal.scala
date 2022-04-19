package domain.entities.utils

import domain.entities.utils.Types.PositiveOrZeroReal
import zio.prelude.{Assertion, Subtype}

object PositiveOrZeroReal extends Subtype[Double] {

  override inline def assertion: Assertion[Double] =
    Assertion.greaterThanOrEqualTo(0d)

  val one: PositiveOrZeroReal =
    PositiveOrZeroReal(1)

  val zero: PositiveOrZeroReal =
    PositiveOrZeroReal(0)

  def successor(n: PositiveOrZeroReal): PositiveOrZeroReal =
    wrap(n + 1)

  def times(x: PositiveOrZeroReal, y: PositiveOrZeroReal): PositiveOrZeroReal = {
    val product = x * y
    if (x == 0 || product / x != y) PositiveOrZeroReal(Double.MaxValue) else wrap(product)
  }

  def divides(x: PositiveOrZeroReal, y: PositiveOrZeroReal): PositiveOrZeroReal = {
    val division = x / y
    if (x == 0 || division * y != x) PositiveOrZeroReal(Double.MaxValue) else wrap(division)
  }

  def plus(x: PositiveOrZeroReal, y: PositiveOrZeroReal): PositiveOrZeroReal = {
    val sum = x + y
    if (sum < 0) PositiveOrZeroReal(Double.MaxValue) else wrap(sum)
  }

  def minus(x: PositiveOrZeroReal, y: PositiveOrZeroReal): PositiveOrZeroReal = {
    val difference = x - y
    if (difference < 0) zero else wrap(difference)
  }
  
  def max(x: PositiveOrZeroReal, y: PositiveOrZeroReal): PositiveOrZeroReal = {
    if (x < y) y else x
  }

  def min(x: PositiveOrZeroReal, y: PositiveOrZeroReal): PositiveOrZeroReal = {
    if (x < y) x else y
  }

  def unsafeMake(n: Double): PositiveOrZeroReal =
    wrap(n)
}
