package domain.entities

import java.time.{Duration, LocalDateTime}

final case class DeliveryTimeRange(
    private val startInterval: LocalDateTime,
    private val endInterval: LocalDateTime
) {
  val (correctStartInterval: LocalDateTime, correctEndInterval: LocalDateTime) =
    if (startInterval.isAfter(endInterval)) {
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
