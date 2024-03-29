package adapters.controllers.dtos

import domain.entities.utils.types.VolumeInMillim3.VolumeInMillim3
import domain.entities.utils.types.WeightInGram.WeightInGram
import domain.entities.{Area, DeliveryCategory, DeliveryTimeRange}
import domain.entities.utils.types.{VolumeInMillim3, WeightInGram}

protected[controllers] final case class DeliveryCategoryDto(
    deliveryTimeRange: DeliveryTimeRangeDto,
    deliveryArea: AreaDto,
    totalWeight: WeightInGram,
    totalVolume: VolumeInMillim3,
    maxPackageWeight: WeightInGram
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
