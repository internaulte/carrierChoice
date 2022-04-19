package domain.entities

import domain.entities.utils.{Point, PositiveOrZeroReal}
import domain.entities.utils.Types.{DistanceInKm, VolumeInCm, WeightInKg}
import zio.prelude.NonEmptySet

import java.util.UUID

sealed trait BaseDeliveryTrait {
  val totalVolume: VolumeInCm
  val maxPackageWeight: WeightInKg
  val totalWeight: WeightInKg
  val deliveryTimeRange: DeliveryTimeRange
}

final case class Delivery(
    id: UUID,
    withdrawal: Point,
    destination: Point,
    deliveryTimeRange: DeliveryTimeRange,
    packages: NonEmptySet[Package]
) extends BaseDeliveryTrait {
  val (totalVolume: VolumeInCm, maxPackageWeight: WeightInKg, totalWeight: WeightInKg) = packages.foldLeft(
    (PositiveOrZeroReal.zero, PositiveOrZeroReal.zero, PositiveOrZeroReal.zero)
  ) {
    case ((totalVolume, maxSampleWeight, totalWeight), currentPackage) =>
      (
        PositiveOrZeroReal.plus(totalVolume, currentPackage.volume),
        PositiveOrZeroReal.max(maxSampleWeight, currentPackage.weightInKg),
        PositiveOrZeroReal.plus(totalWeight, currentPackage.weightInKg)
      )
  }

  lazy val travelDistance: DistanceInKm = destination.distanceInKmTo(withdrawal)
}

final case class DeliveryCategory(
    deliveryTimeRange: DeliveryTimeRange,
    deliveryArea: Area,
    totalWeight: WeightInKg,
    totalVolume: VolumeInCm,
    maxPackageWeight: WeightInKg
) extends BaseDeliveryTrait
