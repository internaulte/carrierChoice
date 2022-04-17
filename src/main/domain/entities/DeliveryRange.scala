package domain.entities

import domain.entities.utils.DayTime

final case class DeliveryRange(
    startInterval: DayTime,
    endInterval: DayTime
) {
  def isOtherRangeOverlaps(otherRange: DeliveryRange): Boolean = {
    val isThisEndsBeforeOtherStarts = this.endInterval isStrictBefore otherRange.startInterval
    val isOtherEndsBeforeThisStarts = otherRange.endInterval isStrictBefore this.startInterval

    !isThisEndsBeforeOtherStarts && !isOtherEndsBeforeThisStarts
  }
}
