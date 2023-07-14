package domain.entities.utils.types

object LongNatural {
  opaque type LongNatural <: Long = Long

  val one: LongNatural = unsafe(1)

  val zero: LongNatural = unsafe(0)

  def apply(value: Long): Option[LongNatural] =
    if (value >= 0) Some(value)
    else None

  def unsafe(value: Long): LongNatural = value

  extension (longNatural: LongNatural) {
    def times(x: LongNatural): LongNatural = {
      val product = x * longNatural
      unsafe(product)
    }

    def plus(x: LongNatural): LongNatural = {
      val sum = x + longNatural
      unsafe(sum)
    }

    def maximum(x: LongNatural): LongNatural = {
      if (x < longNatural) longNatural else x
    }
  }
}
