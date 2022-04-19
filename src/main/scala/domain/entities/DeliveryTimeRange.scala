package domain.entities

import java.time.{Duration, LocalTime}

final case class DeliveryTimeRange(
    startInterval: LocalTime,
    endInterval: LocalTime
) {
  lazy val duration: Duration = Duration.between(startInterval, endInterval)

  def containsTotally(otherRange: DeliveryTimeRange): Boolean = {
    val isThisStartsBeforeOtherStarts = this.startInterval isBefore otherRange.startInterval
    val isThisEndsAfterOtherEnds = otherRange.endInterval isBefore this.endInterval

    isThisStartsBeforeOtherStarts && isThisEndsAfterOtherEnds
  }
}
