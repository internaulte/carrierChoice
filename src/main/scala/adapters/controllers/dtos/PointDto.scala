package adapters.controllers.dtos

import domain.entities.utils.Point
import domain.entities.utils.types.{Latitude, Longitude}

protected[controllers] final case class PointDto(latitude: Double, longitude: Double) {
  def toPoint: Option[Point] = {
    for {
      finalLatitude <- Latitude(latitude)
      finalLongitude <- Longitude(longitude)
    } yield Point(latitude = finalLatitude, longitude = finalLongitude)
  }
}

protected[controllers] object PointDto {
  def apply(point: Point): PointDto = {
    PointDto(latitude = point.latitude, longitude = point.longitude)
  }
}
