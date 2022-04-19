package adapters.controllers.dtos

import domain.entities.Area
import domain.entities.utils.Types.DistanceInKm

protected[controllers] final case class AreaDto(center: PointDto, radius: DistanceInKm) {
  def toArea: Area = {
    Area(center = center.toPoint, radius = radius)
  }
}

protected[controllers] object AreaDto {
  def apply(area: Area): AreaDto = {
    AreaDto(center = PointDto(area.center), radius = area.radius)
  }
}
