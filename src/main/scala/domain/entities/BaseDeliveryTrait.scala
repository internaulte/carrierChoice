package domain.entities

import domain.entities.utils.types.{DistanceInMeters, VolumeInMillim3, WeightInGram}
import domain.entities.utils.Point
import domain.entities.utils.types.LongNatural
import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram
import domain.entities.utils.types.DistanceInMeters.DistanceInMeters
import domain.entities.utils.types.NonEmptyList.NonEmptyList

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
    override val deliveryTimeRange: DeliveryTimeRange,
    packages: NonEmptyList[Package]
) extends BaseDeliveryTrait {
  override val (totalVolume: VolumeInMillim3, maxPackageWeight: WeightInGram, totalWeight: WeightInGram) =
    packages.foldLeft(
      (VolumeInMillim3.zero, WeightInGram.zero, WeightInGram.zero)
    ) {
      case ((totalVolume, maxSampleWeight, totalWeight), currentPackage) =>
        (
          totalVolume.addition(currentPackage.volume),
          maxSampleWeight.getMaximum(currentPackage.weightInGram),
          totalWeight.addition(currentPackage.weightInGram)
        )
    }

  lazy val travelDistance: DistanceInMeters = destination.distanceInMetersTo(withdrawal)
}

final case class DeliveryCategory(
    override val deliveryTimeRange: DeliveryTimeRange,
    deliveryArea: Area,
    override val totalWeight: WeightInGram,
    override val totalVolume: VolumeInMillim3,
    override val maxPackageWeight: WeightInGram
) extends BaseDeliveryTrait
