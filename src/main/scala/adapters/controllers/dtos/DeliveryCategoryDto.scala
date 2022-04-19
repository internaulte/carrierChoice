package adapters.controllers.dtos

import domain.entities.{Area, DeliveryCategory, DeliveryTimeRange}
import domain.entities.utils.Types.{VolumeInCm, WeightInKg}

protected[controllers] final case class DeliveryCategoryDto(
    deliveryTimeRange: DeliveryTimeRangeDto,
    deliveryArea: AreaDto,
    totalWeight: WeightInKg,
    totalVolume: VolumeInCm,
    maxPackageWeight: WeightInKg
) {
  def toDeliveryCategory: DeliveryCategory = {
    DeliveryCategory(
      deliveryTimeRange = deliveryTimeRange.toDeliveryTimeRange,
      deliveryArea = deliveryArea.toArea,
      totalWeight = totalWeight,
      totalVolume = totalVolume,
      maxPackageWeight = maxPackageWeight
    )
  }
}

protected[controllers] object DeliveryCategoryDto {
  def apply(deliveryCategory: DeliveryCategory): DeliveryCategoryDto = {
    DeliveryCategoryDto(
      deliveryTimeRange = DeliveryTimeRangeDto(deliveryCategory.deliveryTimeRange),
      deliveryArea = AreaDto(deliveryCategory.deliveryArea),
      totalWeight = deliveryCategory.totalWeight,
      totalVolume = deliveryCategory.totalVolume,
      maxPackageWeight = deliveryCategory.maxPackageWeight
    )
  }
}
