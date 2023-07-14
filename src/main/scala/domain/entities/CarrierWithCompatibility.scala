package domain.entities

import domain.entities.utils.types.Natural.Natural

final case class CarrierWithCompatibility(carrier: Carrier, carrierCompatibility: CarrierCompatibility) {
  def carrierScore: Natural = carrierCompatibility.score
}

object CarrierWithCompatibility {
  def getCheaperCarrierOfTwo(
      carrierWithCompatibility: CarrierWithCompatibility,
      otherCarrierWithCompatibility: CarrierWithCompatibility
  ): CarrierWithCompatibility = {
    if (carrierWithCompatibility.carrier.isCheaperThanOther(otherCarrierWithCompatibility.carrier)) {
      carrierWithCompatibility
    } else {
      otherCarrierWithCompatibility
    }
  }
}
