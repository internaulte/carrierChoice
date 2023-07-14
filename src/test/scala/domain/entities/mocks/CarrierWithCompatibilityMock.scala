package domain.entities.mocks

import core.Mock
import domain.entities.{Carrier, CarrierCompatibility, CarrierWithCompatibility, FullyCompatible}

final class CarrierWithCompatibilityMock extends Mock[CarrierWithCompatibility] {
  private var carrier: Carrier = new CarrierMock().build()
  private var carrierCompatibility: CarrierCompatibility = FullyCompatible
  
  def setCarrier(carrier: Carrier): CarrierWithCompatibilityMock = {
    this.carrier = carrier
    this
  }
  
  def setCarrierCompatibility(carrierCompatibility: CarrierCompatibility): CarrierWithCompatibilityMock = {
    this.carrierCompatibility = carrierCompatibility
    this
  }

  override def build(): CarrierWithCompatibility = {
    CarrierWithCompatibility(
      carrier = this.carrier,
      carrierCompatibility = this.carrierCompatibility
    )
  }
}
