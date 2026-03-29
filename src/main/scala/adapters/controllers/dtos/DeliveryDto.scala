package adapters.controllers.dtos

import domain.entities.Delivery
import domain.entities.utils.types.NonEmptyList
import upickle.default.*

import java.util.UUID
import scala.util.Try

protected[controllers] final case class DeliveryDto(
    id: String,
    withdrawal: PointDto,
    destination: PointDto,
    deliveryTimeRange: DeliveryTimeRangeDto,
    packages: Seq[PackageDto]
) derives ReadWriter {
  def toDelivery: Option[Delivery] = {
    for {
      uuid <- Try(UUID.fromString(id)).toOption
      withdrawalPoint <- withdrawal.toPoint
      destinationPoint <- destination.toPoint
      optionPackages = packages.map(_.toPackage)
      isAnyPackageNone = optionPackages.exists(_.isEmpty)
      packages <-
        if (isAnyPackageNone) {
          None
        } else {
          Some(optionPackages.map(_.get))
        }
      finalPackages <- NonEmptyList.fromIterable(packages)
      timeRange <- deliveryTimeRange.toDeliveryTimeRange
    } yield Delivery(
      id = uuid,
      withdrawal = withdrawalPoint,
      destination = destinationPoint,
      deliveryTimeRange = timeRange,
      packages = finalPackages
    )
  }
}
