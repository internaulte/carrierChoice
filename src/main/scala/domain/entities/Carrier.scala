package domain.entities

import domain.entities.utils.Types.*
import domain.entities.utils.LongNatural
import domain.entities.{Delivery, DeliveryCategory}
import zio.prelude.newtypes.Natural

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

    val finalScore = Natural.plus(transportAndSchedulingAndDeliveryZonePossiblityScore, deliveryInTimeScore)

    CarrierCompatibility(finalScore)
  }

  private def getTransportAndSchedulingDeliveryZonePossiblityScore(deliveryTraitObject: BaseDeliveryTrait): Natural = {
    val transportAndSchedulingPossiblityScore = getTransportAndSchedulingPossiblityScore(deliveryCategory)
    val deliveryZonePossibilityScore = deliveryTraitObject match {
      case delivery: Delivery => getDeliveryZonePossibleScore(delivery)
      case deliveryCategory: DeliveryCategory => getDeliveryZonePossibleScore(deliveryCategory)
    }

    Natural.plus(transportAndSchedulingPossiblityScore, deliveryZonePossibilityScore)
  }

  private def getTransportAndSchedulingPossiblityScore(deliveryTraitObject: BaseDeliveryTrait): Natural = {
    val transportPossibilityScore = getTransportPossiblityScore(
      maxPackageWeight = deliveryTraitObject.maxPackageWeight,
      totalVolume = deliveryTraitObject.totalVolume,
      totalWeight = deliveryTraitObject.totalWeight
    )
    val timePossibilityScore = getSchedulingPossibilityScore(deliveryTimeRange = deliveryCategory.deliveryTimeRange)

    Natural.plus(transportPossibilityScore, timePossibilityScore)
  }

  private def getTransportPossiblityScore(
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

  private def getSchedulingPossibilityScore(deliveryTimeRange: DeliveryTimeRange): Natural = {
    val isSchedulingPossible = this.deliveryCategory.deliveryTimeRange.containsTotally(deliveryTimeRange)

    if (isSchedulingPossible) {
      Natural.one
    } else {
      Natural.zero
    }
  }

  private def getDeliveryZonePossibleScore(delivery: Delivery): Natural = {
    val isDeliveryZonePossible = this.deliveryCategory.deliveryArea.isPointInArea(delivery.withdrawal) &&
      this.deliveryCategory.deliveryArea.isPointInArea(delivery.destination)

    if (isDeliveryZonePossible) {
      Natural.one
    } else {
      Natural.zero
    }
  }

  private def getDeliveryZonePossibleScore(deliveryCategory: DeliveryCategory): Natural = {
    val isDeliveryZonePossible = this.deliveryCategory.deliveryArea.isAreaContainsOther(deliveryCategory.deliveryArea)

    if (isDeliveryZonePossible) {
      Natural.one
    } else {
      Natural.zero
    }
  }

  private def isDeliveryPossibleInTime(delivery: Delivery): Boolean = {
    val predictedDeliveryTimeSeconds = LongNatural.divides(delivery.travelDistance, this.averageSpeed)
    val predictedDeliveryDuration = Duration.ofSeconds(predictedDeliveryTimeSeconds)

    predictedDeliveryDuration.minus(this.deliveryCategory.deliveryTimeRange.duration).isNegative
  }
}
