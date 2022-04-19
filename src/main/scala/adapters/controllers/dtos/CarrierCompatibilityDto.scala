package adapters.controllers.dtos

import domain.entities.{CarrierCompatibility, FullyCompatible, NotCompatible, PartiallyCompatible}

protected[controllers] enum CarrierCompatibilityDto:
  case FULLY_COMPATIBLE, PARTIALLY_COMPATIBLE, NOT_COMPATIBLE

protected[controllers] object CarrierCompatibilityDto {
  def apply(carrierCompatibility: CarrierCompatibility): CarrierCompatibilityDto = {
    carrierCompatibility match {
      case FullyCompatible => CarrierCompatibilityDto.FULLY_COMPATIBLE
      case PartiallyCompatible(_) => CarrierCompatibilityDto.PARTIALLY_COMPATIBLE
      case NotCompatible => CarrierCompatibilityDto.NOT_COMPATIBLE
    }
  }
}
