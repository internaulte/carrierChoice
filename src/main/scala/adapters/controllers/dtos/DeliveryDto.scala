package adapters.controllers.dtos

import domain.entities.{Delivery, DeliveryTimeRange, Package}
import domain.entities.utils.Point
import domain.entities.utils.types.NonEmptyList
import domain.entities.utils.types.NonEmptyList.NonEmptyList

import java.util.UUID

protected[controllers] final case class DeliveryDto(
    id: UUID,
    withdrawal: PointDto,
    destination: PointDto,
    deliveryTimeRange: DeliveryTimeRangeDto,
    packages: Seq[PackageDto]
) {
  def toDelivery: Option[Delivery] = {
    for {
      withdrawalPoint <- withdrawal.toPoint
      destinationPoint <- destination.toPoint
      optionPackages = packages.map(_.toPackage)
      isAnyPackageNone = optionPackages.exists(_.isEmpty)
      packages <- if (isAnyPackageNone) {
        None
      } else {
        Some(optionPackages.map(_.get))
      }
      finalPackages <- NonEmptyList.fromIterable(packages)
    } yield Delivery(
      id = id,
      withdrawal = withdrawalPoint,
      destination = destinationPoint,
      deliveryTimeRange = deliveryTimeRange.toDeliveryTimeRange,
      packages = finalPackages
    )
  }
}
