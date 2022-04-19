package domain.entities

import domain.entities.utils.Point
import domain.entities.utils.Types.DistanceInKm

final case class Area(center: Point, radius: DistanceInKm) {
  def isPointInArea(point: Point): Boolean = {
    if (this.center == point) {
      true
    } else {
      this.center.distanceInKmTo(point) <= this.radius
    }
  }

  def isAreaContainsOther(otherArea: Area): Boolean = {
    val distanceOfCenters = this.center.distanceInKmTo(otherArea.center)
    val maxOtherAreaDistanceToThisCenter = otherArea.radius + distanceOfCenters

    maxOtherAreaDistanceToThisCenter <= this.radius
  }
}
