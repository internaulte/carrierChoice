package domain.entities

import domain.entities.utils.PositiveOrZeroReal
import domain.entities.utils.Types.PositiveOrZeroReal
import zio.prelude.newtypes.Natural

sealed trait CarrierCompatibility {
  val score: Natural
}

case object FullyCompatible extends CarrierCompatibility {
  override val score: Natural = Natural(4)
}

final case class PartiallyCompatible(override val score: Natural) extends CarrierCompatibility

case object NotCompatible extends CarrierCompatibility {
  override val score: Natural = Natural.zero
}

object CarrierCompatibility {
  def apply(score: Natural): CarrierCompatibility = {
    score match {
      case FullyCompatible.score => FullyCompatible
      case NotCompatible.score => NotCompatible
      case _ => PartiallyCompatible(score)
    }
  }
}
