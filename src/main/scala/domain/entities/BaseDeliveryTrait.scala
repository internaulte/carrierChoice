package domain.entities

import domain.entities.utils.types.{DistanceInMeters, VolumeInMillim3, WeightInGram}
import domain.entities.utils.Point
import domain.entities.utils.types.LongNatural
import zio.prelude.NonEmptySet

import java.util.UUID

sealed trait BaseDeliveryTrait {
  val totalVolume: VolumeInMillim3
  val maxPackageWeight: WeightInGram
  val totalWeight: WeightInGram
  val deliveryTimeRange: DeliveryTimeRange
}

final case class Delivery(
    id: UUID,
    withdrawal: Point,
    destination: Point,
    deliveryTimeRange: DeliveryTimeRange,
    packages: NonEmptySet[Package]
) extends BaseDeliveryTrait {
  val (totalVolume: VolumeInMillim3, maxPackageWeight: WeightInGram, totalWeight: WeightInGram) = packages.foldLeft(
    (LongNatural.zero, LongNatural.zero, LongNatural.zero)
  ) {
    case ((totalVolume, maxSampleWeight, totalWeight), currentPackage) =>
      (
        LongNatural.plus(totalVolume, currentPackage.volume),
        LongNatural.max(maxSampleWeight, currentPackage.weightInGram),
        LongNatural.plus(totalWeight, currentPackage.weightInGram)
      )
  }

  lazy val travelDistance: DistanceInMeters = destination.distanceInMetersTo(withdrawal)
}

final case class DeliveryCategory(
    deliveryTimeRange: DeliveryTimeRange,
    deliveryArea: Area,
    totalWeight: WeightInGram,
    totalVolume: VolumeInMillim3,
    maxPackageWeight: WeightInGram
) extends BaseDeliveryTrait
