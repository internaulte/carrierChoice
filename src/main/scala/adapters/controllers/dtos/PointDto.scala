package adapters.controllers.dtos

import domain.entities.utils.Point
import domain.entities.utils.types.{Latitude, Longitude}

protected[controllers] final case class PointDto(latitude: Latitude, longitude: Longitude) {
  def toPoint: Point = {
    Point(latitude = latitude, longitude = longitude)
  }
}

protected[controllers] object PointDto {
  def apply(point: Point): PointDto = {
    PointDto(latitude = point.latitude, longitude = point.longitude)
  }
}
