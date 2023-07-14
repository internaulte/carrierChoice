package domain.entities

import domain.entities.utils.types.Natural.Natural
import domain.entities.utils.types.Natural

sealed trait CarrierCompatibility {
  val score: Natural
}

case object FullyCompatible extends CarrierCompatibility {
  override final val score: Natural = Natural.unsafe(4)
}

sealed case class PartiallyCompatible(override final val score: Natural) extends CarrierCompatibility

case object NotCompatible extends CarrierCompatibility {
  override final val score: Natural = Natural.zero
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
