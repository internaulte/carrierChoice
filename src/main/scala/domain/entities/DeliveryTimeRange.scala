package domain.entities

import java.time.{Duration, LocalTime}

final case class DeliveryTimeRange(
    private val startInterval: LocalTime,
    private val endInterval: LocalTime
) {
  val (correctStartInterval: LocalTime, correctEndInterval: LocalTime) = if (startInterval.isAfter(endInterval)) {
    (endInterval, startInterval)
  } else {
    (startInterval, endInterval)
  }
  lazy val duration: Duration = Duration.between(correctStartInterval, correctEndInterval)

  def containsTotally(otherRange: DeliveryTimeRange): Boolean = {
    val isThisStartsBeforeOtherStarts = !(this.correctStartInterval isAfter otherRange.startInterval)
    val isThisEndsAfterOtherEnds = !(otherRange.correctEndInterval isAfter this.correctEndInterval)

    isThisStartsBeforeOtherStarts && isThisEndsAfterOtherEnds
  }
}
