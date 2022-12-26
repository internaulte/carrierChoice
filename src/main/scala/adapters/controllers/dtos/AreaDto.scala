package adapters.controllers.dtos

import domain.entities.Area
import domain.entities.utils.Types.DistanceInMeters

protected[controllers] final case class AreaDto(center: PointDto, radius: DistanceInMeters) {
  def toArea: Area = {
    Area(center = center.toPoint, radius = radius)
  }
}

protected[controllers] object AreaDto {
  def apply(area: Area): AreaDto = {
    AreaDto(center = PointDto(area.center), radius = area.radius)
  }
}
