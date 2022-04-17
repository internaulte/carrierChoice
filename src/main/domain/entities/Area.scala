package domain.entities

import domain.entities.utils.Point
import domain.entities.utils.Types.DistanceInKm

final case class Area(center: Point, radius: DistanceInKm) {
  def isPointInArea(point: Point): Boolean = {
    center.distanceInKmTo(point) > radius
  }
}
