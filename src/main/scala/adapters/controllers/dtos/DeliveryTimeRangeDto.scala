package adapters.controllers.dtos

import domain.entities.DeliveryTimeRange

import java.time.LocalDateTime

protected[controllers] final case class DeliveryTimeRangeDto(
    startInterval: LocalDateTime,
    endInterval: LocalDateTime
) {
  def toDeliveryTimeRange: DeliveryTimeRange = {
    DeliveryTimeRange(startInterval, endInterval)
  }
}

protected[controllers] object DeliveryTimeRangeDto {
  def apply(deliveryTimeRange: DeliveryTimeRange): DeliveryTimeRangeDto = {
    DeliveryTimeRangeDto(
      startInterval = deliveryTimeRange.correctStartInterval,
      endInterval = deliveryTimeRange.correctEndInterval
    )
  }
}
