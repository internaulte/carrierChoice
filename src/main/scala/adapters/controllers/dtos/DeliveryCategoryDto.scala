package adapters.controllers.dtos

import domain.entities.DeliveryCategory
import domain.entities.utils.types.{VolumeInMillim3, WeightInGram}
import upickle.default.*

protected[controllers] final case class DeliveryCategoryDto(
    deliveryTimeRange: DeliveryTimeRangeDto,
    deliveryArea: AreaDto,
    totalWeight: Long,
    totalVolume: Long,
    maxPackageWeight: Long
) derives ReadWriter {
  def toDeliveryCategory: Option[DeliveryCategory] = {
    for {
      area <- deliveryArea.toArea
      timeRange <- deliveryTimeRange.toDeliveryTimeRange
      validTotalWeight <- WeightInGram(totalWeight)
      validTotalVolume <- VolumeInMillim3(totalVolume)
      validMaxPackageWeight <- WeightInGram(maxPackageWeight)
    } yield DeliveryCategory(
      deliveryTimeRange = timeRange,
      deliveryArea = area,
      totalWeight = validTotalWeight,
      totalVolume = validTotalVolume,
      maxPackageWeight = validMaxPackageWeight
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
