package domain.entities

import domain.entities.utils.types.CostInMillis.CostInMillis
import domain.entities.utils.types.{DurationInSeconds, Natural}
import domain.entities.utils.types.DurationInSeconds.DurationInSeconds
import domain.entities.utils.types.Natural.Natural
import domain.entities.utils.types.SpeedInMetersPerSecond.SpeedInMetersPerSecond
import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram
import domain.entities.{Delivery, DeliveryCategory}

import java.time.Duration
import java.util.UUID

final case class Carrier(
    id: UUID,
    deliveryCategory: DeliveryCategory,
    averageSpeed: SpeedInMetersPerSecond,
    costPerRide: CostInMillis
) {
  def isCheaperThanOther(otherCarrier: Carrier): Boolean = {
    costPerRide <= otherCarrier.costPerRide
  }

  def getCompatibilityForDeliveryCategory(deliveryCategory: DeliveryCategory): CarrierCompatibility = {
    val finalScore = getTransportAndSchedulingDeliveryZonePossiblityScore(deliveryCategory)

    CarrierCompatibility(finalScore)
  }

  def getCompatibilityForDelivery(delivery: Delivery): CarrierCompatibility = {
    val transportAndSchedulingAndDeliveryZonePossiblityScore = getTransportAndSchedulingDeliveryZonePossiblityScore(
      deliveryCategory
    )

    val deliveryInTimeScore = if (isDeliveryPossibleInTime(delivery)) Natural.one else Natural.zero

    val finalScore = transportAndSchedulingAndDeliveryZonePossiblityScore.plus(deliveryInTimeScore)

    CarrierCompatibility(finalScore)
  }

  private def getTransportAndSchedulingDeliveryZonePossiblityScore(deliveryTraitObject: BaseDeliveryTrait): Natural = {
    val transportAndSchedulingPossiblityScore = getTransportAndSchedulingPossiblityScore(deliveryTraitObject)
    val deliveryZonePossibilityScore = deliveryTraitObject match {
      case delivery: Delivery => getDeliveryPossibleScore(delivery)
      case deliveryCategory: DeliveryCategory => getDeliveryZonePossibleScore(deliveryCategory)
    }

    transportAndSchedulingPossiblityScore.plus(deliveryZonePossibilityScore)
  }

  private def getTransportAndSchedulingPossiblityScore(deliveryTraitObject: BaseDeliveryTrait): Natural = {
    val transportPossibilityScore = getTransportPossiblityScore(
      maxPackageWeight = deliveryTraitObject.maxPackageWeight,
      totalVolume = deliveryTraitObject.totalVolume,
      totalWeight = deliveryTraitObject.totalWeight
    )
    val timePossibilityScore = getSchedulingPossibilityScore(deliveryTimeRange = deliveryTraitObject.deliveryTimeRange)

    transportPossibilityScore.plus(timePossibilityScore)
  }

  private[entities] def getTransportPossiblityScore(
      maxPackageWeight: WeightInGram,
      totalVolume: VolumeInMillim3,
      totalWeight: WeightInGram
  ): Natural = {
    val isMaxPackageWeightCompatible = maxPackageWeight <= this.deliveryCategory.maxPackageWeight
    val isMaxVolumeCompatible = totalVolume <= this.deliveryCategory.totalVolume
    val isTotalWeightCompatible = totalWeight <= this.deliveryCategory.totalWeight

    if (isMaxPackageWeightCompatible && isMaxVolumeCompatible && isTotalWeightCompatible) {
      Natural.one
    } else {
      Natural.zero
    }
  }

  private[entities] def getSchedulingPossibilityScore(deliveryTimeRange: DeliveryTimeRange): Natural = {
    val isSchedulingPossible = this.deliveryCategory.deliveryTimeRange.containsTotally(deliveryTimeRange)

    if (isSchedulingPossible) {
      Natural.one
    } else {
      Natural.zero
    }
  }

  private[entities] def getDeliveryPossibleScore(delivery: Delivery): Natural = {
    val isDeliveryZonePossible = this.deliveryCategory.deliveryArea.isPointInArea(delivery.withdrawal) &&
      this.deliveryCategory.deliveryArea.isPointInArea(delivery.destination)

    if (isDeliveryZonePossible) {
      Natural.one
    } else {
      Natural.zero
    }
  }

  private[entities] def getDeliveryZonePossibleScore(deliveryCategory: DeliveryCategory): Natural = {
    val isDeliveryZonePossible = this.deliveryCategory.deliveryArea.isAreaContainsOther(deliveryCategory.deliveryArea)

    if (isDeliveryZonePossible) {
      Natural.one
    } else {
      Natural.zero
    }
  }

  private[entities] def isDeliveryPossibleInTime(delivery: Delivery): Boolean = {
    val predictedDeliveryTimeSeconds: DurationInSeconds = DurationInSeconds.fromDistanceAndSpeed(
      travelDistance = delivery.travelDistance,
      averageSpeed = this.averageSpeed
    )

    val predictedDeliveryDuration = Duration.ofSeconds(predictedDeliveryTimeSeconds)

    !predictedDeliveryDuration.minus(this.deliveryCategory.deliveryTimeRange.duration).isPositive
  }
}
