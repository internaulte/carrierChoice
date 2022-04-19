package adapters.controllers.dtos

import domain.entities.{Delivery, DeliveryTimeRange, Package}
import domain.entities.utils.Point
import zio.prelude.NonEmptySet

import java.util.UUID

protected[controllers] final case class DeliveryDto(
    id: UUID,
    withdrawal: PointDto,
    destination: PointDto,
    deliveryTimeRange: DeliveryTimeRangeDto,
    packages: NonEmptySet[PackageDto]
) {
  def toDelivery: Delivery = {
    Delivery(
      id = id,
      withdrawal = withdrawal.toPoint,
      destination = destination.toPoint,
      deliveryTimeRange = deliveryTimeRange.toDeliveryTimeRange,
      packages = packages.map(_.toPackage)
    )
  }
}
