package domain.entities.utils.types

object Natural {
  opaque type Natural <: Int = Int

  val one: Natural = unsafe(1)

  val zero: Natural = unsafe(0)

  def apply(value: Int): Option[Natural] =
    if (value >= 0) Some(value)
    else None

  def unsafe(value: Int): Natural = value

  extension (natural: Natural) {
    def times(x: Natural): Natural = {
      val product = x * natural
      unsafe(product)
    }

    def plus(x: Natural): Natural = {
      val sum = x + natural
      unsafe(sum)
    }

    def maximum(x: Natural): Natural = {
      if (x < natural) natural else x
    }
  }
}
