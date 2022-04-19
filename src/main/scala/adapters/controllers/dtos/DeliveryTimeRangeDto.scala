package adapters.controllers.dtos

import domain.entities.DeliveryTimeRange

import java.time.LocalTime

protected[controllers] final case class DeliveryTimeRangeDto(
    startInterval: LocalTime,
    endInterval: LocalTime
) {
  def toDeliveryTimeRange: DeliveryTimeRange = {
    DeliveryTimeRange(startInterval, endInterval)
  }
}

protected[controllers] object DeliveryTimeRangeDto {
  def apply(deliveryTimeRange: DeliveryTimeRange): DeliveryTimeRangeDto = {
    DeliveryTimeRangeDto(startInterval = deliveryTimeRange.startInterval, endInterval = deliveryTimeRange.endInterval)
  }
}
