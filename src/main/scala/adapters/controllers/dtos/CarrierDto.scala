package adapters.controllers.dtos

import domain.entities.utils.types.CostInMillis.CostInMillis
import domain.entities.utils.types.SpeedInMetersPerSecond.SpeedInMetersPerSecond
import domain.entities.{Carrier, DeliveryCategory}
import domain.entities.utils.types.{CostInMillis, SpeedInMetersPerSecond}

import java.util.UUID

protected[controllers] final case class CarrierDto(
    id: UUID,
    deliveryCategory: DeliveryCategoryDto,
    averageSpeed: SpeedInMetersPerSecond,
    costPerRide: CostInMillis
)

protected[controllers] object CarrierDto {
  def apply(carrier: Carrier): CarrierDto = {
    CarrierDto(
      id = carrier.id,
      deliveryCategory = DeliveryCategoryDto(carrier.deliveryCategory),
      averageSpeed = carrier.averageSpeed,
      costPerRide = carrier.costPerRide
    )
  }
}
