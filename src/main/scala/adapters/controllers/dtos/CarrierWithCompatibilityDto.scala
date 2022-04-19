package adapters.controllers.dtos

import domain.entities.CarrierWithCompatibility

protected[controllers] final case class CarrierWithCompatibilityDto(
    carrier: CarrierDto,
    carrierCompatibility: CarrierCompatibilityDto
)

protected[controllers] object CarrierWithCompatibilityDto {
  def apply(carrierWithCompatibility: CarrierWithCompatibility): CarrierWithCompatibilityDto = {
    CarrierWithCompatibilityDto(
      carrier = CarrierDto(carrierWithCompatibility.carrier),
      carrierCompatibility = CarrierCompatibilityDto(carrierWithCompatibility.carrierCompatibility)
    )
  }
}
