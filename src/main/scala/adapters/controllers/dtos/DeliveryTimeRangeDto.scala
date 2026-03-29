package adapters.controllers.dtos

import domain.entities.DeliveryTimeRange
import upickle.default.*

import java.time.LocalDateTime
import scala.util.Try

protected[controllers] final case class DeliveryTimeRangeDto(
    startInterval: String,
    endInterval: String
) derives ReadWriter {
  def toDeliveryTimeRange: Option[DeliveryTimeRange] = {
    for {
      start <- Try(LocalDateTime.parse(startInterval)).toOption
      end <- Try(LocalDateTime.parse(endInterval)).toOption
    } yield DeliveryTimeRange(start, end)
  }
}

protected[controllers] object DeliveryTimeRangeDto {
  def apply(deliveryTimeRange: DeliveryTimeRange): DeliveryTimeRangeDto = {
    DeliveryTimeRangeDto(
      startInterval = deliveryTimeRange.correctStartInterval.toString,
      endInterval = deliveryTimeRange.correctEndInterval.toString
    )
  }
}
