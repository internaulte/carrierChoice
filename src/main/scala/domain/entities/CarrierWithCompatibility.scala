package domain.entities

import zio.prelude.newtypes.Natural

final case class CarrierWithCompatibility(carrier: Carrier, carrierCompatibility: CarrierCompatibility) {
  def carrierScore: Natural = this.carrierCompatibility.score
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
