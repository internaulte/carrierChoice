package adapters.controllers.dtos

import domain.entities.Carrier
import upickle.default.*

protected[controllers] final case class CarrierDto(
    id: String,
    deliveryCategory: DeliveryCategoryDto,
    averageSpeed: Int,
    costPerRide: Long
) derives ReadWriter

protected[controllers] object CarrierDto {
  def apply(carrier: Carrier): CarrierDto = {
    CarrierDto(
      id = carrier.id.toString,
      deliveryCategory = DeliveryCategoryDto(carrier.deliveryCategory),
      averageSpeed = carrier.averageSpeed,
      costPerRide = carrier.costPerRide
    )
  }
}
