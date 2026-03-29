package adapters.controllers.dtos

import domain.entities.Area
import domain.entities.utils.types.{DistanceInMeters, Natural}
import upickle.default.*

protected[controllers] final case class AreaDto(center: PointDto, radius: Int) derives ReadWriter {
  def toArea: Option[Area] = {
    for {
      point <- center.toPoint
      naturalRadius <- Natural(radius)
    } yield Area(center = point, radius = DistanceInMeters(naturalRadius))
  }
}

protected[controllers] object AreaDto {
  def apply(area: Area): AreaDto = {
    AreaDto(center = PointDto(area.center), radius = area.radius)
  }
}
